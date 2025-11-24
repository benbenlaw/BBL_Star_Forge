package com.benbenlaw.starforge.data.builder;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StarForgeRecipeBuilder implements RecipeBuilder {

    protected String group;
    protected int tier;
    protected int starPower;
    protected int duration;
    protected Ingredient input;
    Optional<List<Ingredient>> extraIngredients;
    ItemStack result;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public StarForgeRecipeBuilder(int tier, int starPower, int duration, Ingredient input,
                                  Optional<List<Ingredient>> extraIngredients, ItemStack result) {
        this.tier = tier;
        this.starPower = starPower;
        this.duration = duration;
        this.input = input;
        this.extraIngredients = extraIngredients;
        this.result = result;
    }

    public static StarForgeRecipeBuilder starForgeRecipeBuilder(int tier, int starPower, int duration,
                                                                Ingredient input, Optional<List<Ingredient>> extraIngredients,
                                                                ItemStack result) {
        return new StarForgeRecipeBuilder(tier, starPower, duration, input, extraIngredients, result);
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return ItemStack.EMPTY.getItem();
    }

    public void save(@NotNull RecipeOutput recipeOutput) {
        this.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "forge/" +
                BuiltInRegistries.ITEM.getKey(this.result.getItem()).getPath()));
    }

    @Override
    public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
        Advancement.Builder builder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(builder::addCriterion);
        StarForgeRecipe coolantRecipe = new StarForgeRecipe(this.tier, this.starPower, this.duration, this.input, this.extraIngredients, this.result);
        recipeOutput.accept(id, coolantRecipe, builder.build(id.withPrefix("recipes/forge/")));

    }
}
