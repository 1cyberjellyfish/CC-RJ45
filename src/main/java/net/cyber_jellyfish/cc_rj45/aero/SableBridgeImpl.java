//? if <=1.21.1 && neoforge {
/*package net.cyber_jellyfish.cc_rj45.aero;

import java.util.UUID;

import javax.annotation.Nullable;

import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import dev.ryanhcode.sable.api.block.BlockSubLevelAssemblyListener;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableConnectorHugeBlock;
import net.cyber_jellyfish.cc_rj45.block.NetCableHubBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;

public class SableBridgeImpl implements SableBridge {

    @Override
    public NetCableConnectorBlock createConnectorBlock(BlockBehaviour.Properties properties) {
        return new SableConnectorBlock(properties);
    }

    @Override
    public NetCableHubBlock createHubBlock(BlockBehaviour.Properties properties) {
        return new SableHubBlock(properties);
    }

    @Override
    public NetCableConnectorHugeBlock createHugeConnectorBlock(BlockBehaviour.Properties properties) {
        return new SableHugeConnectorBlock(properties);
    }

    public static class SableConnectorBlock extends NetCableConnectorBlock implements BlockSubLevelAssemblyListener {
        public SableConnectorBlock(Properties properties) {
            super(properties);
        }
    }

    public static class SableHubBlock extends NetCableHubBlock implements BlockSubLevelAssemblyListener {
        public SableHubBlock(Properties properties) {
            super(properties);
        }
    }

    public static class SableHugeConnectorBlock extends NetCableConnectorHugeBlock implements BlockSubLevelAssemblyListener {
        public SableHugeConnectorBlock(Properties properties) {
            super(properties);
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public UUID getContainingUUID(Level level, BlockPos pos) {
        SubLevelAccess access = SableCompanion.INSTANCE.getContaining(level, pos);
        return access != null ? access.getUniqueId() : null;
    }

    @Override
    public UUID getContainingUUID(BlockEntity be) {
        SubLevelAccess access = SableCompanion.INSTANCE.getContaining(be);
        return access != null ? access.getUniqueId() : null;
    }

    @Override
    public Vec3 projectOut(Level level, Vec3 localPos) {
        return SableCompanion.INSTANCE.projectOutOfSubLevel(level, localPos);
    }

    @Override
    public Vec3 projectOutInterpolated(Level level, Vec3 localPos, float partialTick) {
        SubLevelAccess access = SableCompanion.INSTANCE.getContaining(level, localPos);
        if (access instanceof ClientSubLevelAccess clientAccess) {
            return clientAccess.renderPose(partialTick).transformPosition(localPos);
        }
        return projectOut(level, localPos);
    }

    @Override
    public Vec3 getGlobalPos(Level level, BlockPos pos) {
        return projectOut(level, Vec3.atCenterOf(pos));
    }

    @Override
    public BlockEntity findBlockEntity(ServerLevel rootLevel, ResourceLocation dimension, UUID subLevelId, BlockPos pos) {
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimension);
        ServerLevel targetLevel = rootLevel.getServer().getLevel(dimensionKey);
        if (targetLevel == null) return null;

        if (subLevelId != null) {
            SubLevelContainer container = SubLevelContainer.getContainer(targetLevel);
            if (container == null) return null;
            SubLevel sub = container.getSubLevel(subLevelId);
            if (sub == null) return null;
            return sub.getLevel().getBlockEntity(pos);
        }

        return targetLevel.getBlockEntity(pos);
    }

    @Override
    public Vec3 getLinkGlobalPos(MinecraftServer server, ResourceLocation dimension, UUID subLevelId, BlockPos pos) {
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimension);
        ServerLevel rootLevel = server.getLevel(dimensionKey);
        if (rootLevel == null) return Vec3.atCenterOf(pos);

        if (subLevelId != null) {
            SubLevelContainer container = SubLevelContainer.getContainer(rootLevel);
            if (container == null) return Vec3.atCenterOf(pos);
            SubLevel sub = container.getSubLevel(subLevelId);
            if (sub == null) return Vec3.atCenterOf(pos);
            return projectOut(sub.getLevel(), Vec3.atCenterOf(pos));
        }

        return Vec3.atCenterOf(pos);
    }

    @Override
    public boolean isInSubLevel(Level level, BlockPos pos) {
        SubLevelAccess access = SableCompanion.INSTANCE.getContaining(level, pos);
        return access != null;
    }

    @Override
    public Level getSubLevelLevel(Level anyLevel, UUID subUUID) {
        SubLevelContainer container = SubLevelContainer.getContainer(anyLevel);
        if (container == null) return null;
        SubLevel sub = container.getSubLevel(subUUID);
        return sub != null ? sub.getLevel() : null;
    }

    @Override
    public boolean isRootLevel(Level level) {
        return SubLevelContainer.getContainer(level) != null;
    }

    @Override
    public Vec3 projectOutFromBE(BlockEntity be, Vec3 localPos, float partialTick) {
        SubLevelAccess access = SableCompanion.INSTANCE.getContaining(be);
        if (access instanceof ClientSubLevelAccess clientAccess) {
            return clientAccess.renderPose(partialTick).transformPosition(localPos);
        }
        return projectOutInterpolated(be.getLevel(), localPos, partialTick);
    }

    @Override
    public Vec3 projectOutAt(Level rootLevel, UUID subUUID, Vec3 localPos, float partialTick) {
        SubLevelContainer container = SubLevelContainer.getContainer(rootLevel);
        if (container == null) return localPos;
        SubLevel sub = container.getSubLevel(subUUID);
        if (sub == null) return localPos;
        if (sub instanceof ClientSubLevelAccess clientSub) {
            return clientSub.renderPose(partialTick).transformPosition(localPos);
        }
        return projectOut(sub.getLevel(), localPos);
    }

    private final java.util.Map<BlockPos, Long> migratingPositions = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public boolean isMigrating(BlockPos pos) {
        Long expiry = migratingPositions.get(pos);
        if (expiry == null) return false;
        if (System.currentTimeMillis() < expiry) return true;
        migratingPositions.remove(pos);
        return false;
    }

    @Override
    public void onConnectorBeforeMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {
        long expiry = System.currentTimeMillis() + 10000L;
        migratingPositions.put(sourcePos, expiry);
        migratingPositions.put(targetPos, expiry);

        BlockEntity sourceBe = sourceLevel.getBlockEntity(sourcePos);
        if (!(sourceBe instanceof net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity connector)) return;

        UUID sourceSubUUID = getContainingUUID(sourceLevel, sourcePos);
        UUID targetSubUUID = getContainingUUID(targetLevel, targetPos);
        ResourceLocation sourceDim = sourceLevel.dimension().location();
        ResourceLocation targetDim = targetLevel.dimension().location();

        java.util.Set<net.cyber_jellyfish.cc_rj45.util.CableLink> currentLinks = new java.util.HashSet<>(connector.getLinks());
        for (net.cyber_jellyfish.cc_rj45.util.CableLink link : currentLinks) {
            BlockEntity otherBe = findBlockEntity(sourceLevel, link.dimension(), link.subLevel(), link.pos());
            if (!(otherBe instanceof net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity otherConnector)) continue;

            UUID otherSubUUID = link.subLevel();
            boolean isIntraShip = java.util.Objects.equals(sourceSubUUID, otherSubUUID);

            if (isIntraShip) {
                BlockPos offset = otherConnector.getBlockPos().subtract(sourcePos);
                BlockPos expectedOtherTargetPos = targetPos.offset(offset.getX(), offset.getY(), offset.getZ());

                net.cyber_jellyfish.cc_rj45.util.CableLink oldLinkOnOther = new net.cyber_jellyfish.cc_rj45.util.CableLink(sourceDim, sourceSubUUID, sourcePos, link.color());
                net.cyber_jellyfish.cc_rj45.util.CableLink oldLinkOnOtherNullSub = new net.cyber_jellyfish.cc_rj45.util.CableLink(sourceDim, null, sourcePos, link.color());
                net.cyber_jellyfish.cc_rj45.util.CableLink newLinkOnOther = new net.cyber_jellyfish.cc_rj45.util.CableLink(targetDim, targetSubUUID, targetPos, link.color());

                if (otherConnector.isLinkedTo(oldLinkOnOther)) {
                    otherConnector.replaceLink(oldLinkOnOther, newLinkOnOther);
                } else if (otherConnector.isLinkedTo(oldLinkOnOtherNullSub)) {
                    otherConnector.replaceLink(oldLinkOnOtherNullSub, newLinkOnOther);
                }

                net.cyber_jellyfish.cc_rj45.util.CableLink newLinkOnSelf;
                if (targetSubUUID != null && otherSubUUID == null) {
                    newLinkOnSelf = new net.cyber_jellyfish.cc_rj45.util.CableLink(sourceDim, null, otherConnector.getBlockPos(), link.color());
                } else if (targetSubUUID == null && sourceSubUUID != null) {
                    newLinkOnSelf = new net.cyber_jellyfish.cc_rj45.util.CableLink(targetDim, null, otherConnector.getBlockPos(), link.color());
                } else {
                    newLinkOnSelf = new net.cyber_jellyfish.cc_rj45.util.CableLink(link.dimension(), targetSubUUID, expectedOtherTargetPos, link.color());
                }
                connector.replaceLink(link, newLinkOnSelf);
            } else {
                net.cyber_jellyfish.cc_rj45.util.CableLink oldLinkOnOther = new net.cyber_jellyfish.cc_rj45.util.CableLink(sourceDim, sourceSubUUID, sourcePos, link.color());
                net.cyber_jellyfish.cc_rj45.util.CableLink oldLinkOnOtherNullSub = new net.cyber_jellyfish.cc_rj45.util.CableLink(sourceDim, null, sourcePos, link.color());

                boolean nowSameLevel = java.util.Objects.equals(targetSubUUID, otherSubUUID);
                net.cyber_jellyfish.cc_rj45.util.CableLink newLinkOnOther = new net.cyber_jellyfish.cc_rj45.util.CableLink(targetDim, nowSameLevel ? null : targetSubUUID, targetPos, link.color());

                if (otherConnector.isLinkedTo(oldLinkOnOther)) {
                    otherConnector.replaceLink(oldLinkOnOther, newLinkOnOther);
                } else if (otherConnector.isLinkedTo(oldLinkOnOtherNullSub)) {
                    otherConnector.replaceLink(oldLinkOnOtherNullSub, newLinkOnOther);
                }

                net.cyber_jellyfish.cc_rj45.util.CableLink newLinkOnSelf = new net.cyber_jellyfish.cc_rj45.util.CableLink(link.dimension(), nowSameLevel ? null : link.subLevel(), link.pos(), link.color());
                connector.replaceLink(link, newLinkOnSelf);
            }
        }
    }

    @Override
    public void onConnectorAfterMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockPos sourcePos, BlockPos targetPos) {
        long expiry = System.currentTimeMillis() + 10000L;
        migratingPositions.put(sourcePos, expiry);
        migratingPositions.put(targetPos, expiry);

        BlockEntity newBe = targetLevel.getBlockEntity(targetPos);
        if (!(newBe instanceof net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity newConnector)) return;

        newConnector.refreshNetworkConnections();
        newConnector.sync();

        for (net.cyber_jellyfish.cc_rj45.util.CableLink link : newConnector.getLinks()) {
            BlockEntity otherBe = findBlockEntity(targetLevel, link.dimension(), link.subLevel(), link.pos());
            if (otherBe instanceof net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity otherConnector) {
                otherConnector.refreshNetworkConnections();
                otherConnector.sync();
            }
        }

        net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity.refreshConnectedNetwork(targetLevel, targetPos);
        net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity.refreshConnectedNetwork(sourceLevel, sourcePos);
    }
}
*///?}
