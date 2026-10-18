package net.cyber_jellyfish.cc_rj45.block.entity;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NetCableHubBlockEntity extends NetCableConnectorBlockEntity {
    public NetCableHubBlockEntity(BlockPos pos, BlockState blockState) {
        super(CCRJ45.NET_CABLE_HUB_BE.get(), pos, blockState);
    }

    @Override
    public int getMaxConnections() {
        return 4;
    }

    @Override
    public double getCablePortOffset() {
        return 6.475 / 16.0;
    }
}
