package net.cyber_jellyfish.cc_rj45.block;

import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorHugeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetCableConnectorHugeBlock extends NetCableConnectorBlock {
    public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("horizontal_facing", Direction.class, Direction.Plane.HORIZONTAL);
    // UP: no rotation; NS = y:0/180, EW = y:90/270 (model x↔z flip)
    private static final VoxelShape SHAPE_UP_NS = Block.box(1.0, 0.0, 4.0, 15.0, 10.0, 12.0);
    private static final VoxelShape SHAPE_UP_EW = Block.box(4.0, 0.0, 1.0, 12.0, 10.0, 15.0);
    // DOWN: x:180; NS = y:0/180, EW = y:90/270
    private static final VoxelShape SHAPE_DOWN_NS = Block.box(1.0, 6.0, 4.0, 15.0, 16.0, 12.0);
    private static final VoxelShape SHAPE_DOWN_EW = Block.box(4.0, 6.0, 1.0, 12.0, 16.0, 15.0);
    // WALL: x:90 + optional y:90/180/270
    private static final VoxelShape SHAPE_NORTH = Block.box(1.0, 4.0, 6.0, 15.0, 12.0, 16.0);
    private static final VoxelShape SHAPE_SOUTH = Block.box(1.0, 4.0, 0.0, 15.0, 12.0, 10.0);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 4.0, 1.0, 10.0, 12.0, 15.0);
    private static final VoxelShape SHAPE_WEST = Block.box(6.0, 4.0, 1.0, 16.0, 12.0, 15.0);

    public NetCableConnectorHugeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.UP) {
            Direction hf = state.getValue(HORIZONTAL_FACING);
            return (hf == Direction.EAST || hf == Direction.WEST) ? SHAPE_UP_EW : SHAPE_UP_NS;
        }
        if (facing == Direction.DOWN) {
            Direction hf = state.getValue(HORIZONTAL_FACING);
            return (hf == Direction.EAST || hf == Direction.WEST) ? SHAPE_DOWN_EW : SHAPE_DOWN_NS;
        }
        return switch (facing) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_UP_NS;
        };
    }

    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetCableConnectorHugeBlockEntity(pos, state);
    }
}
