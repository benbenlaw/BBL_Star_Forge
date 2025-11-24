package com.benbenlaw.starforge.block.custom;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
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

public class StarForgeBlock extends BaseEntityBlock {

    public static final MapCodec<StarForgeBlock> CODEC = simpleCodec(StarForgeBlock::new);
    private static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 14.0F, 12.0F);

    public StarForgeBlock(Properties properties) {
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
        //    if (blockEntity instanceof WoodenStrainerBlockEntity) {
        //        ((WoodenStrainerBlockEntity) blockEntity).drops();
        //    }
        //}
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    protected VoxelShape getShape(BlockState p_334026_, BlockGetter p_334049_, BlockPos p_334056_, CollisionContext p_333870_) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        BlockEntity entity = level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {
            if (entity instanceof StarForgeBlockEntity starForgeBlockEntity) {
                starForgeBlockEntity.rightClick(player);
            }
        }

        return InteractionResult.CONSUME;

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StarForgeBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SFBlockEntities.STAR_FORGE_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }
}
