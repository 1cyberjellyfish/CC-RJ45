package net.cyber_jellyfish.cc_rj45.block;

import com.mojang.serialization.MapCodec;
import net.cyber_jellyfish.cc_rj45.CCRJ45;
import net.cyber_jellyfish.cc_rj45.block.entity.NetCableConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.cyber_jellyfish.cc_rj45.aero.SableHelper;

public class NetCableConnectorBlock extends BaseEntityBlock {
    public static final MapCodec<NetCableConnectorBlock> CODEC = simpleCodec(NetCableConnectorBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0);
    private static final VoxelShape SHAPE_DOWN = Block.box(5.0, 8.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape SHAPE_NORTH = Block.box(5.0, 5.0, 8.0, 11.0, 11.0, 16.0);
    private static final VoxelShape SHAPE_SOUTH = Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 8.0);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 5.0, 5.0, 8.0, 11.0, 11.0);
    private static final VoxelShape SHAPE_WEST = Block.box(8.0, 5.0, 5.0, 16.0, 11.0, 11.0);

    public NetCableConnectorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return getShape(state, level, pos, CollisionContext.empty());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetCableConnectorBlockEntity(pos, state);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        NetCableConnectorBlockEntity.refreshConnectedNetwork(level, pos);
    }

    //? if <=1.21.1 {
    /*@Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        NetCableConnectorBlockEntity.refreshConnectedNetwork(level, pos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
            if (!SableHelper.isMigrating(pos)) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof NetCableConnectorBlockEntity connector) {
                    connector.destroyConnections(serverLevel, true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
    *///?} else {
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @org.jetbrains.annotations.Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        NetCableConnectorBlockEntity.refreshConnectedNetwork(level, pos);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel serverLevel, BlockPos pos, boolean movedByPiston) {
        if (!SableHelper.isMigrating(pos)) {
            BlockEntity be = serverLevel.getBlockEntity(pos);
            if (be instanceof NetCableConnectorBlockEntity connector) {
                connector.destroyConnections(serverLevel, true);
            }
        }
        super.affectNeighborsAfterRemoval(state, serverLevel, pos, movedByPiston);
    }
    //?}

    public void beforeMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockState state, BlockPos sourcePos, BlockPos targetPos) {
        SableHelper.onConnectorBeforeMove(sourceLevel, targetLevel, sourcePos, targetPos);
    }

    public void afterMove(ServerLevel sourceLevel, ServerLevel targetLevel, BlockState state, BlockPos sourcePos, BlockPos targetPos) {
        SableHelper.onConnectorAfterMove(sourceLevel, targetLevel, sourcePos, targetPos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!SableHelper.isMigrating(pos) && level instanceof ServerLevel serverLevel) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NetCableConnectorBlockEntity connector) {
                boolean drop = player == null || !player.getAbilities().instabuild;
                connector.destroyConnections(serverLevel, drop);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
    }

    //? if neoforge {
    /*//? if <=1.21.1 {
    /^@Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!SableHelper.isMigrating(pos) && level instanceof ServerLevel serverLevel) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NetCableConnectorBlockEntity connector) {
                connector.destroyConnections(serverLevel, true);
            }
        }
        super.onBlockExploded(state, level, pos, explosion);
    }
    ^///?} else {
    @Override
    public void onBlockExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
        if (!SableHelper.isMigrating(pos)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof NetCableConnectorBlockEntity connector) {
                connector.destroyConnections(level, true);
            }
        }
        super.onBlockExploded(state, level, pos, explosion);
    }
    //?}
    *///?}
}
