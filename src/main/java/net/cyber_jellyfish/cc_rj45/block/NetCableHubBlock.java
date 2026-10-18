package net.cyber_jellyfish.cc_rj45.block;

import net.cyber_jellyfish.cc_rj45.block.entity.NetCableHubBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetCableHubBlock extends NetCableConnectorBlock {
    private static final VoxelShape SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    private static final VoxelShape SHAPE_DOWN = Block.box(5.0, 8.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape SHAPE_NORTH = Block.box(5.0, 4.0, 8.0, 11.0, 11.0, 16.0);
    private static final VoxelShape SHAPE_SOUTH = Block.box(5.0, 4.0, 0.0, 11.0, 11.0, 8.0);
    private static final VoxelShape SHAPE_WEST = Block.box(8.0, 4.0, 5.0, 16.0, 11.0, 11.0);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 4.0, 5.0, 8.0, 11.0, 11.0);

    public NetCableHubBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            case UP -> SHAPE_UP;
        };
    }

    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetCableHubBlockEntity(pos, state);
    }
}
