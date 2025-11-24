package com.benbenlaw.starforge.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.ItemStackHandler;

public class StarForgeRecipeInput implements RecipeInput {

    private final ItemStackHandler handler;

    public StarForgeRecipeInput(ItemStackHandler handler) {
        this.handler = handler;
    }

    @Override
    public ItemStack getItem(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public int size() {
        return 0;
    }
}
