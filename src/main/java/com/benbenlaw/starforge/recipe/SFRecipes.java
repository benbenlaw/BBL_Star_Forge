package com.benbenlaw.starforge.recipe;

import com.benbenlaw.starforge.StarForge;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SFRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, StarForge.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, StarForge.MOD_ID);


    //Star Forge Recipe
    public static final Supplier<RecipeSerializer<StarForgeRecipe>> STAR_FORGE_RECIPE_SERIALIZER =
            SERIALIZER.register("star_forge", () -> StarForgeRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<StarForgeRecipe>> STAR_FORGE_RECIPE_TYPE =
            TYPES.register("star_forge", () -> StarForgeRecipe.Type.INSTANCE);
}
