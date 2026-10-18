package net.cyber_jellyfish.cc_rj45.client;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClientCableSelection {
    private static boolean active = false;
    private static BlockPos pos = null;
    @Nullable
    private static BlockEntity blockEntity = null;
    @Nullable
    private static java.util.UUID subLevel = null;
    private static boolean isDisconnect = false;
    @Nullable
    private static DyeColor color = null;
    private static boolean isRopeAnchor = false;

    public static boolean isActive() {
        return active;
    }

    public static BlockPos getPos() {
        return pos;
    }

    @Nullable
    public static BlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Nullable
    public static java.util.UUID getSubLevel() {
        return subLevel;
    }

    public static boolean isDisconnect() {
        return isDisconnect;
    }

    @Nullable
    public static DyeColor getColor() {
        return color;
    }

    public static boolean isRopeAnchor() {
        return isRopeAnchor;
    }

    public static void setFirst(BlockPos clickedPos, BlockEntity entity, boolean disconnect, @Nullable DyeColor dyeColor, boolean ropeAnchor) {
        active = true;
        pos = clickedPos;
        blockEntity = entity;
        subLevel = net.cyber_jellyfish.cc_rj45.aero.SableHelper.getContainingUUID(entity);
        isDisconnect = disconnect;
        color = dyeColor;
        isRopeAnchor = ropeAnchor;
    }

    public static void reset() {
        reset(true);
    }

    public static void reset(boolean notifyServer) {
        boolean wasActive = active;
        active = false;
        pos = null;
        blockEntity = null;
        subLevel = null;
        isDisconnect = false;
        color = null;
        isRopeAnchor = false;
        if (notifyServer && wasActive) {
            ClientSender.sendClear();
        }
    }

    private static class ClientSender {
        static void sendClear() {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.getConnection() != null) {
                net.cyber_jellyfish.cc_rj45.network.NetworkHelper.sendToServer(
                        new net.cyber_jellyfish.cc_rj45.network.ClearCableSelectionPayload());
            }
        }
    }
}
