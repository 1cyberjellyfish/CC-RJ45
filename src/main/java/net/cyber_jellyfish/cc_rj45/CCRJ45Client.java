//? if neoforge {
/*package net.cyber_jellyfish.cc_rj45;

import net.cyber_jellyfish.cc_rj45.aero.SimulatedReflect;
import net.cyber_jellyfish.cc_rj45.client.CableWorldRenderer;
import net.cyber_jellyfish.cc_rj45.client.RopeBridgeRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
//? if <=1.21.1 {
/^import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
^///?} else {
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
//?}
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CCRJ45.MODID, dist = Dist.CLIENT)
public class CCRJ45Client {
    public CCRJ45Client(ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
    }

    //? if <=1.21.1 {
    /^@SubscribeEvent
    public void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;

        CableWorldRenderer.render(
                event.getPoseStack(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                event.getCamera(),
                event.getFrustum(),
                event.getPartialTick().getGameTimeDeltaPartialTick(true)
        );

        if (SimulatedReflect.isAvailable()) {
            RopeBridgeRenderer.render(
                    event.getPoseStack(),
                    Minecraft.getInstance().renderBuffers().bufferSource(),
                    event.getCamera(),
                    event.getPartialTick().getGameTimeDeltaPartialTick(true)
            );
        }
    }
    ^///?} else {
    @SubscribeEvent
    public void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        var cameraState = event.getLevelRenderState().cameraRenderState;
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        CableWorldRenderer.render(
                event.getPoseStack(),
                event.getSubmitNodeCollector(),
                cameraState.pos,
                cameraState.cullFrustum,
                partialTick
        );
    }
    //?}
}
*///?}
