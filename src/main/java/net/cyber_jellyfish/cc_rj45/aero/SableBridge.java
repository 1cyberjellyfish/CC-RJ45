package net.cyber_jellyfish.cc_rj45.aero;

import java.util.UUID;

import javax.annotation.Nullable;

import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorHugeBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableHubBlock;
import net.minecraft.core.BlockPos;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;

public interface SableBridge {

    default NetCableConnectorBlock createConnectorBlock(BlockBehaviour.Properties properties) {
        return new NetCableConnectorBlock(properties);
    }

    default NetCableHubBlock createHubBlock(BlockBehaviour.Properties properties) {
        return new NetCableHubBlock(properties);
    }

    default NetCableConnectorHugeBlock createHugeConnectorBlock(BlockBehaviour.Properties properties) {
        return new NetCableConnectorHugeBlock(properties);
    }

    boolean isAvailable();

    @Nullable
    UUID getContainingUUID(Level level, BlockPos pos);

    @Nullable
    UUID getContainingUUID(BlockEntity be);

    Vec3 projectOut(Level level, Vec3 localPos);

    Vec3 projectOutInterpolated(Level level, Vec3 localPos, float partialTick);

    Vec3 getGlobalPos(Level level, BlockPos pos);

    //? if <=1.21.1 {
    /*@Nullable
    BlockEntity findBlockEntity(ServerLevel rootLevel, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos);

    Vec3 getLinkGlobalPos(MinecraftServer server, ResourceLocation dimension, @Nullable UUID subLevelId, BlockPos pos);
    *///?} else {
    @Nullable
    BlockEntity findBlockEntity(ServerLevel rootLevel, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos);

    Vec3 getLinkGlobalPos(MinecraftServer server, Identifier dimension, @Nullable UUID subLevelId, BlockPos pos);
    //?}

    boolean isInSubLevel(Level level, BlockPos pos);

    @Nullable
    Level getSubLevelLevel(Level anyLevel, UUID subUUID);

    Vec3 projectOutAt(Level rootLevel, UUID subUUID, Vec3 localPos, float partialTick);

    boolean isRootLevel(Level level);

    Vec3 projectOutFromBE(BlockEntity be, Vec3 localPos, float partialTick);

    void onConnectorBeforeMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos);

    void onConnectorAfterMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos);

    boolean isMigrating(BlockPos pos);
}
