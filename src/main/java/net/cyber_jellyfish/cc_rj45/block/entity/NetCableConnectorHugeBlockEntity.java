package net.cyber_jellyfish.cc_rj45.block.entity;

import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NetCableConnectorHugeBlockEntity extends NetCableConnectorBlockEntity {
    public NetCableConnectorHugeBlockEntity(BlockPos pos, BlockState blockState) {
        super(CCRJ45.NET_CABLE_CONNECTOR_HUGE_BE.get(), pos, blockState);
    }

    @Override
    public int getMaxConnections() {
        return 2;
    }

    @Override
    public int getMaxLinkDistance() {
        return Config.connectorHugeMaxCableLength();
    }

    @Override
    public double getCablePortOffset() {
        return 6.1 / 16.0;
    }

    public float getCableWidth() {
        return 0.14f;
    }

    public int getCableColorR() {
        return 40;
    }

    public int getCableColorG() {
        return 40;
    }

    public int getCableColorB() {
        return 40;
    }
}
