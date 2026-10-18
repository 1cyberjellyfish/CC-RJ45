//? if fabric {
package net.cyber_jellyfish.cc_rj45.fabric;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.ModDataComponents;
import net.cyber_jellyfish.cc_rj45.aero.BridgeSyncPayload;
import net.cyber_jellyfish.cc_rj45.aero.ClientBridgeState;
import net.cyber_jellyfish.cc_rj45.client.CableTensionOverlayRenderer;
import net.cyber_jellyfish.cc_rj45.client.CableWorldRenderer;
import net.cyber_jellyfish.cc_rj45.client.NetCableConnectorRenderer;
import net.cyber_jellyfish.cc_rj45.client.RopeBridgeRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? if <=1.21.1 {
/*import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.renderer.MultiBufferSource;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
//?}
//? if <=1.21.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
*///?}
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;

public class CCRJ45FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if <=1.21.1 {
        /*BlockEntityRenderers.register(CCRJ45.RAW_NET_CABLE_CONNECTOR_BE, NetCableConnectorRenderer::new);
        BlockEntityRenderers.register(CCRJ45.RAW_NET_CABLE_HUB_BE, NetCableConnectorRenderer::new);
        BlockEntityRenderers.register(CCRJ45.RAW_NET_CABLE_CONNECTOR_HUGE_BE, NetCableConnectorRenderer::new);

        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> {
                    if (tintIndex == 1) {
                        var color = stack.get(ModDataComponents.DISK_COLOR.get());
                        return color != null ? 0xFF000000 | color.color() : -1;
                    }
                    return -1;
                },
                CCRJ45.RAW_PORTABLE_DRIVE
        );
        *///?}

        ClientPlayNetworking.registerGlobalReceiver(BridgeSyncPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> ClientBridgeState.update(payload.links()));
        });

        //? if <=1.21.1 {
        /*WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            float partialTick = context.tickCounter().getGameTimeDeltaPartialTick(true);
            CableWorldRenderer.render(context.matrixStack(), context.consumers(), context.camera(), context.frustum(), partialTick);
            RopeBridgeRenderer.render(context.matrixStack(), context.consumers(), context.camera(), partialTick);
            if (context.consumers() instanceof MultiBufferSource.BufferSource bs) {
                CableTensionOverlayRenderer.renderOverlay(context.matrixStack(), bs, context.camera(), partialTick);
            }
        });
        *///?} else {
        LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
            var cameraState = context.levelState().cameraRenderState;
            float partialTick = net.minecraft.client.Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            CableWorldRenderer.render(context.poseStack(), context.submitNodeCollector(), cameraState.pos, cameraState.cullFrustum, partialTick);
            CableTensionOverlayRenderer.renderOverlay(context.poseStack(), context.submitNodeCollector(), cameraState.pos, partialTick);
        });
        //?}

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide()) {
                CableTensionOverlayRenderer.handleClientRightClickBlock(player, world, hand, hitResult.getBlockPos());
            }
            return InteractionResult.PASS;
        });
    }
}
//?}
