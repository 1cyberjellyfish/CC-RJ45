package net.cyber_jellyfish.cc_rj45.client;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.cyber_jellyfish.cc_rj45.aero.ClientBridgeState;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

//? if <=1.21.1 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RopeBridgeRenderer {
    private static final int SEGMENTS = 8;
    private static final int RADIAL = 6;
    private static final float WIDTH = 0.08f;
    private static final float HANG = 1.0f;
    private static final double EPS = 1.0e-6;
    private static final int CABLE_COLOR_R = 120;
    private static final int CABLE_COLOR_G = 120;
    private static final int CABLE_COLOR_B = 120;
    private static final int CABLE_ALPHA = 160;

    public static void render(PoseStack poseStack, MultiBufferSource bufferSource, Camera camera, float partialTick) {
        Map<BlockPos, Set<BlockPos>> links = ClientBridgeState.getAllLinks();
        if (links.isEmpty()) return;

        Vec3 cameraPos = camera.getPosition();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.leash());
        Set<BlockPosPair> drawn = new HashSet<>();

        for (Map.Entry<BlockPos, Set<BlockPos>> entry : links.entrySet()) {
            BlockPos from = entry.getKey();
            for (BlockPos to : entry.getValue()) {
                BlockPosPair pair = new BlockPosPair(from, to);
                if (!drawn.add(pair)) continue;

                double dx = to.getX() - from.getX();
                double dy = to.getY() - from.getY();
                double dz = to.getZ() - from.getZ();

                double distSqr = dx * dx + dy * dy + dz * dz;
                if (distSqr > 256 * 256) continue;

                double cx = from.getX() + 0.5 - cameraPos.x;
                double cy = from.getY() + 0.5 - cameraPos.y;
                double cz = from.getZ() + 0.5 - cameraPos.z;

                poseStack.pushPose();
                poseStack.translate(cx, cy, cz);

                int b1 = Minecraft.getInstance().level.getBrightness(LightLayer.BLOCK, from);
                int b2 = Minecraft.getInstance().level.getBrightness(LightLayer.BLOCK, to);
                int s1 = Minecraft.getInstance().level.getBrightness(LightLayer.SKY, from);
                int s2 = Minecraft.getInstance().level.getBrightness(LightLayer.SKY, to);

                float fx = (float) (to.getX() - from.getX());
                float fy = (float) (to.getY() - from.getY());
                float fz = (float) (to.getZ() - from.getZ());

                renderWire(consumer, poseStack.last().pose(), fx, fy, fz, b1, b2, s1, s2);
                poseStack.popPose();
            }
        }
    }

    private static void renderWire(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z,
                                    int b1, int b2, int s1, int s2) {
        float[] ringA = new float[RADIAL * 3];
        float[] ringB = new float[RADIAL * 3];

        computeRing(x, y, z, 0f, ringA);

        for (int i = 0; i < SEGMENTS; i++) {
            float t = (float) (i + 1) / SEGMENTS;
            computeRing(x, y, z, t, ringB);

            float midT = ((float) i + 0.5f) / SEGMENTS;
            int blockLight = (int) Mth.lerp(midT, (float) b1, (float) b2);
            int skyLight = (int) Mth.lerp(midT, (float) s1, (float) s2);
            int light = LightTexture.pack(blockLight, skyLight);

            if (i == 0) {
                for (int j = 0; j < RADIAL; j++) {
                    emitTubeVertex(consumer, matrix, light, ringA, j);
                    emitTubeVertex(consumer, matrix, light, ringB, j);
                }
            } else {
                emitTubeVertex(consumer, matrix, light, ringA, 0);
                emitTubeVertex(consumer, matrix, light, ringB, 0);
                for (int j = 1; j < RADIAL; j++) {
                    emitTubeVertex(consumer, matrix, light, ringA, j);
                    emitTubeVertex(consumer, matrix, light, ringB, j);
                }
            }

            emitTubeVertex(consumer, matrix, light, ringA, 0);
            emitTubeVertex(consumer, matrix, light, ringB, 0);

            float[] temp = ringA;
            ringA = ringB;
            ringB = temp;
        }
    }

    private static void emitTubeVertex(VertexConsumer consumer, Matrix4f matrix, int light, float[] ring, int index) {
        int i = index * 3;
        consumer.addVertex(matrix, ring[i], ring[i + 1], ring[i + 2])
                .setColor(CABLE_COLOR_R, CABLE_COLOR_G, CABLE_COLOR_B, CABLE_ALPHA)
                .setLight(light);
    }

    private static void computeRing(float x, float y, float z, float t, float[] out) {
        Vec3 point = curvePoint(x, y, z, t);
        Vec3 tangent = tangentAt(x, y, z, t, 1.0f / SEGMENTS);

        Vec3 ref = Math.abs(tangent.y) < 0.99f ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 normal = tangent.cross(ref).normalize();
        Vec3 binormal = tangent.cross(normal).normalize();

        float radius = WIDTH * 0.5f;
        for (int j = 0; j < RADIAL; j++) {
            double angle = 2.0 * Math.PI * j / RADIAL;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            out[j * 3] = (float) (point.x + radius * (cos * normal.x + sin * binormal.x));
            out[j * 3 + 1] = (float) (point.y + radius * (cos * normal.y + sin * binormal.y));
            out[j * 3 + 2] = (float) (point.z + radius * (cos * normal.z + sin * binormal.z));
        }
    }

    private static Vec3 curvePoint(float x, float y, float z, float t) {
        float fx = x * t;
        float fy = (y > 0.0F ? y * t * t : y - y * (1.0F - t) * (1.0F - t)) + hang(t, distanceFromZero(x, y, z));
        float fz = z * t;
        return new Vec3(fx, fy, fz);
    }

    private static Vec3 tangentAt(float x, float y, float z, float t, float dt) {
        float t2 = Math.min(1.0f, t + dt);
        Vec3 p1 = curvePoint(x, y, z, t);
        Vec3 p2 = curvePoint(x, y, z, t2);
        Vec3 tangent = p2.subtract(p1);
        if (tangent.lengthSqr() < EPS) {
            tangent = new Vec3(x, y, z);
        }
        if (tangent.lengthSqr() < EPS) {
            tangent = new Vec3(1.0, 0.0, 0.0);
        }
        return tangent.normalize();
    }

    private static float hang(float f, float dis) {
        return (float) Math.sin(-f * Math.PI) * (HANG * dis / 16.0F);
    }

    private static float distanceFromZero(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    private record BlockPosPair(BlockPos a, BlockPos b) {
        BlockPosPair {
            if (a.hashCode() > b.hashCode()) {
                BlockPos tmp = a;
                a = b;
                b = tmp;
            }
        }
    }
}
*///?} else {
public class RopeBridgeRenderer {
    public static void render(Object... args) {}
}
//?}
