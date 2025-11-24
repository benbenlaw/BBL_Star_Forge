package com.benbenlaw.starforge.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.IInventoryHandlingBlockEntity;
import com.benbenlaw.core.block.entity.handler.InputOutputItemHandler;
import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import com.benbenlaw.starforge.recipe.StarForgeRecipeInput;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PedestalBlockEntity extends SyncableBlockEntity implements IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    private final IItemHandler pedestalItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == 0 ,  //
            i -> false
    );

    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return pedestalItemHandler;
    }

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(SFBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), pos, state);
    }


    public void tick() {

    }

    public void rightClick(Player player) {

        ItemStack heldItem = player.getMainHandItem();

        if (!heldItem.isEmpty()) {
            // Insert only one item
            ItemStack toInsert = heldItem.copy();
            toInsert.setCount(1);

            ItemStack remaining = itemHandler.insertItem(0, toInsert, false);
            if (remaining.isEmpty()) {
                // Decrease the player's held stack by 1
                heldItem.shrink(1);
                player.setItemInHand(player.getUsedItemHand(), heldItem);
                setChanged();
                sync();
            }
        }
        else if (heldItem.isEmpty()) {
            ItemStack inputStack = itemHandler.getStackInSlot(0);

            if (!inputStack.isEmpty()) {
                // Extract only one item
                ItemStack extracted = itemHandler.extractItem(0, 1, false);
                player.setItemInHand(InteractionHand.MAIN_HAND, extracted);
                setChanged();
                sync();
            }
        }

    }


    public StarForgeBlockEntity findNearbyForge() {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                BlockPos pos = worldPosition.offset(x, 0, z);
                if (level.getBlockEntity(pos) instanceof StarForgeBlockEntity forge) {
                    return forge;
                }
            }
        }
        return null;
    }


    @Override
    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", itemHandler.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.itemHandler.deserializeNBT(provider, tag.getCompound("inventory"));
    }
}
