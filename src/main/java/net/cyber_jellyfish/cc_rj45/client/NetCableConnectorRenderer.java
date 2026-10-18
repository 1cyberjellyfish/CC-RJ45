package net.cyber_jellyfish.cc_rj45.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

//? if <=1.21.1 {
/*import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class NetCableConnectorRenderer<T extends NetCableConnectorBlockEntity> implements BlockEntityRenderer<T> {

    public NetCableConnectorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // All cables are rendered globally and smoothly in CableWorldRenderer during RenderLevelStageEvent
        // with pure client frustum culling on the wire AABB itself, avoiding chunk-culling pop-in and jitter.
    }

    public static Vec3 cableOrigin(NetCableConnectorBlockEntity be) {
        return be.getCablePortOrigin();
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return false;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
*///?} else {
public class NetCableConnectorRenderer {
    public static Vec3 cableOrigin(NetCableConnectorBlockEntity be) {
        return be.getCablePortOrigin();
    }
}
//?}
