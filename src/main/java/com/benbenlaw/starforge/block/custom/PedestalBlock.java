package com.benbenlaw.starforge.block.custom;

import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.entity.PedestalBlockEntity;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BaseEntityBlock {

    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);
    private static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.0625, 0.25, 0.75, 0.1875, 0.75), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.4375, 0.25, 0.75, 0.5625, 0.75), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.5625, 0.1875, 0.8125, 0.875, 0.8125), BooleanOp.OR);
    }

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* BLOCK ENTITY */

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pIsMoving) {

        //if (pState.getBlock() != pNewState.getBlock()) {
        //    BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        //    if (blockEntity instanceof WoodenStrainockEntity) {
        //        ((WoodenStrainerBlockEntity) blockEntity).drops();
        //    }
        //}
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        BlockEntity entity = level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {
            if (entity instanceof PedestalBlockEntity pedestalBlockEntity) {
                pedestalBlockEntity.rightClick(player);
            }
        }

        return InteractionResult.CONSUME;

    }

    @Override
    protected VoxelShape getShape(BlockState p_334026_, BlockGetter p_334049_, BlockPos p_334056_, CollisionContext p_333870_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PedestalBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SFBlockEntities.PEDESTAL_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }
}