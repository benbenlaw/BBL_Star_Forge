package com.benbenlaw.starforge.integration.jei;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.recipe.SFRecipes;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEISFPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    public static RecipeType<StarForgeRecipe> STAR_FORGE = new RecipeType<>(StarForgeRecipeCategory.UID, StarForgeRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SFBlocks.STAR_FORGE.get()), StarForgeRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StarForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        final var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(StarForgeRecipeCategory.RECIPE_TYPE,
                recipeManager.getAllRecipesFor(SFRecipes.STAR_FORGE_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());

    }

}
