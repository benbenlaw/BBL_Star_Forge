package com.benbenlaw.starforge.block.custom;

import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.entity.StarBlockEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StarBlock extends BaseEntityBlock {

    public static final MapCodec<StarBlock> CODEC = simpleCodec(props -> new StarBlock(props, 0, 0, 0xFFFFFF));
    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    private final int transferRate;
    private final int starPowerCapacity;
    private final int color;

    public StarBlock(Properties properties, int transferRate, int starPowerCapacity, int color) {
        super(properties);
        this.transferRate = transferRate;
        this.starPowerCapacity = starPowerCapacity;
        this.color = color;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public int getTransferRate() {
        return transferRate;
    }

    public int getStarPowerCapacity() {
        return starPowerCapacity;
    }

    public int getColor() {
        return color;
    }

    @Override
    protected VoxelShape getShape(BlockState p_334026_, BlockGetter p_334049_, BlockPos p_334056_, CollisionContext p_333870_) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        BlockEntity entity = level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {
            if (entity instanceof StarBlockEntity starBlockEntity) {
                starBlockEntity.rightClick(player);
            }
        }

        return InteractionResult.CONSUME;

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pNewState, boolean pIsMoving) {

        //if (pState.getBlock() != pNewState.getBlock()) {
        //    BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        //    if (blockEntity instanceof WoodenStrainerBlockEntity) {
        //        ((WoodenStrainerBlockEntity) blockEntity).drops();
        //    }
        //}
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StarBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SFBlockEntities.STAR_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }
}
