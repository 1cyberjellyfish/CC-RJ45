package net.cyber_jellyfish.cc_rj45.client;

import java.util.Set;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.aero.SimulatedReflect;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.cyber_jellyfish.cc_rj45.item.NetCableItem;
import net.cyber_jellyfish.cc_rj45.util.CableLink;
import net.cyber_jellyfish.cc_rj45.util.PlayerDataHelper;
import net.minecraft.ChatFormatting;
//? if <=1.21.1 {
/*import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
*///?} else {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
//?}
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
//? if <=1.21.1 {
/^import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
^///?} else {
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
//?}
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = CCRJ45.MODID, value = Dist.CLIENT)
*///?}
public class CableTensionOverlayRenderer {

    //? if neoforge {
    /*@SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        handleClientRightClickEmpty();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        handleClientRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getPos());
    }

    //? if <=1.21.1 {
    /^@SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        renderOverlay(event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), event.getCamera(), partialTick);
    }
    ^///?} else {
    @SubscribeEvent
    public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        var cameraState = event.getLevelRenderState().cameraRenderState;
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        renderOverlay(event.getPoseStack(), event.getSubmitNodeCollector(), cameraState.pos, partialTick);
    }
    //?}
    *///?}

    public static void handleClientRightClickEmpty() {
        if (ClientCableSelection.isActive()) {
            ClientCableSelection.reset(true);
            //? if <=1.21.1 {
            /*Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable("message.ccrj45.cable.selection_cleared"), false);
            *///?} else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendOverlayMessage(
                        Component.translatable("message.ccrj45.cable.selection_cleared"));
            }
            //?}
        }
    }

    public static void handleClientRightClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos) {
        if (hand != InteractionHand.MAIN_HAND) return;
        if (player == null || !player.getMainHandItem().isEmpty()) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof NetCableConnectorBlockEntity connector)) return;

        if (ClientCableSelection.isActive() && ClientCableSelection.isDisconnect()) {
            ClientCableSelection.reset(false);
            player.swing(InteractionHand.MAIN_HAND);
            return;
        }

        if (player.isShiftKeyDown() && connector.getConnectionCount() > 0) {
            ClientCableSelection.setFirst(pos, connector, true, null, false);
            player.swing(InteractionHand.MAIN_HAND);
        }
    }

    @FunctionalInterface
    public interface LineRunner {
        void draw(java.util.function.BiConsumer<VertexConsumer, Matrix4f> drawer);
    }

    //? if <=1.21.1 {
    /*public static void renderOverlay(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Camera camera, float partialTick) {
        Vec3 camPos = camera.getPosition();
        LineRunner lineRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.lines()), poseStack.last().pose());
        CableWorldRenderer.GeometryRunner quadRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.entityCutoutNoCull(CableWorldRenderer.WHITE_TEXTURE)), poseStack.last().pose());
        renderOverlayCommon(poseStack, lineRunner, quadRunner, camPos, partialTick);
    }
    *///?} else {
    public static void renderOverlay(PoseStack poseStack, SubmitNodeCollector collector, Vec3 camPos, float partialTick) {
        LineRunner lineRunner = drawer -> collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, consumer) -> drawer.accept(consumer, pose.pose()));
        CableWorldRenderer.GeometryRunner quadRunner = drawer -> collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(CableWorldRenderer.WHITE_TEXTURE, false), (pose, consumer) -> drawer.accept(consumer, pose.pose()));
        renderOverlayCommon(poseStack, lineRunner, quadRunner, camPos, partialTick);
    }
    //?}

    public static void renderOverlayCommon(PoseStack poseStack, LineRunner lineRunner, CableWorldRenderer.GeometryRunner quadRunner, Vec3 camPos, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        NetCableItem heldCable = null;
        if (mainHand.getItem() instanceof NetCableItem c) {
            heldCable = c;
        } else if (offHand.getItem() instanceof NetCableItem c) {
            heldCable = c;
        }

        boolean isEmptyHand = mainHand.isEmpty();

        if (heldCable == null) {
            if (!isEmptyHand) {
                if (ClientCableSelection.isActive()) {
                    ClientCableSelection.reset();
                }
                return;
            }
            if (!ClientCableSelection.isActive() && !player.isShiftKeyDown()) {
                return;
            }
        }

        HitResult hit = mc.hitResult;
        BlockHitResult bHit = (hit instanceof BlockHitResult b && b.getType() != HitResult.Type.MISS) ? b : null;

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        if (!ClientCableSelection.isActive()) {
            // Case 1: Point A not selected yet - highlight hovered connector
            if (bHit != null) {
                BlockPos hitPos = bHit.getBlockPos();
                BlockEntity be = mc.level.getBlockEntity(hitPos);
                if (be == null && SableHelper.isAvailable()) {
                    be = CableWorldRenderer.findConnectorByPos(hitPos, SableHelper.getContainingUUID(mc.level, hitPos));
                }

                if (be instanceof NetCableConnectorBlockEntity connector || SimulatedReflect.isRopeAnchor(be)) {
                    boolean sneak = player.isShiftKeyDown();
                    if (sneak && be instanceof NetCableConnectorBlockEntity conn) {
                        Set<CableLink> links = conn.getLinks();
                        Vec3 fromOrigin = NetCableConnectorRenderer.cableOrigin(conn);
                        Vec3 fromLocal = Vec3.atLowerCornerOf(hitPos).add(fromOrigin);
                        Vec3 fromGlobal = SableHelper.isAvailable() ? SableHelper.projectOutFromBE(conn, fromLocal, partialTick) : fromLocal;

                        if (links.isEmpty()) {
                            renderBlockTargetOutline(mc.level, lineRunner, hitPos, bHit.getDirection(), conn, partialTick, 1.0f, 0.3f, 0.3f, 0.85f);
                            renderAttachmentPoint(lineRunner, fromGlobal, 1.0f, 0.3f, 0.3f);
                            PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.no_connections").withStyle(ChatFormatting.RED));
                        } else {
                            // Highlight all existing connected lines from this connector in red
                            for (CableLink link : links) {
                                Vec3 toGlobal = CableWorldRenderer.computeToGlobal(link, link.subLevel(), partialTick, mc.level);
                                if (toGlobal != null) {
                                    float linkDist = (float) fromGlobal.distanceTo(toGlobal);
                                    int linkMaxDist = conn.getMaxLinkDistance();
                                    NetCableConnectorBlockEntity partner = CableWorldRenderer.findConnectorByPos(link.pos(), link.subLevel());
                                    if (partner != null) linkMaxDist = Math.min(linkMaxDist, partner.getMaxLinkDistance());
                                    float linkHang = CableWorldRenderer.calculateHangAmount(linkDist, linkMaxDist);
                                    float linkWidth = conn.getCableWidth() * 1.3f * CableWorldRenderer.calculateTensionWidthFactor(linkDist, linkMaxDist);
                                    renderSaggingWireOutline(lineRunner, fromGlobal, toGlobal, linkWidth, 1.0f, 0.2f, 0.2f, 0.85f, linkHang);

                                    renderBlockTargetOutline(mc.level, lineRunner, link.pos(), null, partner, partialTick, 1.0f, 0.25f, 0.25f, 0.75f);
                                    renderAttachmentPoint(lineRunner, toGlobal, 1.0f, 0.25f, 0.25f);
                                }
                            }

                            renderBlockTargetOutline(mc.level, lineRunner, hitPos, bHit.getDirection(), conn, partialTick, 1.0f, 0.3f, 0.3f, 0.9f);
                            renderAttachmentPoint(lineRunner, fromGlobal, 1.0f, 0.3f, 0.3f);

                            if (links.size() == 1) {
                                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.disconnect_prompt").withStyle(ChatFormatting.RED));
                            } else {
                                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.disconnect_choose").withStyle(ChatFormatting.RED));
                            }
                        }
                    } else {
                        // Normal hover outline
                        Vec3 port;
                        if (be instanceof NetCableConnectorBlockEntity conn) {
                            Vec3 fromOrigin = NetCableConnectorRenderer.cableOrigin(conn);
                            Vec3 fromLocal = Vec3.atLowerCornerOf(hitPos).add(fromOrigin);
                            port = SableHelper.isAvailable() ? SableHelper.projectOutFromBE(conn, fromLocal, partialTick) : fromLocal;
                        } else {
                            Vec3 fromLocal = Vec3.atCenterOf(hitPos);
                            port = (be != null && SableHelper.isAvailable()) ? SableHelper.projectOutFromBE(be, fromLocal, partialTick) : fromLocal;
                        }
                        renderBlockTargetOutline(mc.level, lineRunner, hitPos, bHit.getDirection(), be, partialTick, 0.2f, 0.9f, 0.4f, 0.85f);
                        renderAttachmentPoint(lineRunner, port, 0.2f, 0.9f, 1.0f);
                    }
                }
            }
        } else {
            // Case 2: Point A is selected - live tension overlay
            BlockPos startPos = ClientCableSelection.getPos();
            BlockEntity startEntity = ClientCableSelection.getBlockEntity();
            if (startEntity == null || startEntity.isRemoved()) {
                startEntity = CableWorldRenderer.findConnectorByPos(startPos, ClientCableSelection.getSubLevel());
            }
            if (startEntity == null) {
                startEntity = mc.level.getBlockEntity(startPos);
            }

            if (startEntity == null) {
                ClientCableSelection.reset();
                poseStack.popPose();
                return;
            }

            boolean isDisconnect = ClientCableSelection.isDisconnect();

            Vec3 startPort;
            if (startEntity instanceof NetCableConnectorBlockEntity beA) {
                Vec3 startOrigin = NetCableConnectorRenderer.cableOrigin(beA);
                Vec3 startLocal = Vec3.atLowerCornerOf(startPos).add(startOrigin);
                startPort = SableHelper.isAvailable() ? SableHelper.projectOutFromBE(beA, startLocal, partialTick) : startLocal;
            } else {
                Vec3 startLocal = Vec3.atCenterOf(startPos);
                startPort = SableHelper.isAvailable() ? SableHelper.projectOutFromBE(startEntity, startLocal, partialTick) : startLocal;
            }

            // Highlight Point A prominently (wireframe cage + glowing cross marker)
            float startR = isDisconnect ? 1.0f : 0.1f;
            float startG = isDisconnect ? 0.2f : 0.7f;
            float startB = isDisconnect ? 0.2f : 1.0f;
            renderBlockTargetOutline(mc.level, lineRunner, startPos, null, startEntity, partialTick,
                    startR, startG, startB, 0.95f, true);
            renderAttachmentPoint(lineRunner, startPort, startR, startG, startB, true);

            // In disconnect mode, highlight all existing cables from Point A in glowing red
            if (isDisconnect && startEntity instanceof NetCableConnectorBlockEntity beA) {
                for (CableLink link : beA.getLinks()) {
                    Vec3 toGlobal = CableWorldRenderer.computeToGlobal(link, link.subLevel(), partialTick, mc.level);
                    if (toGlobal != null) {
                        float linkDist = (float) startPort.distanceTo(toGlobal);
                        int linkMaxDist = beA.getMaxLinkDistance();
                        NetCableConnectorBlockEntity partner = CableWorldRenderer.findConnectorByPos(link.pos(), link.subLevel());
                        if (partner != null) linkMaxDist = Math.min(linkMaxDist, partner.getMaxLinkDistance());
                        float linkHang = CableWorldRenderer.calculateHangAmount(linkDist, linkMaxDist);
                        float linkWidth = beA.getCableWidth() * 1.3f * CableWorldRenderer.calculateTensionWidthFactor(linkDist, linkMaxDist);
                        renderSaggingWireOutline(lineRunner, startPort, toGlobal, linkWidth, 1.0f, 0.2f, 0.2f, 0.85f, linkHang);

                        renderBlockTargetOutline(mc.level, lineRunner, link.pos(), null, partner, partialTick, 1.0f, 0.25f, 0.25f, 0.75f);
                        renderAttachmentPoint(lineRunner, toGlobal, 1.0f, 0.25f, 0.25f);
                    }
                }
            }

            Vec3 endPort;
            boolean isAimingConnector = false;
            boolean isAimingLinked = false;
            NetCableConnectorBlockEntity beB = null;

            if (bHit != null) {
                BlockPos hitPos = bHit.getBlockPos();
                BlockEntity hitBe = mc.level.getBlockEntity(hitPos);
                if (hitBe == null && SableHelper.isAvailable()) {
                    hitBe = CableWorldRenderer.findConnectorByPos(hitPos, SableHelper.getContainingUUID(mc.level, hitPos));
                }

                if ((hitBe instanceof NetCableConnectorBlockEntity || SimulatedReflect.isRopeAnchor(hitBe)) && !hitPos.equals(startPos)) {
                    isAimingConnector = true;
                    if (hitBe instanceof NetCableConnectorBlockEntity targetConnector) {
                        beB = targetConnector;
                        Vec3 toOrigin = NetCableConnectorRenderer.cableOrigin(beB);
                        Vec3 toLocal = Vec3.atLowerCornerOf(hitPos).add(toOrigin);
                        endPort = SableHelper.isAvailable() ? SableHelper.projectOutFromBE(beB, toLocal, partialTick) : toLocal;

                        if (startEntity instanceof NetCableConnectorBlockEntity beA) {
                            isAimingLinked = beA.isLinkedToPos(hitPos);
                        }
                    } else {
                        Vec3 toLocal = Vec3.atCenterOf(hitPos);
                        endPort = (hitBe != null && SableHelper.isAvailable()) ? SableHelper.projectOutFromBE(hitBe, toLocal, partialTick) : toLocal;
                    }

                    float tr = isDisconnect ? 1.0f : 0.2f;
                    float tg = isDisconnect ? 0.3f : 0.9f;
                    float tb = isDisconnect ? 0.3f : 0.4f;
                    renderBlockTargetOutline(mc.level, lineRunner, hitPos, bHit.getDirection(), hitBe, partialTick, tr, tg, tb, 0.85f);
                    renderAttachmentPoint(lineRunner, endPort, tr, tg, tb);
                } else {
                    Vec3 rawHitLoc = bHit.getLocation();
                    if (SableHelper.isAvailable() && SableHelper.isInSubLevel(mc.level, hitPos)) {
                        endPort = SableHelper.projectOutInterpolated(mc.level, rawHitLoc, partialTick);
                    } else {
                        endPort = rawHitLoc;
                    }
                }
            } else {
                double reach = player.blockInteractionRange();
                endPort = player.getEyePosition().add(player.getLookAngle().scale(reach));
            }

            double dist = startPort.distanceTo(endPort);
            int maxLen = 16;
            if (startEntity instanceof NetCableConnectorBlockEntity beA) {
                maxLen = beA.getMaxLinkDistance();
            }
            if (beB != null) {
                maxLen = Math.min(maxLen, beB.getMaxLinkDistance());
            }

            float tension = (float) (dist / maxLen);

            float r, g, b;
            if (isDisconnect) {
                r = 1.0f;
                g = isAimingLinked ? 0.2f : 0.45f;
                b = isAimingLinked ? 0.2f : 0.45f;
            } else if (dist > maxLen) {
                r = 1.0f;
                g = 0.2f;
                b = 0.2f;
            } else if (isAimingConnector && beB != null && beB.getLinks().size() >= beB.getMaxConnections() && !beB.isLinkedToPos(startPos)) {
                r = 1.0f;
                g = 0.2f;
                b = 0.2f;
            } else if (tension < 0.65f) {
                r = 0.2f;
                g = 0.95f;
                b = 0.35f;
            } else {
                float t = (tension - 0.65f) / 0.35f;
                r = 1.0f;
                g = Math.max(0.2f, 0.95f - t * 0.7f);
                b = 0.1f;
            }

            if (isDisconnect) {
                // In disconnect mode: render direct red laser line / strip connecting Point A to target
                float lr = 1.0f;
                float lg = isAimingLinked ? 0.15f : 0.45f;
                float lb = isAimingLinked ? 0.15f : 0.45f;
                renderDisconnectLaserLine(lineRunner, startPort, endPort, lr, lg, lb, 0.95f);
            } else {
                // Desaturate preview cable body with held cable dye color
                int cr, cg, cb;
                if (heldCable != null && heldCable.getColor() != null) {
                    int diffuse = heldCable.getColor().getTextureDiffuseColor();
                    int[] desat = CableWorldRenderer.adjustCableColor((diffuse >> 16) & 0xFF, (diffuse >> 8) & 0xFF, diffuse & 0xFF, 0.60f);
                    cr = (int) (desat[0] * (0.5f + 0.5f * r));
                    cg = (int) (desat[1] * (0.5f + 0.5f * g));
                    cb = (int) (desat[2] * (0.5f + 0.5f * b));
                } else {
                    cr = (int) (r * 255);
                    cg = (int) (g * 255);
                    cb = (int) (b * 255);
                }

                // Render live preview sagging square wire
                float hangAmount = CableWorldRenderer.calculateHangAmount((float) dist, maxLen);
                float cableWidth = ((startEntity instanceof NetCableConnectorBlockEntity beA) ? beA.getCableWidth() : 0.08f)
                        * CableWorldRenderer.calculateTensionWidthFactor((float) dist, maxLen);

                // 1. Wireframe outline along sagging curve
                renderSaggingWireOutline(lineRunner, startPort, endPort, cableWidth, r, g, b, 0.95f, hangAmount);

                // 2. Solid/textured square wire body along sagging curve
                CableWorldRenderer.renderSaggingSquareWire(poseStack, quadRunner, Vec3.ZERO, startPort, endPort,
                        cableWidth, cr, cg, cb, mc.level, hangAmount);
            }

            // 3. Actionbar tension / disconnect status display
            String distStr = String.format(java.util.Locale.ROOT, "%.1f", dist);
            String maxStr = String.format(java.util.Locale.ROOT, "%d", maxLen);
            int pct = (int) Math.round(tension * 100);

            if (isDisconnect) {
                if (isAimingConnector && beB != null) {
                    if (isAimingLinked) {
                        PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.disconnect_target").withStyle(ChatFormatting.RED));
                    } else {
                        PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.not_connected_here").withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.first_selected_disconnect", distStr, maxStr).withStyle(ChatFormatting.RED));
                }
            } else {
                ChatFormatting fmt = tension <= 0.7f ? ChatFormatting.GREEN : (tension <= 1.0f ? ChatFormatting.YELLOW : ChatFormatting.RED);
                PlayerDataHelper.sendActionMessage(player, Component.translatable("message.ccrj45.cable.tension", distStr, maxStr, pct).withStyle(fmt));
            }
        }

        poseStack.popPose();
    }

    //? if <=1.21.1 {
    /*public static void renderDisconnectLaserLine(PoseStack poseStack, MultiBufferSource bufferSource,
                                                 Vec3 start, Vec3 end,
                                                 float r, float g, float b, float a) {
        LineRunner lineRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.lines()), poseStack.last().pose());
        renderDisconnectLaserLine(lineRunner, start, end, r, g, b, a);
    }
    *///?}

    public static void renderDisconnectLaserLine(LineRunner lineRunner,
                                                 Vec3 start, Vec3 end,
                                                 float r, float g, float b, float a) {
        lineRunner.draw((consumer, matrix) -> {

        // Center primary laser beam
        vertexLine(consumer, matrix, (float) start.x, (float) start.y, (float) start.z, r, g, b, a, 0, 1, 0);
        vertexLine(consumer, matrix, (float) end.x, (float) end.y, (float) end.z, r, g, b, a, 0, 1, 0);

        // 3D glowing strip effect: surrounding cross offsets
        float offset = 0.02f;
        Vector3f dir = new Vector3f((float) (end.x - start.x), (float) (end.y - start.y), (float) (end.z - start.z));
        float len = dir.length();
        if (len < 1e-4f) return;
        dir.normalize();

        Vector3f up = new Vector3f(0, 1, 0);
        if (Math.abs(dir.y) > 0.95f) {
            up.set(1, 0, 0);
        }
        Vector3f side = new Vector3f(dir).cross(up).normalize().mul(offset);
        up = new Vector3f(side).cross(dir).normalize().mul(offset);

        // 4 surrounding glowing beam lines
        vertexLine(consumer, matrix, (float) start.x + side.x, (float) start.y + side.y, (float) start.z + side.z, r, g, b, a * 0.75f, 0, 1, 0);
        vertexLine(consumer, matrix, (float) end.x + side.x, (float) end.y + side.y, (float) end.z + side.z, r, g, b, a * 0.75f, 0, 1, 0);

        vertexLine(consumer, matrix, (float) start.x - side.x, (float) start.y - side.y, (float) start.z - side.z, r, g, b, a * 0.75f, 0, 1, 0);
        vertexLine(consumer, matrix, (float) end.x - side.x, (float) end.y - side.y, (float) end.z - side.z, r, g, b, a * 0.75f, 0, 1, 0);

        vertexLine(consumer, matrix, (float) start.x + up.x, (float) start.y + up.y, (float) start.z + up.z, r, g, b, a * 0.75f, 0, 1, 0);
        vertexLine(consumer, matrix, (float) end.x + up.x, (float) end.y + up.y, (float) end.z + up.z, r, g, b, a * 0.75f, 0, 1, 0);

        vertexLine(consumer, matrix, (float) start.x - up.x, (float) start.y - up.y, (float) start.z - up.z, r, g, b, a * 0.75f, 0, 1, 0);
        vertexLine(consumer, matrix, (float) end.x - up.x, (float) end.y - up.y, (float) end.z - up.z, r, g, b, a * 0.75f, 0, 1, 0);
        });
    }

    //? if <=1.21.1 {
    /*public static void renderSaggingWireOutline(PoseStack poseStack, MultiBufferSource bufferSource,
                                                Vec3 start, Vec3 end, float width,
                                                float r, float g, float b, float a, float hangAmount) {
        LineRunner lineRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.lines()), poseStack.last().pose());
        renderSaggingWireOutline(lineRunner, start, end, width, r, g, b, a, hangAmount);
    }
    *///?}

    public static void renderSaggingWireOutline(LineRunner lineRunner,
                                                Vec3 start, Vec3 end, float width,
                                                float r, float g, float b, float a, float hangAmount) {
        float dist = (float) start.distanceTo(end);
        if (dist < 1.0e-5f) return;

        int segments = Math.max(8, Math.min(24, (int) Math.ceil(dist * 1.5f)));
        Vec3[] centerPoints = new Vec3[segments + 1];
        for (int i = 0; i <= segments; i++) {
            float t = (float) i / segments;
            centerPoints[i] = CableWorldRenderer.curvePoint(start, end, t, hangAmount);
        }

        Vector3f[] pTL = new Vector3f[segments + 1];
        Vector3f[] pTR = new Vector3f[segments + 1];
        Vector3f[] pBR = new Vector3f[segments + 1];
        Vector3f[] pBL = new Vector3f[segments + 1];
        float hw = width * 0.5f;

        Vec3 delta = end.subtract(start);
        Vector3f upRef = Math.abs(delta.y) / (dist + 1.0e-6f) > 0.88f ? new Vector3f(0, 0, 1) : new Vector3f(0, 1, 0);

        for (int i = 0; i <= segments; i++) {
            Vec3 p = centerPoints[i];
            Vec3 tangentVec;
            if (i < segments) {
                tangentVec = centerPoints[i + 1].subtract(p);
            } else {
                tangentVec = p.subtract(centerPoints[i - 1]);
            }
            Vector3f D = new Vector3f((float) tangentVec.x, (float) tangentVec.y, (float) tangentVec.z);
            if (D.lengthSquared() > 1.0e-6f) D.normalize();
            else D = new Vector3f(1, 0, 0);

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

            float sliceScale = (i % 2 == 1) ? 0.90f : 1.0f;
            float currentHw = hw * sliceScale;

            Vector3f vT = new Vector3f(T).mul(currentHw);
            Vector3f vN = new Vector3f(N).mul(currentHw);
            Vector3f c = new Vector3f((float) p.x, (float) p.y, (float) p.z);

            pTL[i] = new Vector3f(c).sub(vT).add(vN);
            pTR[i] = new Vector3f(c).add(vT).add(vN);
            pBR[i] = new Vector3f(c).add(vT).sub(vN);
            pBL[i] = new Vector3f(c).sub(vT).sub(vN);
        }

        lineRunner.draw((consumer, matrix) -> {
            // Start ring
            drawLine(consumer, matrix, pTL[0], pTR[0], r, g, b, a);
            drawLine(consumer, matrix, pTR[0], pBR[0], r, g, b, a);
            drawLine(consumer, matrix, pBR[0], pBL[0], r, g, b, a);
            drawLine(consumer, matrix, pBL[0], pTL[0], r, g, b, a);

            // End ring
            drawLine(consumer, matrix, pTL[segments], pTR[segments], r, g, b, a);
            drawLine(consumer, matrix, pTR[segments], pBR[segments], r, g, b, a);
            drawLine(consumer, matrix, pBR[segments], pBL[segments], r, g, b, a);
            drawLine(consumer, matrix, pBL[segments], pTL[segments], r, g, b, a);

            // Along edges
            for (int i = 0; i < segments; i++) {
                float segDim = (i % 2 == 1) ? 0.75f : 1.0f;
                float sr = r * segDim;
                float sg = g * segDim;
                float sb = b * segDim;
                drawLine(consumer, matrix, pTL[i], pTL[i + 1], sr, sg, sb, a);
                drawLine(consumer, matrix, pTR[i], pTR[i + 1], sr, sg, sb, a);
                drawLine(consumer, matrix, pBR[i], pBR[i + 1], sr, sg, sb, a);
                drawLine(consumer, matrix, pBL[i], pBL[i + 1], sr, sg, sb, a);
            }
        });
    }

    //? if <=1.21.1 {
    /*private static void renderAttachmentPoint(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 pos, float r, float g, float b) {
        renderAttachmentPoint(poseStack, bufferSource, pos, r, g, b, false);
    }

    private static void renderAttachmentPoint(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 pos, float r, float g, float b, boolean isSelectedNode) {
        LineRunner lineRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.lines()), poseStack.last().pose());
        renderAttachmentPoint(lineRunner, pos, r, g, b, isSelectedNode);
    }
    *///?}

    private static void renderAttachmentPoint(LineRunner lineRunner, Vec3 pos, float r, float g, float b) {
        renderAttachmentPoint(lineRunner, pos, r, g, b, false);
    }

    private static void renderAttachmentPoint(LineRunner lineRunner, Vec3 pos, float r, float g, float b, boolean isSelectedNode) {
        lineRunner.draw((consumer, matrix) -> {
            double s = isSelectedNode ? 0.055 : 0.035;
            renderBoxLines(consumer, matrix,
                    pos.x - s, pos.y - s, pos.z - s,
                    pos.x + s, pos.y + s, pos.z + s,
                    r, g, b, 0.95f);
            if (isSelectedNode) {
                drawLine(consumer, matrix, new Vector3f((float) (pos.x - s), (float) (pos.y - s), (float) (pos.z - s)),
                        new Vector3f((float) (pos.x + s), (float) (pos.y + s), (float) (pos.z + s)), r, g, b, 0.95f);
                drawLine(consumer, matrix, new Vector3f((float) (pos.x - s), (float) (pos.y + s), (float) (pos.z - s)),
                        new Vector3f((float) (pos.x + s), (float) (pos.y - s), (float) (pos.z + s)), r, g, b, 0.95f);
                drawLine(consumer, matrix, new Vector3f((float) (pos.x + s), (float) (pos.y - s), (float) (pos.z - s)),
                        new Vector3f((float) (pos.x - s), (float) (pos.y + s), (float) (pos.z + s)), r, g, b, 0.95f);
                drawLine(consumer, matrix, new Vector3f((float) (pos.x - s), (float) (pos.y - s), (float) (pos.z + s)),
                        new Vector3f((float) (pos.x + s), (float) (pos.y + s), (float) (pos.z - s)), r, g, b, 0.95f);
            }
        });
    }

    //? if <=1.21.1 {
    /*public static void renderBlockTargetOutline(Level level, PoseStack poseStack, MultiBufferSource bufferSource,
                                                BlockPos pos, Direction face, BlockEntity be, float partialTick,
                                                float r, float g, float b, float a) {
        renderBlockTargetOutline(level, poseStack, bufferSource, pos, face, be, partialTick, r, g, b, a, false);
    }

    public static void renderBlockTargetOutline(Level level, PoseStack poseStack, MultiBufferSource bufferSource,
                                                BlockPos pos, Direction face, BlockEntity be, float partialTick,
                                                float r, float g, float b, float a, boolean outlineAllFaces) {
        LineRunner lineRunner = drawer -> drawer.accept(bufferSource.getBuffer(RenderType.lines()), poseStack.last().pose());
        renderBlockTargetOutline(level, lineRunner, pos, face, be, partialTick, r, g, b, a, outlineAllFaces);
    }
    *///?}

    public static void renderBlockTargetOutline(Level level, LineRunner lineRunner,
                                                BlockPos pos, Direction face, BlockEntity be, float partialTick,
                                                float r, float g, float b, float a) {
        renderBlockTargetOutline(level, lineRunner, pos, face, be, partialTick, r, g, b, a, false);
    }

    public static void renderBlockTargetOutline(Level level, LineRunner lineRunner,
                                                BlockPos pos, Direction face, BlockEntity be, float partialTick,
                                                float r, float g, float b, float a, boolean outlineAllFaces) {
        if (level == null || pos == null) return;
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        VoxelShape shape = state.getShape(level, pos);
        if (shape.isEmpty()) return;

        double bx = pos.getX();
        double by = pos.getY();
        double bz = pos.getZ();

        boolean hasSub = SableHelper.isAvailable() && (be != null || SableHelper.isInSubLevel(level, pos));

        double ex = 0.003;
        lineRunner.draw((consumer, matrix) -> {
            for (AABB box : shape.toAabbs()) {
                renderTransformedBoxLines(consumer, matrix, level, be, partialTick, hasSub,
                        box.minX + bx - ex, box.minY + by - ex, box.minZ + bz - ex,
                        box.maxX + bx + ex, box.maxY + by + ex, box.maxZ + bz + ex,
                        r, g, b, a);
            }

            if (outlineAllFaces) {
                for (Direction d : Direction.values()) {
                    for (AABB box : shape.toAabbs()) {
                        renderTransformedFaceLines(consumer, matrix, level, be, partialTick, hasSub,
                                box.minX + bx, box.minY + by, box.minZ + bz,
                                box.maxX + bx, box.maxY + by, box.maxZ + bz,
                                d, r, g, b, a);
                    }
                }
            } else if (face != null) {
                for (AABB box : shape.toAabbs()) {
                    renderTransformedFaceLines(consumer, matrix, level, be, partialTick, hasSub,
                            box.minX + bx, box.minY + by, box.minZ + bz,
                            box.maxX + bx, box.maxY + by, box.maxZ + bz,
                            face, r, g, b, a);
                }
            }
        });
    }

    private static Vector3f transformVertex(Level level, BlockEntity be, float partialTick, boolean hasSub, double x, double y, double z) {
        if (hasSub) {
            Vec3 local = new Vec3(x, y, z);
            Vec3 global = be != null
                    ? SableHelper.projectOutFromBE(be, local, partialTick)
                    : SableHelper.projectOutInterpolated(level, local, partialTick);
            return new Vector3f((float) global.x, (float) global.y, (float) global.z);
        }
        return new Vector3f((float) x, (float) y, (float) z);
    }

    private static void renderTransformedFaceLines(VertexConsumer consumer, Matrix4f matrix,
                                                   Level level, BlockEntity be, float partialTick, boolean hasSub,
                                                   double minX, double minY, double minZ,
                                                   double maxX, double maxY, double maxZ,
                                                   Direction face, float r, float g, float b, float a) {
        float bias = 0.002f;
        Vector3f p1, p2, p3, p4;
        switch (face) {
            case UP -> {
                double y = maxY + bias;
                p1 = transformVertex(level, be, partialTick, hasSub, minX, y, minZ);
                p2 = transformVertex(level, be, partialTick, hasSub, maxX, y, minZ);
                p3 = transformVertex(level, be, partialTick, hasSub, maxX, y, maxZ);
                p4 = transformVertex(level, be, partialTick, hasSub, minX, y, maxZ);
            }
            case DOWN -> {
                double y = minY - bias;
                p1 = transformVertex(level, be, partialTick, hasSub, minX, y, minZ);
                p2 = transformVertex(level, be, partialTick, hasSub, maxX, y, minZ);
                p3 = transformVertex(level, be, partialTick, hasSub, maxX, y, maxZ);
                p4 = transformVertex(level, be, partialTick, hasSub, minX, y, maxZ);
            }
            case NORTH -> {
                double z = minZ - bias;
                p1 = transformVertex(level, be, partialTick, hasSub, minX, minY, z);
                p2 = transformVertex(level, be, partialTick, hasSub, maxX, minY, z);
                p3 = transformVertex(level, be, partialTick, hasSub, maxX, maxY, z);
                p4 = transformVertex(level, be, partialTick, hasSub, minX, maxY, z);
            }
            case SOUTH -> {
                double z = maxZ + bias;
                p1 = transformVertex(level, be, partialTick, hasSub, minX, minY, z);
                p2 = transformVertex(level, be, partialTick, hasSub, maxX, minY, z);
                p3 = transformVertex(level, be, partialTick, hasSub, maxX, maxY, z);
                p4 = transformVertex(level, be, partialTick, hasSub, minX, maxY, z);
            }
            case WEST -> {
                double x = minX - bias;
                p1 = transformVertex(level, be, partialTick, hasSub, x, minY, minZ);
                p2 = transformVertex(level, be, partialTick, hasSub, x, minY, maxZ);
                p3 = transformVertex(level, be, partialTick, hasSub, x, maxY, maxZ);
                p4 = transformVertex(level, be, partialTick, hasSub, x, maxY, minZ);
            }
            case EAST -> {
                double x = maxX + bias;
                p1 = transformVertex(level, be, partialTick, hasSub, x, minY, minZ);
                p2 = transformVertex(level, be, partialTick, hasSub, x, minY, maxZ);
                p3 = transformVertex(level, be, partialTick, hasSub, x, maxY, maxZ);
                p4 = transformVertex(level, be, partialTick, hasSub, x, maxY, minZ);
            }
            default -> {
                return;
            }
        }
        drawLine(consumer, matrix, p1, p2, r, g, b, a);
        drawLine(consumer, matrix, p2, p3, r, g, b, a);
        drawLine(consumer, matrix, p3, p4, r, g, b, a);
        drawLine(consumer, matrix, p4, p1, r, g, b, a);
    }

    private static void renderTransformedBoxLines(VertexConsumer consumer, Matrix4f matrix,
                                                  Level level, BlockEntity be, float partialTick, boolean hasSub,
                                                  double minX, double minY, double minZ,
                                                  double maxX, double maxY, double maxZ,
                                                  float r, float g, float b, float a) {
        Vector3f c000 = transformVertex(level, be, partialTick, hasSub, minX, minY, minZ);
        Vector3f c001 = transformVertex(level, be, partialTick, hasSub, minX, minY, maxZ);
        Vector3f c010 = transformVertex(level, be, partialTick, hasSub, minX, maxY, minZ);
        Vector3f c011 = transformVertex(level, be, partialTick, hasSub, minX, maxY, maxZ);
        Vector3f c100 = transformVertex(level, be, partialTick, hasSub, maxX, minY, minZ);
        Vector3f c101 = transformVertex(level, be, partialTick, hasSub, maxX, minY, maxZ);
        Vector3f c110 = transformVertex(level, be, partialTick, hasSub, maxX, maxY, minZ);
        Vector3f c111 = transformVertex(level, be, partialTick, hasSub, maxX, maxY, maxZ);

        // Bottom 4
        drawLine(consumer, matrix, c000, c100, r, g, b, a);
        drawLine(consumer, matrix, c100, c101, r, g, b, a);
        drawLine(consumer, matrix, c101, c001, r, g, b, a);
        drawLine(consumer, matrix, c001, c000, r, g, b, a);

        // Top 4
        drawLine(consumer, matrix, c010, c110, r, g, b, a);
        drawLine(consumer, matrix, c110, c111, r, g, b, a);
        drawLine(consumer, matrix, c111, c011, r, g, b, a);
        drawLine(consumer, matrix, c011, c010, r, g, b, a);

        // Vertical 4
        drawLine(consumer, matrix, c000, c010, r, g, b, a);
        drawLine(consumer, matrix, c100, c110, r, g, b, a);
        drawLine(consumer, matrix, c101, c111, r, g, b, a);
        drawLine(consumer, matrix, c001, c011, r, g, b, a);
    }

    private static void renderBoxLines(VertexConsumer consumer, Matrix4f matrix,
                                       double minX, double minY, double minZ,
                                       double maxX, double maxY, double maxZ,
                                       float r, float g, float b, float a) {
        float x1 = (float) minX, y1 = (float) minY, z1 = (float) minZ;
        float x2 = (float) maxX, y2 = (float) maxY, z2 = (float) maxZ;

        // Bottom 4
        drawLine(consumer, matrix, new Vector3f(x1, y1, z1), new Vector3f(x2, y1, z1), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y1, z1), new Vector3f(x2, y1, z2), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y1, z2), new Vector3f(x1, y1, z2), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x1, y1, z2), new Vector3f(x1, y1, z1), r, g, b, a);

        // Top 4
        drawLine(consumer, matrix, new Vector3f(x1, y2, z1), new Vector3f(x2, y2, z1), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y2, z1), new Vector3f(x2, y2, z2), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y2, z2), new Vector3f(x1, y2, z2), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x1, y2, z2), new Vector3f(x1, y2, z1), r, g, b, a);

        // Vertical 4
        drawLine(consumer, matrix, new Vector3f(x1, y1, z1), new Vector3f(x1, y2, z1), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y1, z1), new Vector3f(x2, y2, z1), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x2, y1, z2), new Vector3f(x2, y2, z2), r, g, b, a);
        drawLine(consumer, matrix, new Vector3f(x1, y1, z2), new Vector3f(x1, y2, z2), r, g, b, a);
    }

    private static void drawLine(VertexConsumer consumer, Matrix4f matrix, Vector3f p1, Vector3f p2, float r, float g, float b, float a) {
        Vector3f normal = new Vector3f(p2).sub(p1);
        if (normal.lengthSquared() > 1e-6f) {
            normal.normalize();
        } else {
            normal.set(0, 1, 0);
        }
        vertexLine(consumer, matrix, p1.x(), p1.y(), p1.z(), r, g, b, a, normal.x(), normal.y(), normal.z());
        vertexLine(consumer, matrix, p2.x(), p2.y(), p2.z(), r, g, b, a, normal.x(), normal.y(), normal.z());
    }

    private static void vertexLine(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float r, float g, float b, float a, float nx, float ny, float nz) {
        //? if <=1.21.1 {
        /*consumer.addVertex(matrix, x, y, z).setColor(r, g, b, a).setNormal(nx, ny, nz);
        *///?} else {
        consumer.addVertex(matrix, x, y, z).setColor(r, g, b, a).setNormal(nx, ny, nz).setLineWidth(1.0f);
        //?}
    }
}
