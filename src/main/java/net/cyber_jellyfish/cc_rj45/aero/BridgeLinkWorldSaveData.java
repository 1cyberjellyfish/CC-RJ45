package net.cyber_jellyfish.cc_rj45.aero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class BridgeLinkWorldSaveData extends SavedData {
    private static final String ID = "ccrj45_bridge_links";

    private final Map<BlockPos, Set<BlockPos>> links = new HashMap<>();

    public static BridgeLinkWorldSaveData get(ServerLevel level) {
        //? if <=1.21.1 && neoforge {
        /*return level.getDataStorage().computeIfAbsent(
                new Factory<>(BridgeLinkWorldSaveData::new, BridgeLinkWorldSaveData::load, null),
                ID
        );
        *///?} else {
        return new BridgeLinkWorldSaveData();
        //?}
    }

    public void addLink(BlockPos a, BlockPos b) {
        links.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        links.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        setDirty();
    }

    public void removeLink(BlockPos a, BlockPos b) {
        Set<BlockPos> fromA = links.get(a);
        if (fromA != null) {
            fromA.remove(b);
            if (fromA.isEmpty()) links.remove(a);
        }
        Set<BlockPos> fromB = links.get(b);
        if (fromB != null) {
            fromB.remove(a);
            if (fromB.isEmpty()) links.remove(b);
        }
        setDirty();
    }

    public Set<BlockPos> getLinksFor(BlockPos pos) {
        return links.getOrDefault(pos, Set.of());
    }

    public boolean isLinked(BlockPos a, BlockPos b) {
        Set<BlockPos> fromA = links.get(a);
        return fromA != null && fromA.contains(b);
    }

    public boolean hasAnyLink(BlockPos pos) {
        return links.containsKey(pos);
    }

    public Map<BlockPos, Set<BlockPos>> getAll() {
        return Map.copyOf(links);
    }

    public void removeAllLinks(BlockPos pos) {
        Set<BlockPos> linked = links.remove(pos);
        if (linked != null) {
            for (BlockPos other : linked) {
                Set<BlockPos> fromOther = links.get(other);
                if (fromOther != null) {
                    fromOther.remove(pos);
                    if (fromOther.isEmpty()) links.remove(other);
                }
            }
            setDirty();
        }
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag data = new CompoundTag();
        int idx = 0;
        for (Map.Entry<BlockPos, Set<BlockPos>> entry : links.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putLong("pos", entry.getKey().asLong());
            long[] arr = entry.getValue().stream().mapToLong(BlockPos::asLong).toArray();
            entryTag.put("linked", new LongArrayTag(arr));
            data.put(String.valueOf(idx++), entryTag);
        }
        tag.put("links", data);
        return tag;
    }

    public static BridgeLinkWorldSaveData load(CompoundTag tag, HolderLookup.Provider registries) {
        BridgeLinkWorldSaveData data = new BridgeLinkWorldSaveData();
        //? if <=1.21.1 {
        /*CompoundTag linksTag = tag.getCompound("links");
        for (String key : linksTag.getAllKeys()) {
            CompoundTag entry = linksTag.getCompound(key);
        *///?} else {
        CompoundTag linksTag = tag.getCompoundOrEmpty("links");
        for (String key : linksTag.keySet()) {
            CompoundTag entry = linksTag.getCompoundOrEmpty(key);
        //?}
            BlockPos pos = BlockPos.of(net.cyber_jellyfish.cc_rj45.util.TagHelper.getLong(entry, "pos"));
            long[] linked = net.cyber_jellyfish.cc_rj45.util.TagHelper.getLongArray(entry, "linked");
            Set<BlockPos> linkedSet = new HashSet<>();
            for (long l : linked) {
                linkedSet.add(BlockPos.of(l));
            }
            data.links.put(pos, linkedSet);
        }
        return data;
    }
}
