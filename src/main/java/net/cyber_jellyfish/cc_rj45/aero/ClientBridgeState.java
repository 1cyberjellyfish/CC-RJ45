package net.cyber_jellyfish.cc_rj45.aero;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;

public class ClientBridgeState {
    private static volatile Map<BlockPos, Set<BlockPos>> bridgeLinks = Map.of();

    public static void update(Map<BlockPos, Set<BlockPos>> links) {
        Map<BlockPos, Set<BlockPos>> copy = new HashMap<>();
        for (Map.Entry<BlockPos, Set<BlockPos>> entry : links.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<>(entry.getValue())));
        }
        bridgeLinks = Collections.unmodifiableMap(copy);
    }

    public static boolean hasLink(BlockPos pos) {
        return bridgeLinks.containsKey(pos);
    }

    public static Set<BlockPos> getLinksFor(BlockPos pos) {
        return bridgeLinks.getOrDefault(pos, Set.of());
    }

    public static Map<BlockPos, Set<BlockPos>> getAllLinks() {
        return bridgeLinks;
    }
}
