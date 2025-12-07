package com.benbenlaw.starforge.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CapBlock extends Block {
    public CapBlock(Properties properties) {
        super(properties);
    }

    private static VoxelShape SHAPE = Shapes.empty();

    static {
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.75, 0.25, 0.75, 0.875, 0.75), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.375, 0.25, 0.75, 0.5, 0.75), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.0625, 0.1875, 0.8125, 0.375, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 0.9375, 0.8125), BooleanOp.OR);
        SHAPE = Shapes.join(SHAPE, Shapes.box(0.3125, 0.5, 0.3125, 0.6875, 0.75, 0.6875), BooleanOp.OR);
    }

    @Override
    protected VoxelShape getShape(BlockState p_334026_, BlockGetter p_334049_, BlockPos p_334056_, CollisionContext p_333870_) {
        return SHAPE;
    }
}
