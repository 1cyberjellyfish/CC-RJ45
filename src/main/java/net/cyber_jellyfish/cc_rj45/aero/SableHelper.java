package net.cyber_jellyfish.cc_rj45.aero;

import java.util.UUID;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorHugeBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableHubBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;

public class SableHelper {
    private static final String BRIDGE_IMPL = "net.cyber_jellyfish.cc_rj45.aero.SableBridgeImpl";
    private static final SableBridge NOOP = new SableBridge() {
        @Override public boolean isAvailable() { return false; }
        @Override public @Nullable UUID getContainingUUID(Level level, BlockPos pos) { return null; }
        @Override public @Nullable UUID getContainingUUID(BlockEntity be) { return null; }
        @Override public Vec3 projectOut(Level level, Vec3 localPos) { return localPos; }
        @Override public Vec3 projectOutInterpolated(Level level, Vec3 localPos, float partialTick) { return localPos; }
        @Override public Vec3 getGlobalPos(Level level, BlockPos pos) { return Vec3.atCenterOf(pos); }
        //? if <=1.21.1 {
        /*@Override public @Nullable BlockEntity findBlockEntity(ServerLevel rootLevel, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos) {
            if (subLevelId != null) return null;
            ResourceKey<Level> dk = ResourceKey.create(Registries.DIMENSION, dimension);
            ServerLevel tl = rootLevel.getServer().getLevel(dk);
            return (tl != null && CCRJ45.isChunkLoadedNow(tl, pos)) ? tl.getBlockEntity(pos) : null;
        }
        @Override public Vec3 getLinkGlobalPos(MinecraftServer server, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos) {
            return Vec3.atCenterOf(pos);
        }
        *///?} else {
        @Override public @Nullable BlockEntity findBlockEntity(ServerLevel rootLevel, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos) {
            if (subLevelId != null) return null;
            ResourceKey<Level> dk = ResourceKey.create(Registries.DIMENSION, dimension);
            ServerLevel tl = rootLevel.getServer().getLevel(dk);
            return (tl != null && CCRJ45.isChunkLoadedNow(tl, pos)) ? tl.getBlockEntity(pos) : null;
        }
        @Override public Vec3 getLinkGlobalPos(MinecraftServer server, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos) {
            return Vec3.atCenterOf(pos);
        }
        //?}
        @Override public boolean isInSubLevel(Level level, BlockPos pos) { return false; }
        @Override public @Nullable Level getSubLevelLevel(Level anyLevel, UUID subUUID) { return null; }
        @Override public Vec3 projectOutAt(Level rootLevel, UUID subUUID, Vec3 localPos, float partialTick) { return localPos; }
        @Override public boolean isRootLevel(Level level) { return true; }
        @Override public Vec3 projectOutFromBE(BlockEntity be, Vec3 localPos, float partialTick) { return localPos; }
        @Override public void onConnectorBeforeMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {}
        @Override public void onConnectorAfterMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {}
        @Override public boolean isMigrating(BlockPos pos) { return false; }
    };
    private static SableBridge bridge = NOOP;
    private static Boolean available;

    private static SableBridge getBridge() {
        isAvailable();
        return bridge;
    }

    public static boolean isAvailable() {
        if (available != null) return available;
        //? if <=1.21.1 && neoforge {
        /*try {
            net.neoforged.fml.ModList modList = net.neoforged.fml.ModList.get();
            if (modList != null && !modList.isLoaded("sable")) {
                available = false;
                CCRJ45.LOGGER.debug("Sable mod not loaded");
                return false;
            }
            Class<?> implClass = Class.forName(BRIDGE_IMPL);
            bridge = (SableBridge) implClass.getDeclaredConstructor().newInstance();
            available = true;
            CCRJ45.LOGGER.info("Sable mod detected, sublevel cable linking enabled");
        } catch (Throwable t) {
            available = false;
            CCRJ45.LOGGER.debug("Sable mod not available: {}", t.getMessage());
        }
        *///?} else {
        available = false;
        //?}
        return available;
    }

    public static NetCableConnectorBlock createConnectorBlock(BlockBehaviour.Properties properties) {
        return getBridge().createConnectorBlock(properties);
    }

    public static NetCableHubBlock createHubBlock(BlockBehaviour.Properties properties) {
        return getBridge().createHubBlock(properties);
    }

    public static NetCableConnectorHugeBlock createHugeConnectorBlock(BlockBehaviour.Properties properties) {
        return getBridge().createHugeConnectorBlock(properties);
    }

    @Nullable
    public static UUID getContainingUUID(Level level, BlockPos pos) {
        return getBridge().getContainingUUID(level, pos);
    }

    @Nullable
    public static UUID getContainingUUID(BlockEntity be) {
        return getBridge().getContainingUUID(be);
    }

    public static Vec3 projectOut(Level level, Vec3 localPos) {
        return getBridge().projectOut(level, localPos);
    }

    public static Vec3 projectOutInterpolated(Level level, Vec3 localPos, float partialTick) {
        return getBridge().projectOutInterpolated(level, localPos, partialTick);
    }

    public static Vec3 getGlobalPos(Level level, BlockPos pos) {
        return getBridge().getGlobalPos(level, pos);
    }

    //? if <=1.21.1 {
    /*@Nullable
    public static BlockEntity findBlockEntity(ServerLevel rootLevel, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos) {
        return getBridge().findBlockEntity(rootLevel, dimension, subLevelId, pos);
    }

    public static Vec3 getLinkGlobalPos(MinecraftServer server, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos) {
        return getBridge().getLinkGlobalPos(server, dimension, subLevelId, pos);
    }
    *///?} else {
    @Nullable
    public static BlockEntity findBlockEntity(ServerLevel rootLevel, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos) {
        return getBridge().findBlockEntity(rootLevel, dimension, subLevelId, pos);
    }

    public static Vec3 getLinkGlobalPos(MinecraftServer server, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos) {
        return getBridge().getLinkGlobalPos(server, dimension, subLevelId, pos);
    }
    //?}

    public static boolean isInSubLevel(Level level, BlockPos pos) {
        return getBridge().isInSubLevel(level, pos);
    }

    @Nullable
    public static Level getSubLevelLevel(Level anyLevel, UUID subUUID) {
        return getBridge().getSubLevelLevel(anyLevel, subUUID);
    }

    public static Vec3 projectOutAt(Level rootLevel, UUID subUUID, Vec3 localPos, float partialTick) {
        return getBridge().projectOutAt(rootLevel, subUUID, localPos, partialTick);
    }

    public static boolean isRootLevel(Level level) {
        return getBridge().isRootLevel(level);
    }

    public static Vec3 projectOutFromBE(BlockEntity be, Vec3 localPos, float partialTick) {
        return getBridge().projectOutFromBE(be, localPos, partialTick);
    }

    public static void onConnectorBeforeMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {
        getBridge().onConnectorBeforeMove(sourceLevel, targetLevel, sourcePos, targetPos);
    }

    public static void onConnectorAfterMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {
        getBridge().onConnectorAfterMove(sourceLevel, targetLevel, sourcePos, targetPos);
    }

    public static boolean isMigrating(BlockPos pos) {
        return getBridge().isMigrating(pos);
    }
}
