package net.cyber_jellyfish.cc_rj45.client;

//? if <=1.21.1 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;

public class SublevelCableRenderer {
    public static void render(PoseStack poseStack, MultiBufferSource bufferSource, Camera camera, float partialTick) {
        // Delegated to unified CableWorldRenderer which handles all cables (sublevel and normal world)
        CableWorldRenderer.render(poseStack, bufferSource, camera, null, partialTick);
    }
}
*///?} else {
public class SublevelCableRenderer {
    public static void render(Object... args) {}
}
//?}
