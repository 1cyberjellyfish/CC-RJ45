package net.cyber_jellyfish.cc_rj45.block.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.Config;
import net.cyber_jellyfish.cc_rj45.aero.BridgeWiredElement;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;
import net.cyber_jellyfish.cc_rj45.util.CableLink;
import dan200.computercraft.api.ComputerCraftAPI;
import net.cyber_jellyfish.cc_rj45.util.CCApiHelper;
import dan200.computercraft.api.network.wired.WiredElement;
import dan200.computercraft.api.network.wired.WiredNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
//? if <=1.21.1 {
/*import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.resources.Identifier;
//?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class NetCableConnectorBlockEntity extends BlockEntity implements WiredElement {
    private static final String LINKS_SUB_TAG = "links_sub";
    private static final String LINKS_TAG = "links";
    private static final String LINKS_REL_TAG = "links_rel";

    public static final Set<NetCableConnectorBlockEntity> ALL_CONNECTORS = ConcurrentHashMap.newKeySet();
    public static final Set<NetCableConnectorBlockEntity> PENDING_REFRESH = ConcurrentHashMap.newKeySet();

    public static void processPendingRefreshes() {
        if (PENDING_REFRESH.isEmpty()) return;
        List<NetCableConnectorBlockEntity> toProcess = new ArrayList<>(PENDING_REFRESH);
        PENDING_REFRESH.clear();
        for (NetCableConnectorBlockEntity connector : toProcess) {
            if (!connector.isRemoved() && connector.getLevel() instanceof ServerLevel) {
                connector.refreshNetworkConnections();
            }
        }
    }

    private final Set<CableLink> linkedConnectors = ConcurrentHashMap.newKeySet();
    private final Set<WiredNode> adjacentNodes = new HashSet<>();
    private final Set<WiredNode> linkedNodes = new HashSet<>();
    private final WiredNode node;

    public NetCableConnectorBlockEntity(BlockPos pos, BlockState blockState) {
        this(CCRJ45.NET_CABLE_CONNECTOR_BE.get(), pos, blockState);
    }

    protected NetCableConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.node = ComputerCraftAPI.createWiredNodeForElement(this);
    }

    public boolean linkTo(BlockPos pos) {
        return linkTo(new CableLink(CCRJ45.getDimensionId(level), null, pos));
    }

    public boolean linkTo(CableLink link) {
        if (!canCreateLinkTo(link)) return false;
        boolean added = linkedConnectors.add(link);
        if (added) {
            connectLinkedConnector(link);
            long a = worldPosition.asLong();
            long b = link.pos().asLong();
            RECENT_REFUNDS.remove(a < b ? a + ":" + b : b + ":" + a);
            sync();
        }
        return added;
    }

    public void loadLink(CableLink link) {
        if (linkedConnectors.add(link)) {
            connectLinkedConnector(link);
            long a = worldPosition.asLong();
            long b = link.pos().asLong();
            RECENT_REFUNDS.remove(a < b ? a + ":" + b : b + ":" + a);
        }
    }

    public boolean isLinkedTo(BlockPos pos) {
        return linkedConnectors.contains(new CableLink(CCRJ45.getDimensionId(level), null, pos));
    }

    public boolean isLinkedTo(CableLink link) {
        return linkedConnectors.contains(link);
    }

    public boolean isLinkedToPos(BlockPos pos) {
        for (CableLink link : linkedConnectors) {
            if (link.pos().equals(pos)) return true;
        }
        return false;
    }

    @javax.annotation.Nullable
    public CableLink getLink(CableLink target) {
        for (CableLink link : linkedConnectors) {
            if (link.equals(target)) return link;
        }
        return null;
    }

    public boolean updateLinkColor(CableLink newLink) {
        if (!linkedConnectors.contains(newLink)) return false;
        linkedConnectors.remove(newLink);
        linkedConnectors.add(newLink);
        setChanged();
        sync();
        return true;
    }

    public int getConnectionCount() {
        return linkedConnectors.size();
    }

    public int getMaxConnections() {
        return 2;
    }

    public int getMaxLinkDistance() {
        return Config.connectorMaxCableLength();
    }

    public boolean canLinkToConnector(NetCableConnectorBlockEntity other) {
        double ownMaxSqr = (double) getMaxLinkDistance() * getMaxLinkDistance();
        double otherMaxSqr = (double) other.getMaxLinkDistance() * other.getMaxLinkDistance();

        Vec3 myGlobal = getGlobalPortPos();
        Vec3 otherGlobal = other.getGlobalPortPos();

        double distanceSqr = myGlobal.distanceToSqr(otherGlobal);
        return distanceSqr <= ownMaxSqr && distanceSqr <= otherMaxSqr;
    }

    @javax.annotation.Nullable
    public static NetCableConnectorBlockEntity getConnector(Level level, BlockPos pos) {
        if (!CCRJ45.isChunkLoadedNow(level, pos)) return null;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof NetCableConnectorBlockEntity connector) return connector;
        return null;
    }

    //? if <=1.21.1 {
    /*@javax.annotation.Nullable
    public static NetCableConnectorBlockEntity getConnector(ServerLevel level, ResourceLocation dimension, @javax.annotation.Nullable UUID subLevelId, BlockPos pos) {
        if (level == null || pos == null) return null;
        if (subLevelId != null || (dimension != null && !dimension.equals(CCRJ45.getDimensionId(level)))) {
            BlockEntity be = SableHelper.findBlockEntity(level, dimension, subLevelId, pos);
            if (be instanceof NetCableConnectorBlockEntity connector) return connector;
        }
        return getConnector(level, pos);
    }
    *///?} else {
    @javax.annotation.Nullable
    public static NetCableConnectorBlockEntity getConnector(ServerLevel level, Identifier dimension, @javax.annotation.Nullable UUID subLevelId, BlockPos pos) {
        if (level == null || pos == null) return null;
        if (subLevelId != null || (dimension != null && !dimension.equals(CCRJ45.getDimensionId(level)))) {
            BlockEntity be = SableHelper.findBlockEntity(level, dimension, subLevelId, pos);
            if (be instanceof NetCableConnectorBlockEntity connector) return connector;
        }
        return getConnector(level, pos);
    }
    //?}

    public boolean canCreateLinkTo(BlockPos pos) {
        return canCreateLinkTo(new CableLink(CCRJ45.getDimensionId(level), null, pos));
    }

    private boolean canCreateLinkTo(CableLink link) {
        if (isLinkedTo(link) || linkedConnectors.size() >= getMaxConnections()) return false;

        if (level instanceof ServerLevel serverLevel) {
            NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
            if (other != null) {
                return canLinkToConnector(other);
            }
        }

        double maxSqr = (double) getMaxLinkDistance() * getMaxLinkDistance();
        Vec3 myGlobal = getGlobalPortPos();
        Vec3 otherGlobal;
        if (link.subLevel() == null) {
            otherGlobal = SableHelper.getGlobalPos(level, link.pos());
        } else {
            otherGlobal = SableHelper.getLinkGlobalPos(
                    level != null && level.getServer() != null ? level.getServer() : null,
                    link.dimension(), link.subLevel(), link.pos());
        }
        return myGlobal.distanceToSqr(otherGlobal) <= maxSqr;
    }

    public double getCablePortOffset() {
        return 6.1 / 16.0;
    }

    public float getCableWidth() {
        return 0.08f;
    }

    public int getCableColorR() {
        return 100;
    }

    public int getCableColorG() {
        return 100;
    }

    public int getCableColorB() {
        return 100;
    }

    public boolean isPhysicalSideAllowed(Direction side) {
        if (side == null) return true;
        return side == getPhysicalConnectionSide();
    }

    protected Direction getPhysicalConnectionSide() {
        BlockState state = getBlockState();
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.getValue(BlockStateProperties.FACING).getOpposite();
        }
        return Direction.DOWN;
    }

    public boolean unlinkTo(BlockPos pos) {
        return unlinkTo(new CableLink(CCRJ45.getDimensionId(level), null, pos));
    }

    public boolean unlinkTo(CableLink link) {
        boolean removed = linkedConnectors.remove(link);
        if (removed) {
            disconnectLinkedConnector(link);
            sync();
        }
        return removed;
    }

    public Set<BlockPos> getLinkedPositions() {
        Set<BlockPos> result = new HashSet<>();
        for (CableLink link : linkedConnectors) {
            result.add(link.pos());
        }
        return result;
    }

    public Set<CableLink> getLinks() {
        return linkedConnectors;
    }

    public Vec3 getCablePortOrigin() {
        BlockState state = getBlockState();
        if (!state.hasProperty(BlockStateProperties.FACING)) {
            return new Vec3(0.5, 0.5, 0.5);
        }
        Direction facing = state.getValue(BlockStateProperties.FACING);
        double offset = getCablePortOffset();
        return switch (facing) {
            case UP -> new Vec3(0.5, offset, 0.5);
            case DOWN -> new Vec3(0.5, 1.0 - offset, 0.5);
            case NORTH -> new Vec3(0.5, 0.5, 1.0 - offset);
            case SOUTH -> new Vec3(0.5, 0.5, offset);
            case EAST -> new Vec3(offset, 0.5, 0.5);
            case WEST -> new Vec3(1.0 - offset, 0.5, 0.5);
        };
    }

    public Vec3 getGlobalPortPos() {
        Vec3 localPort = Vec3.atLowerCornerOf(worldPosition).add(getCablePortOrigin());
        if (level == null) return localPort;
        return SableHelper.projectOut(level, localPort);
    }

    public Vec3 getGlobalPos() {
        if (level == null) return Vec3.atCenterOf(worldPosition);
        return SableHelper.getGlobalPos(level, worldPosition);
    }

    public void refreshNetworkConnections() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        refreshAdjacentConnections(serverLevel);
        refreshLinkedConnections(serverLevel);
    }

    public static void refreshConnectedNetwork(Level level, BlockPos seed) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Set<Long> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(seed);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            if (!visited.add(currentPos.asLong())) continue;

            NetCableConnectorBlockEntity connector = getConnector(serverLevel, currentPos);
            if (connector == null) continue;

            connector.refreshNetworkConnections();

            for (BlockPos linked : connector.getLinkedPositions()) {
                if (!visited.contains(linked.asLong())) queue.add(linked);
            }

            for (Direction side : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(side);
                NetCableConnectorBlockEntity neighborConnector = getConnector(serverLevel, neighborPos);
                if (neighborConnector == null) continue;

                boolean aToB = connector.isPhysicalSideAllowed(side);
                boolean bToA = neighborConnector.isPhysicalSideAllowed(side.getOpposite());
                if (aToB && bToA && !visited.contains(neighborPos.asLong())) {
                    queue.add(neighborPos);
                }
            }
        }
    }

    public void replaceLink(CableLink oldLink, CableLink newLink) {
        if (linkedConnectors.remove(oldLink)) {
            disconnectLinkedConnector(oldLink);
            linkedConnectors.add(newLink);
            connectLinkedConnector(newLink);
            sync();
        }
    }

    //? if <=1.21.1 {
    /*@Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        net.minecraft.nbt.ListTag v2List = new net.minecraft.nbt.ListTag();
        int[] relValues = new int[linkedConnectors.size() * 3];
        CompoundTag subTag = new CompoundTag();
        int idx = 0;
        for (CableLink link : linkedConnectors) {
            v2List.add(link.toNbt());

            BlockPos offset = link.pos().subtract(worldPosition);
            relValues[idx] = offset.getX();
            relValues[idx + 1] = offset.getY();
            relValues[idx + 2] = offset.getZ();

            if (link.subLevel() != null) {
                subTag.putUUID(Long.toString(offset.asLong()), link.subLevel());
            }
            idx += 3;
        }
        tag.put("links_v2", v2List);
        tag.putIntArray(LINKS_REL_TAG, relValues);
        if (!subTag.isEmpty()) {
            tag.put(LINKS_SUB_TAG, subTag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        linkedConnectors.clear();

        ResourceLocation myDim = level != null ? level.dimension().location()
                : ResourceLocation.parse("minecraft:overworld");

        if (tag.contains("links_v2", Tag.TAG_LIST)) {
            net.minecraft.nbt.ListTag v2List = tag.getList("links_v2", Tag.TAG_COMPOUND);
            for (int i = 0; i < v2List.size(); i++) {
                linkedConnectors.add(CableLink.fromNbt(v2List.getCompound(i)));
            }
            return;
        }

        Map<Long, UUID> subMap = new HashMap<>();
        if (tag.contains(LINKS_SUB_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag subTag = tag.getCompound(LINKS_SUB_TAG);
            for (String key : subTag.getAllKeys()) {
                subMap.put(Long.parseLong(key), subTag.getUUID(key));
            }
        }

        int[] relData = tag.getIntArray(LINKS_REL_TAG);
        if (relData.length > 0) {
            for (int i = 0; i + 2 < relData.length; i += 3) {
                BlockPos offset = new BlockPos(relData[i], relData[i + 1], relData[i + 2]);
                BlockPos absPos = worldPosition.offset(offset.getX(), offset.getY(), offset.getZ());
                UUID sub = subMap.get(offset.asLong());
                linkedConnectors.add(new CableLink(myDim, sub, absPos));
            }
            return;
        }

        long[] absData = tag.getLongArray(LINKS_TAG);
        if (absData.length > 0) {
            for (long abs : absData) {
                BlockPos pos = BlockPos.of(abs);
                linkedConnectors.add(new CableLink(myDim, null, pos));
            }
        }
    }
    *///?} else {
    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        net.minecraft.world.level.storage.ValueOutput.ValueOutputList v2List = output.childrenList("links_v2");
        for (CableLink link : linkedConnectors) {
            net.minecraft.world.level.storage.ValueOutput child = v2List.addChild();
            child.putString("dim", link.dimension().toString());
            if (link.subLevel() != null) child.putString("sub", link.subLevel().toString());
            child.putIntArray("pos", new int[]{link.pos().getX(), link.pos().getY(), link.pos().getZ()});
            if (link.color() != null) {
                child.putString("color", link.color().getName());
            }
        }
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        linkedConnectors.clear();
        Identifier myDim = CCRJ45.getDimensionId(level);

        var listOpt = input.childrenList("links_v2");
        if (listOpt.isPresent()) {
            for (net.minecraft.world.level.storage.ValueInput child : listOpt.get()) {
                Identifier dim = Identifier.parse(child.getStringOr("dim", "minecraft:overworld"));
                UUID sub = child.getString("sub").map(UUID::fromString).orElse(null);
                int[] p = child.getIntArray("pos").orElse(new int[]{0, 0, 0});
                net.minecraft.world.item.DyeColor color = child.getString("color").map(c -> net.minecraft.world.item.DyeColor.byName(c, null)).orElse(null);
                linkedConnectors.add(new CableLink(dim, sub, new BlockPos(p[0], p[1], p[2]), color));
            }
        }
    }
    //?}

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        ALL_CONNECTORS.add(this);
        if (level instanceof ServerLevel) {
            PENDING_REFRESH.add(this);
        }
    }

    //? if neoforge {
    /*@Override
    public void onLoad() {
        super.onLoad();
        ALL_CONNECTORS.add(this);
        if (level instanceof ServerLevel) {
            PENDING_REFRESH.add(this);
        }
    }
    *///?}

    @Override
    public void setRemoved() {
        ALL_CONNECTORS.remove(this);
        PENDING_REFRESH.remove(this);
        if (level != null) {
            BridgeWiredElement.invalidate(level, worldPosition);
        }
        if (level instanceof ServerLevel) {
            try {
                node.remove();
            } catch (RuntimeException e) {
                CCRJ45.LOGGER.warn("Failed to remove wired node at {}", worldPosition, e);
            }
            adjacentNodes.clear();
            linkedNodes.clear();
        }
        super.setRemoved();
    }

    @Override
    public WiredNode getNode() {
        return node;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public Vec3 getPosition() {
        return Vec3.atCenterOf(worldPosition);
    }

    @Override
    public String getSenderID() {
        return "ccrj45:" + worldPosition.getX() + "," + worldPosition.getY() + "," + worldPosition.getZ();
    }

    public void checkLinkDistances() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (SableHelper.isMigrating(worldPosition)) return;

        Vec3 myGlobal = getGlobalPortPos();
        Set<CableLink> stale = new HashSet<>();

        for (CableLink link : linkedConnectors) {
            if (SableHelper.isMigrating(link.pos())) continue;

            NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
            int effectiveMaxDist = getMaxLinkDistance();
            Vec3 targetGlobal;
            if (other != null) {
                effectiveMaxDist = Math.min(effectiveMaxDist, other.getMaxLinkDistance());
                targetGlobal = other.getGlobalPortPos();
            } else {
                if (link.subLevel() == null) {
                    targetGlobal = SableHelper.getGlobalPos(level, link.pos());
                } else {
                    targetGlobal = SableHelper.getLinkGlobalPos(
                            serverLevel.getServer(), link.dimension(), link.subLevel(), link.pos());
                }
            }

            double maxSqr = (double) effectiveMaxDist * effectiveMaxDist;
            if (myGlobal.distanceToSqr(targetGlobal) > maxSqr) {
                stale.add(link);
            }
        }

        if (!stale.isEmpty()) {
            for (CableLink link : stale) {
                NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
                if (other != null) {
                    CableLink reverse = new CableLink(
                            CCRJ45.getDimensionId(level),
                            SableHelper.getContainingUUID(this),
                            worldPosition,
                            link.color());
                    other.unlinkTo(reverse);
                    other.sync();
                }
                disconnectLinkedConnector(link);
                linkedConnectors.remove(link);
                long a = worldPosition.asLong();
                long b = link.pos().asLong();
                String pairKey = a < b ? a + ":" + b : b + ":" + a;
                if (shouldRefund(pairKey)) {
                    ItemStack stack = link.color() != null && CCRJ45.COLORED_NET_CABLES.containsKey(link.color())
                            ? new ItemStack(CCRJ45.COLORED_NET_CABLES.get(link.color()).get())
                            : new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get());
                    Block.popResource(level, worldPosition, stack);
                }
            }
            sync();
        }
    }

    private void refreshAdjacentConnections(ServerLevel serverLevel) {
        Set<WiredNode> found = new HashSet<>();
        for (Direction side : Direction.values()) {
            if (!isPhysicalSideAllowed(side)) continue;

            BlockPos neighborPos = worldPosition.relative(side);
            if (!CCRJ45.isChunkLoadedNow(serverLevel, neighborPos)) continue;
            dan200.computercraft.api.network.wired.WiredElement neighbor = CCApiHelper.getWiredElement(serverLevel, neighborPos, side.getOpposite());
            if (neighbor == null) continue;

            WiredNode neighborNode = neighbor.getNode();
            if (neighborNode == node) continue;
            found.add(neighborNode);
            node.connectTo(neighborNode);
        }

        for (WiredNode oldNode : adjacentNodes) {
            if (!found.contains(oldNode)) {
                node.disconnectFrom(oldNode);
            }
        }

        adjacentNodes.clear();
        adjacentNodes.addAll(found);
    }

    private static final Map<String, Long> RECENT_REFUNDS = new ConcurrentHashMap<>();

    private static boolean shouldRefund(String pairKey) {
        long now = System.currentTimeMillis();
        Long last = RECENT_REFUNDS.put(pairKey, now);
        if (RECENT_REFUNDS.size() > 500) {
            RECENT_REFUNDS.entrySet().removeIf(entry -> (now - entry.getValue()) > 10000L);
        }
        return last == null || (now - last) > 2000L;
    }

    private void refreshLinkedConnections(ServerLevel serverLevel) {
        Set<WiredNode> found = new HashSet<>();

        for (CableLink link : linkedConnectors) {
            NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
            if (other != null) {
                WiredNode otherNode = other.getNode();
                found.add(otherNode);
                node.connectTo(otherNode);
            }
        }

        for (WiredNode oldNode : linkedNodes) {
            if (!found.contains(oldNode)) {
                node.disconnectFrom(oldNode);
            }
        }

        linkedNodes.clear();
        linkedNodes.addAll(found);
    }

    private void connectLinkedConnector(CableLink link) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
        if (other != null) {
            WiredNode otherNode = other.getNode();
            node.connectTo(otherNode);
            linkedNodes.add(otherNode);
        }
    }

    private void disconnectLinkedConnector(CableLink link) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        NetCableConnectorBlockEntity other = getConnector(serverLevel, link.dimension(), link.subLevel(), link.pos());
        if (other != null) {
            WiredNode otherNode = other.getNode();
            node.disconnectFrom(otherNode);
            linkedNodes.remove(otherNode);
        }
    }

    public void sync() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            //? if neoforge {
            /*level.invalidateCapabilities(worldPosition);
            *///?}
        }
    }

    private Set<CableLink> snapshotForDrops;

    public Set<BlockPos> destroyConnections(ServerLevel level) {
        return destroyConnections(level, true);
    }

    public Set<BlockPos> destroyConnections(ServerLevel level, boolean shouldDropItems) {
        BlockPos selfPos = getBlockPos();
        Set<CableLink> linksToProcess = new HashSet<>(linkedConnectors);

        for (CableLink link : linksToProcess) {
            long a = selfPos.asLong();
            long b = link.pos().asLong();
            String pairKey = a < b ? a + ":" + b : b + ":" + a;

            // Only drop the cable item once per connection pair and only if shouldDropItems is true
            if (shouldDropItems && shouldRefund(pairKey)) {
                ItemStack stack = link.color() != null && CCRJ45.COLORED_NET_CABLES.containsKey(link.color())
                        ? new ItemStack(CCRJ45.COLORED_NET_CABLES.get(link.color()).get())
                        : new ItemStack(CCRJ45.NET_CONNECTOR_CABLE.get());
                Block.popResource(level, selfPos, stack);
            }

            // Unlink remote connector so it doesn't leave an orphaned visual cable or state
            BlockEntity other = SableHelper.findBlockEntity(level, link.dimension(), link.subLevel(), link.pos());
            if (other instanceof NetCableConnectorBlockEntity otherConnector) {
                UUID mySub = SableHelper.getContainingUUID(this);
                boolean sameSub = java.util.Objects.equals(mySub, link.subLevel());
                CableLink backLink = new CableLink(CCRJ45.getDimensionId(level), sameSub ? null : mySub, selfPos, link.color());
                otherConnector.unlinkTo(backLink);
                refreshConnectedNetwork(otherConnector.getLevel(), link.pos());
            }
        }
        linkedConnectors.clear();

        for (Direction side : Direction.values()) {
            BlockPos neighborPos = selfPos.relative(side);
            if (!CCRJ45.isChunkLoadedNow(level, neighborPos)) continue;
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor instanceof NetCableConnectorBlockEntity connector) {
                refreshConnectedNetwork(level, neighborPos);
            }
        }

        Set<BlockPos> linked = new HashSet<>();
        for (CableLink link : linksToProcess) {
            linked.add(link.pos());
        }
        return linked;
    }

    public AABB getRenderBoundingBox() {
        AABB box = new AABB(worldPosition);
        for (CableLink link : linkedConnectors) {
            box = box.minmax(new AABB(link.pos()));
        }
        return box.inflate(1.0);
    }

    public void dropItems(ServerLevel level) {
        // Handled cleanly by destroyConnections(level, shouldDropItems)
    }
}
