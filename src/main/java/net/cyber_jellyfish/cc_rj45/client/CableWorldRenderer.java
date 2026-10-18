package net.cyber_jellyfish.cc_rj45.client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.cyber_jellyfish.cc_rj45.util.CableLink;
//? if <=1.21.1 {
/*import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CableWorldRenderer {
    public static final float HANG = 1.0f;

    @FunctionalInterface
    public interface GeometryRunner {
        void draw(java.util.function.BiConsumer<VertexConsumer, Matrix4f> drawer);
    }

    //? if <=1.21.1 {
    /*public static final ResourceLocation WHITE_TEXTURE = ResourceLocation.parse("ccrj45:textures/misc/white.png");

    public static void render(PoseStack poseStack, MultiBufferSource bufferSource, Camera camera, Frustum frustum, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Level renderLevel = mc.level;
        if (renderLevel == null) return;

        Vec3 cameraPos = camera.getPosition();
        GeometryRunner runner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.entityCutoutNoCull(WHITE_TEXTURE)), poseStack.last().pose());
        renderCables(poseStack, runner, cameraPos, frustum, partialTick, renderLevel);
    }
    *///?} else {
    public static final Identifier WHITE_TEXTURE = Identifier.parse("ccrj45:textures/misc/white.png");

    public static void render(PoseStack poseStack, SubmitNodeCollector collector, Vec3 cameraPos, Frustum frustum, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Level renderLevel = mc.level;
        if (renderLevel == null) return;

        GeometryRunner runner = drawer -> collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(WHITE_TEXTURE, false), (pose, consumer) -> drawer.accept(consumer, pose.pose()));
        renderCables(poseStack, runner, cameraPos, frustum, partialTick, renderLevel);
    }
    //?}

    public static void renderCables(PoseStack poseStack, GeometryRunner runner, Vec3 cameraPos, Frustum frustum, float partialTick, Level renderLevel) {
        Set<String> drawn = new HashSet<>();

        for (NetCableConnectorBlockEntity be : NetCableConnectorBlockEntity.ALL_CONNECTORS) {
            if (be.isRemoved()) continue;
            Level beLevel = be.getLevel();
            if (beLevel == null || !beLevel.isClientSide()) continue;

            BlockPos fromPos = be.getBlockPos();
            UUID fromSubUUID = SableHelper.getContainingUUID(be);
            if (fromSubUUID == null) {
                fromSubUUID = SableHelper.getContainingUUID(beLevel, fromPos);
            }
            if (beLevel != renderLevel && fromSubUUID == null) continue;
            if (fromSubUUID == null && SableHelper.isInSubLevel(beLevel, fromPos)) continue;

            Vec3 fromOrigin = NetCableConnectorRenderer.cableOrigin(be);
            Vec3 fromLocalPort = Vec3.atLowerCornerOf(fromPos).add(fromOrigin);

            Vec3 fromGlobal = SableHelper.isAvailable()
                    ? SableHelper.projectOutFromBE(be, fromLocalPort, partialTick)
                    : fromLocalPort;

            for (CableLink link : be.getLinks()) {
                UUID toSubUUID = link.subLevel();

                // Eliminate double rendering cleanly
                String pairKey = makePairKey(fromSubUUID, fromPos, toSubUUID, link.pos());
                if (!drawn.add(pairKey)) continue;

                Vec3 toGlobal;
                if (toSubUUID != null || fromSubUUID != null) {
                    if (toSubUUID == fromSubUUID || (toSubUUID != null && toSubUUID.equals(fromSubUUID))) {
                        NetCableConnectorBlockEntity targetBe = findConnectorByPos(link.pos(), toSubUUID);
                        if (targetBe != null) {
                            Vec3 toLocal = Vec3.atLowerCornerOf(link.pos()).add(NetCableConnectorRenderer.cableOrigin(targetBe));
                            toGlobal = SableHelper.projectOutFromBE(targetBe, toLocal, partialTick);
                        } else {
                            Vec3 toLocal = Vec3.atLowerCornerOf(link.pos()).add(fromOrigin);
                            toGlobal = SableHelper.projectOutFromBE(be, toLocal, partialTick);
                        }
                    } else {
                        toGlobal = computeToGlobal(link, toSubUUID, partialTick, renderLevel);
                    }
                } else {
                    NetCableConnectorBlockEntity targetBe = findConnectorByPos(link.pos(), null);
                    if (targetBe != null) {
                        Vec3 toOrigin = NetCableConnectorRenderer.cableOrigin(targetBe);
                        toGlobal = Vec3.atLowerCornerOf(link.pos()).add(toOrigin);
                    } else {
                        BlockEntity raw = renderLevel.getBlockEntity(link.pos());
                        if (raw instanceof NetCableConnectorBlockEntity toEntity) {
                            Vec3 toOrigin = NetCableConnectorRenderer.cableOrigin(toEntity);
                            toGlobal = Vec3.atLowerCornerOf(link.pos()).add(toOrigin);
                        } else {
                            toGlobal = Vec3.atLowerCornerOf(link.pos()).add(fromOrigin);
                        }
                    }
                }

                if (toGlobal == null) continue;

                double dist = fromGlobal.distanceTo(toGlobal);
                if (dist > 256.0 || dist < 1.0e-5) continue;

                int maxDist = be.getMaxLinkDistance();
                NetCableConnectorBlockEntity targetConnector = findConnectorByPos(link.pos(), toSubUUID);
                if (targetConnector != null) {
                    maxDist = Math.min(maxDist, targetConnector.getMaxLinkDistance());
                }

                float hangAmount = calculateHangAmount((float) dist, maxDist);
                float cableWidth = be.getCableWidth() * calculateTensionWidthFactor((float) dist, maxDist);

                double minX = Math.min(fromGlobal.x, toGlobal.x) - 0.5;
                double maxX = Math.max(fromGlobal.x, toGlobal.x) + 0.5;
                double minY = Math.min(fromGlobal.y, toGlobal.y) - hangAmount - 0.5;
                double maxY = Math.max(fromGlobal.y, toGlobal.y) + 0.5;
                double minZ = Math.min(fromGlobal.z, toGlobal.z) - 0.5;
                double maxZ = Math.max(fromGlobal.z, toGlobal.z) + 0.5;
                AABB cableAABB = new AABB(minX, minY, minZ, maxX, maxY, maxZ);

                // Distance culling: max 128 blocks from camera
                if (cableAABB.distanceToSqr(cameraPos) > 128.0 * 128.0) continue;

                // Frustum culling on the wire itself: unloads/disappears ONLY when the wire cannot be seen!
                if (frustum != null && !frustum.isVisible(cableAABB)) continue;

                int cr = be.getCableColorR();
                int cg = be.getCableColorG();
                int cb = be.getCableColorB();

                if (link.color() != null) {
                    int diffuse = link.color().getTextureDiffuseColor();
                    int rawR = (diffuse >> 16) & 0xFF;
                    int rawG = (diffuse >> 8) & 0xFF;
                    int rawB = diffuse & 0xFF;
                    int[] desat = adjustCableColor(rawR, rawG, rawB, 0.60f);
                    cr = desat[0];
                    cg = desat[1];
                    cb = desat[2];
                }

                // Canonicalize orientation so start and end never flip between frames
                boolean swap = false;
                if (fromSubUUID != null || toSubUUID != null) {
                    if (fromSubUUID != null && toSubUUID == null) {
                        swap = true; // Fixed ground is always start, moving ship is always end
                    } else if (fromSubUUID != null && toSubUUID != null) {
                        int cmp = fromSubUUID.compareTo(toSubUUID);
                        swap = (cmp > 0) || (cmp == 0 && fromPos.asLong() > link.pos().asLong());
                    }
                } else {
                    swap = fromPos.asLong() > link.pos().asLong();
                }

                Vec3 wireStart = swap ? toGlobal : fromGlobal;
                Vec3 wireEnd = swap ? fromGlobal : toGlobal;

                renderSaggingSquareWire(poseStack, runner, cameraPos, wireStart, wireEnd,
                        cableWidth, cr, cg, cb, renderLevel, hangAmount);
            }
        }
    }

    public static int[] adjustCableColor(int r, int g, int b, float saturation) {
        float luma = 0.2126f * r + 0.7152f * g + 0.0722f * b;
        int dr = Math.round(Mth.clamp(luma + (r - luma) * saturation, 0f, 255f));
        int dg = Math.round(Mth.clamp(luma + (g - luma) * saturation, 0f, 255f));
        int db = Math.round(Mth.clamp(luma + (b - luma) * saturation, 0f, 255f));
        return new int[]{dr, dg, db};
    }

    //? if <=1.21.1 {
    /*public static void renderSaggingSquareWire(PoseStack poseStack, MultiBufferSource bufferSource,
                                               Vec3 cameraPos, Vec3 fromGlobal, Vec3 toGlobal,
                                               float cableWidth, int cr, int cg, int cb,
                                               Level renderLevel, float hangAmount) {
        GeometryRunner runner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.entityCutoutNoCull(WHITE_TEXTURE)), poseStack.last().pose());
        renderSaggingSquareWire(poseStack, runner, cameraPos, fromGlobal, toGlobal, cableWidth, cr, cg, cb, renderLevel, hangAmount);
    }
    *///?}

    public static void renderSaggingSquareWire(PoseStack poseStack, GeometryRunner runner,
                                               Vec3 cameraPos, Vec3 fromGlobal, Vec3 toGlobal,
                                               float cableWidth, int cr, int cg, int cb,
                                               Level renderLevel, float hangAmount) {
        Vec3 start = fromGlobal.subtract(cameraPos);
        Vec3 end = toGlobal.subtract(cameraPos);
        Vec3 mainDelta = toGlobal.subtract(fromGlobal);
        float dist = (float) mainDelta.length();
        if (dist < 1.0e-5f) return;

        // Continuous stable up-reference vector: avoid sudden 90-degree flips
        Vector3f upRef;
        float horizDistSq = (float) (mainDelta.x * mainDelta.x + mainDelta.z * mainDelta.z);
        if (horizDistSq < 1.0e-4f) {
            upRef = new Vector3f(0, 0, 1);
        } else {
            upRef = new Vector3f(0, 1, 0);
        }

        // Smooth physical segmentation: each full segment is exactly SEGMENT_LEN meters,
        // and the tip segment grows smoothly from 0 to SEGMENT_LEN without shifting existing stripes
        final float SEGMENT_LEN = 0.35f;
        float rawSegments = Math.max(4.0f, dist / SEGMENT_LEN);
        int fullSegments = (int) Math.floor(rawSegments);
        float frac = rawSegments - fullSegments;
        int segments = (frac > 1.0e-4f) ? fullSegments + 1 : fullSegments;

        Vec3[] centerPoints = new Vec3[segments + 1];
        for (int i = 0; i <= segments; i++) {
            float t;
            if (i < fullSegments) {
                t = (float) i / rawSegments;
            } else if (i == fullSegments && segments == fullSegments) {
                t = 1.0f;
            } else if (i == fullSegments) {
                t = (float) fullSegments / rawSegments;
            } else {
                t = 1.0f;
            }
            centerPoints[i] = curvePoint(start, end, t, hangAmount);
        }

        Vector3f[] pTL = new Vector3f[segments + 1];
        Vector3f[] pTR = new Vector3f[segments + 1];
        Vector3f[] pBR = new Vector3f[segments + 1];
        Vector3f[] pBL = new Vector3f[segments + 1];
        Vector3f[] normN = new Vector3f[segments + 1];
        Vector3f[] normT = new Vector3f[segments + 1];
        Vector3f[] dirD = new Vector3f[segments + 1];
        int[] lights = new int[segments + 1];

        float hw = cableWidth * 0.5f;

        for (int i = 0; i <= segments; i++) {
            Vec3 p = centerPoints[i];

            Vec3 tangentVec;
            if (i == 0) {
                tangentVec = centerPoints[1].subtract(centerPoints[0]);
            } else if (i == segments) {
                tangentVec = centerPoints[segments].subtract(centerPoints[segments - 1]);
            } else {
                tangentVec = centerPoints[i + 1].subtract(centerPoints[i - 1]);
            }
            Vector3f D = new Vector3f((float) tangentVec.x, (float) tangentVec.y, (float) tangentVec.z);
            if (D.lengthSquared() > 1.0e-6f) {
                D.normalize();
            } else {
                D = (i > 0 && dirD[i - 1] != null) ? new Vector3f(dirD[i - 1]) : new Vector3f(1, 0, 0);
            }
            dirD[i] = D;

            // Stable orthonormal cross-section frame:
            Vector3f T = new Vector3f(D).cross(upRef);
            if (T.lengthSquared() < 1.0e-4f) {
                T = new Vector3f(D).cross(new Vector3f(1, 0, 0));
                if (T.lengthSquared() < 1.0e-4f) {
                    T = new Vector3f(D).cross(new Vector3f(0, 0, 1));
                }
            }
            if (T.lengthSquared() > 1.0e-6f) {
                T.normalize();
            } else {
                T = new Vector3f(0, 0, 1);
            }
            Vector3f N = new Vector3f(T).cross(D);
            if (N.lengthSquared() > 1.0e-6f) {
                N.normalize();
            } else {
                N = new Vector3f(0, 1, 0);
            }

            normT[i] = T;
            normN[i] = N;

            // Stable physical corrugation relief:
            float sliceScale;
            if (i == 0 || i == segments) {
                sliceScale = 1.0f;
            } else if (i < fullSegments) {
                sliceScale = (i % 2 == 1) ? 0.90f : 1.0f;
            } else {
                float targetScale = (i % 2 == 1) ? 0.90f : 1.0f;
                sliceScale = Mth.lerp(frac, 1.0f, targetScale);
            }
            float currentHw = hw * sliceScale;

            Vector3f vT = new Vector3f(T).mul(currentHw);
            Vector3f vN = new Vector3f(N).mul(currentHw);
            Vector3f c = new Vector3f((float) p.x, (float) p.y, (float) p.z);

            pTL[i] = new Vector3f(c).sub(vT).add(vN);
            pTR[i] = new Vector3f(c).add(vT).add(vN);
            pBR[i] = new Vector3f(c).add(vT).sub(vN);
            pBL[i] = new Vector3f(c).sub(vT).sub(vN);

            Vec3 worldP = p.add(cameraPos);
            BlockPos lightPos = BlockPos.containing(worldP);
            int b = Math.max(renderLevel.getBrightness(LightLayer.BLOCK, lightPos), 4);
            int s = Math.max(renderLevel.getBrightness(LightLayer.SKY, lightPos), 4);
            lights[i] = (s << 20) | (b << 4);
        }

        runner.draw((consumer, matrix) -> {
            // Render each segment slice as 4 clean flat QUADS
            for (int i = 0; i < segments; i++) {
                int light = lights[i];
                Vector3f N = normN[i];
                Vector3f T = normT[i];
                Vector3f negN = new Vector3f(N).negate();
                Vector3f negT = new Vector3f(T).negate();

                // Smooth physical segment dimming:
                float segmentDim;
                if (i < fullSegments) {
                    segmentDim = (i % 2 == 1) ? 0.74f : 1.0f;
                } else {
                    float targetDim = (i % 2 == 1) ? 0.74f : 1.0f;
                    segmentDim = Mth.lerp(frac, 1.0f, targetDim);
                }

                // Face 1: Top (+N, directional shade 1.0)
                renderQuad(consumer, matrix, pTL[i], pTR[i], pTR[i + 1], pTL[i + 1], N, light, cr, cg, cb, 1.0f * segmentDim);

                // Face 2: Right (+T, directional shade 0.80)
                renderQuad(consumer, matrix, pTR[i], pBR[i], pBR[i + 1], pTR[i + 1], T, light, cr, cg, cb, 0.80f * segmentDim);

                // Face 3: Bottom (-N, directional shade 0.55)
                renderQuad(consumer, matrix, pBR[i], pBL[i], pBL[i + 1], pBR[i + 1], negN, light, cr, cg, cb, 0.55f * segmentDim);

                // Face 4: Left (-T, directional shade 0.68)
                renderQuad(consumer, matrix, pBL[i], pTL[i], pTL[i + 1], pBL[i + 1], negT, light, cr, cg, cb, 0.68f * segmentDim);
            }

            // End caps
            Vector3f startNormal = new Vector3f(dirD[0]).negate();
            renderQuad(consumer, matrix, pTL[0], pBL[0], pBR[0], pTR[0], startNormal, lights[0], cr, cg, cb, 0.75f);

            Vector3f endNormal = dirD[segments];
            renderQuad(consumer, matrix, pTL[segments], pTR[segments], pBR[segments], pBL[segments], endNormal, lights[segments], cr, cg, cb, 0.75f);
        });
    }

    private static void renderQuad(VertexConsumer consumer, Matrix4f matrix,
                                   Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4,
                                   Vector3f normal, int light,
                                   int r, int g, int b, float shade) {
        int qr = Math.min(255, (int) (r * shade));
        int qg = Math.min(255, (int) (g * shade));
        int qb = Math.min(255, (int) (b * shade));

        consumer.addVertex(matrix, p1.x, p1.y, p1.z)
                .setColor(qr, qg, qb, 255)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(normal.x, normal.y, normal.z);
        consumer.addVertex(matrix, p2.x, p2.y, p2.z)
                .setColor(qr, qg, qb, 255)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(normal.x, normal.y, normal.z);
        consumer.addVertex(matrix, p3.x, p3.y, p3.z)
                .setColor(qr, qg, qb, 255)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(normal.x, normal.y, normal.z);
        consumer.addVertex(matrix, p4.x, p4.y, p4.z)
                .setColor(qr, qg, qb, 255)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(normal.x, normal.y, normal.z);
    }

    public static Vec3 curvePoint(Vec3 start, Vec3 end, float t, float hangAmount) {
        double fx = Mth.lerp(t, start.x, end.x);
        double fy = (end.y > start.y
                ? start.y + (end.y - start.y) * t * t
                : end.y - (end.y - start.y) * (1.0 - t) * (1.0 - t))
                - Math.sin(t * Math.PI) * hangAmount;
        double fz = Mth.lerp(t, start.z, end.z);
        return new Vec3(fx, fy, fz);
    }

    @javax.annotation.Nullable
    public static NetCableConnectorBlockEntity findConnectorByPos(BlockPos pos, UUID subUUID) {
        for (NetCableConnectorBlockEntity candidate : NetCableConnectorBlockEntity.ALL_CONNECTORS) {
            if (candidate.isRemoved()) continue;
            Level level = candidate.getLevel();
            if (level == null || !level.isClientSide()) continue;
            if (!candidate.getBlockPos().equals(pos)) continue;
            UUID candidateUUID = SableHelper.getContainingUUID(candidate);
            if (candidateUUID == null) {
                if (subUUID == null) return candidate;
            } else {
                if (subUUID != null && subUUID.equals(candidateUUID)) return candidate;
            }
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            if (subUUID == null) {
                BlockEntity be = mc.level.getBlockEntity(pos);
                if (be instanceof NetCableConnectorBlockEntity c) return c;
            } else if (SableHelper.isAvailable()) {
                Level subLevel = SableHelper.getSubLevelLevel(mc.level, subUUID);
                if (subLevel != null) {
                    BlockEntity be = subLevel.getBlockEntity(pos);
                    if (be instanceof NetCableConnectorBlockEntity c) return c;
                }
            }
        }
        return null;
    }

    public static float calculateHangAmount(float dist, int maxDist) {
        float tensionRatio = dist / maxDist;
        float baseHang = (float) (HANG * dist / 16.0);
        if (tensionRatio >= 0.85f) {
            return 0.0f;
        } else if (tensionRatio > 0.70f) {
            float sagFactor = (0.85f - tensionRatio) / 0.15f;
            return baseHang * sagFactor;
        } else {
            return baseHang;
        }
    }

    public static float calculateTensionWidthFactor(float dist, int maxDist) {
        float tensionRatio = dist / maxDist;
        if (tensionRatio >= 0.85f) {
            float stretchProgress = Math.min(1.0f, (tensionRatio - 0.85f) / 0.15f);
            return Mth.lerp(stretchProgress, 1.0f, 0.65f);
        }
        return 1.0f;
    }

    public static String makePairKey(UUID subA, BlockPos posA, UUID subB, BlockPos posB) {
        long pA = posA.asLong();
        long pB = posB.asLong();
        String sA = (subA != null ? subA.toString() : "") + ":" + pA;
        String sB = (subB != null ? subB.toString() : "") + ":" + pB;
        return sA.compareTo(sB) < 0 ? sA + "|" + sB : sB + "|" + sA;
    }

    @javax.annotation.Nullable
    public static Vec3 computeToGlobal(CableLink link, UUID toSubUUID, float partialTick, Level rootLevel) {
        NetCableConnectorBlockEntity targetBe = findConnectorByPos(link.pos(), toSubUUID);
        if (targetBe == null) return null;
        Vec3 targetOrigin = NetCableConnectorRenderer.cableOrigin(targetBe);
        Vec3 targetLocalPort = Vec3.atLowerCornerOf(link.pos()).add(targetOrigin);
        if (toSubUUID != null) {
            return SableHelper.projectOutAt(rootLevel, toSubUUID, targetLocalPort, partialTick);
        }
        return SableHelper.projectOutFromBE(targetBe, targetLocalPort, partialTick);
    }

    public static class CablePairKey {
        private final long aHash;
        private final long bHash;

        public CablePairKey(Vec3 p1, Vec3 p2) {
            long h1 = hashVec(p1);
            long h2 = hashVec(p2);
            if (h1 > h2) {
                this.aHash = h2;
                this.bHash = h1;
            } else {
                this.aHash = h1;
                this.bHash = h2;
            }
        }

        private static long hashVec(Vec3 v) {
            long x = Math.round(v.x * 20.0);
            long y = Math.round(v.y * 20.0);
            long z = Math.round(v.z * 20.0);
            return (x & 0x1FFFFFL) | ((y & 0x1FFFFFL) << 21) | ((z & 0x1FFFFFL) << 42);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CablePairKey other)) return false;
            return aHash == other.aHash && bHash == other.bHash;
        }

        @Override
        public int hashCode() {
            return (int) (aHash * 31 + bHash);
        }
    }
}
