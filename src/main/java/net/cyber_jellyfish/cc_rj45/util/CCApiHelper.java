//? if neoforge {
/*package net.cyber_jellyfish.cc_rj45.util;

import dan200.computercraft.api.network.wired.WiredElement;
import dan200.computercraft.api.network.wired.WiredElementCapability;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class CCApiHelper {
    public static WiredElement getWiredElement(Level level, BlockPos pos, Direction side) {
        if (!CCRJ45.isChunkLoadedNow(level, pos)) return null;
        return level.getCapability(WiredElementCapability.get(), pos, side);
    }
}
*///?} else {
package net.cyber_jellyfish.cc_rj45.util;

import dan200.computercraft.api.network.wired.WiredElement;
import dan200.computercraft.api.network.wired.WiredElementLookup;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class CCApiHelper {
    public static WiredElement getWiredElement(Level level, BlockPos pos, Direction side) {
        if (!CCRJ45.isChunkLoadedNow(level, pos)) return null;
        return WiredElementLookup.get().find(level, pos, side);
    }
}
//?}
