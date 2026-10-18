package net.cyber_jellyfish.cc_rj45.aero;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.network.wired.WiredElement;
import dan200.computercraft.api.network.wired.WiredNode;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;


public class BridgeWiredElement implements WiredElement {
    private static final Map<CacheKey, BridgeWiredElement> CACHE = new ConcurrentHashMap<>();

    private final WeakReference<BlockEntity> anchorRef;
    private final WiredNode node;
    private final Vec3 pos;

    private BridgeWiredElement(BlockEntity anchor) {
        this.anchorRef = new WeakReference<>(anchor);
        this.node = ComputerCraftAPI.createWiredNodeForElement(this);
        this.pos = Vec3.atCenterOf(anchor.getBlockPos());
    }

    public static BridgeWiredElement getOrCreate(BlockEntity anchor) {
        CacheKey key = new CacheKey(anchor.getLevel(), anchor.getBlockPos());
        return CACHE.compute(key, (k, existing) -> {
            if (existing != null && existing.isValid()) return existing;
            BlockEntity be = anchor.getLevel().getBlockEntity(anchor.getBlockPos());
            if (be == null || be.isRemoved()) return null;
            return new BridgeWiredElement(be);
        });
    }

    public static void invalidate(Level level, BlockPos pos) {
        CACHE.remove(new CacheKey(level, pos));
    }

    public static void invalidateLevel(Level level) {
        CACHE.keySet().removeIf(k -> k.level == level);
    }

    @Override
    public WiredNode getNode() {
        return node;
    }

    @Override
    public Level getLevel() {
        BlockEntity be = anchorRef.get();
        return be != null ? be.getLevel() : null;
    }

    @Override
    public Vec3 getPosition() {
        return pos;
    }

    @Override
    public String getSenderID() {
        BlockEntity be = anchorRef.get();
        if (be != null) {
            return "ccrj45:bridge:" + be.getBlockPos().toShortString();
        }
        return "ccrj45:bridge:orphan";
    }

    public boolean isValid() {
        BlockEntity be = anchorRef.get();
        return be != null && !be.isRemoved();
    }

    private record CacheKey(Level level, BlockPos pos) {}
}
