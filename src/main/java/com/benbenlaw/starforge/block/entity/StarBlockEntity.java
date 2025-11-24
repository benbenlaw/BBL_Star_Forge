package com.benbenlaw.starforge.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.IInventoryHandlingBlockEntity;
import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import com.benbenlaw.starforge.recipe.StarForgeRecipeInput;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

public class StarBlockEntity extends SyncableBlockEntity {

    private int transferRate;
    private int starPowerCapacity;
    private int starPower;

    public StarBlockEntity(BlockPos pos, BlockState state) {
        super(SFBlockEntities.STAR_BLOCK_ENTITY.get(), pos, state);
        this.transferRate = ((StarBlock) state.getBlock()).getTransferRate();
        this.starPowerCapacity = ((StarBlock) state.getBlock()).getStarPowerCapacity();
    }


    public void tick() {
        Level level = this.getLevel();
        if (level == null || level.isClientSide()) return; // Only run on server

        BlockPos abovePos = this.worldPosition.above();

        if (level.isNight() && level.canSeeSky(abovePos)) {
            charge();
        }
    }


    public void charge() {
        if (this.starPower < this.starPowerCapacity) {
            this.starPower += this.transferRate;
            if (this.starPower > this.starPowerCapacity) {
                this.starPower = this.starPowerCapacity;
            }
            setChanged();
        }
    }

    public int extractPower(int amount) {
        int extracted = Math.min(amount, starPower);
        starPower -= extracted;
        if (starPower < 0) starPower = 0;
        setChanged();
        return extracted;
    }

    public int getStarPower() {
        return starPower;
    }

    public int getTransferRate() {
        return transferRate;
    }

    public void rightClick(Player player) {

        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.is(Items.STICK)) {
            player.sendSystemMessage(Component.nullToEmpty(starPower + "/" + starPowerCapacity));
            player.sendSystemMessage(Component.nullToEmpty(transferRate + " SP/t"));
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("starPower", starPower);
        tag.putInt("transferRate", transferRate);
        tag.putInt("starPowerCapacity", starPowerCapacity);


    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.starPower = tag.getInt("starPower");
        this.transferRate = tag.getInt("transferRate");
        this.starPowerCapacity = tag.getInt("starPowerCapacity");
    }
}
