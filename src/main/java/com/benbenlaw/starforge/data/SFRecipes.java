package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.data.builder.StarForgeRecipeBuilder;
import com.benbenlaw.starforge.item.SFItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SFRecipes extends RecipeProvider {

    public SFRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {

        //Star Forge
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SFBlocks.STAR_FORGE.get())
                .define('A', Tags.Items.INGOTS_GOLD)
                .define('B', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('C', Items.ANVIL)
                .define('D', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('E', Items.NETHER_STAR)
                .pattern("ABA")
                .pattern("CEC")
                .pattern("DDD")
                .unlockedBy("has_star", has(Items.NETHER_STAR)).save(consumer);

        //Cosmic Planks
        StarForgeRecipeBuilder.starForgeRecipeBuilder(-1, 0, 20, Ingredient.of(ItemTags.PLANKS),
                Optional.empty(),
                new ItemStack(SFBlocks.COSMIC_PLANKS.get(), 1)).save(consumer);

        //Cosmic Pillar
        StarForgeRecipeBuilder.starForgeRecipeBuilder(-1, 0, 20, Ingredient.of(ItemTags.LOGS),
                Optional.empty(),
                new ItemStack(SFBlocks.COSMIC_PILLAR.get(), 1)).save(consumer);

        //Pedestal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SFBlocks.PEDESTAL.get())
                .define('A', Tags.Items.INGOTS_GOLD)
                .define('B', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('C', SFBlocks.COSMIC_PILLAR.get())
                .define('D', SFBlocks.COSMIC_PLANKS.get())
                .pattern("ABA")
                .pattern(" C ")
                .pattern("DDD")
                .unlockedBy("has_star", has(SFBlocks.COSMIC_PLANKS)).save(consumer);

        //Blue Star
        StarForgeRecipeBuilder.starForgeRecipeBuilder(0, 0, 200,
                Ingredient.of(Items.NETHER_STAR),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS)
                )),
                new ItemStack(SFBlocks.BLUE_STAR.get(), 1)).save(consumer);

        //Tier 1 Cap
        StarForgeRecipeBuilder.starForgeRecipeBuilder(0, 250, 200,
                Ingredient.of(Items.BEACON),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS)
                )),
                new ItemStack(SFBlocks.TIER_1_STAR_FORGE_CAP.get(), 1)).save(consumer);

        //White Star
        StarForgeRecipeBuilder.starForgeRecipeBuilder(1, 1000, 200,
                Ingredient.of(SFBlocks.BLUE_STAR.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON)
                )),
                new ItemStack(SFBlocks.WHITE_STAR.get(), 1)).save(consumer);

        //Tier 2 Cap
        StarForgeRecipeBuilder.starForgeRecipeBuilder(1, 500, 200,
                Ingredient.of(SFBlocks.TIER_1_STAR_FORGE_CAP.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON)
                )),
                new ItemStack(SFBlocks.TIER_2_STAR_FORGE_CAP.get(), 1)).save(consumer);

        //Yellow Star
        StarForgeRecipeBuilder.starForgeRecipeBuilder(2, 2000, 200,
                Ingredient.of(SFBlocks.WHITE_STAR.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD)
                )),
                new ItemStack(SFBlocks.YELLOW_STAR.get(), 1)).save(consumer);

        //Tier 3 Cap
        StarForgeRecipeBuilder.starForgeRecipeBuilder(2, 1000, 200,
                Ingredient.of(SFBlocks.TIER_2_STAR_FORGE_CAP.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD)
                )),
                new ItemStack(SFBlocks.TIER_3_STAR_FORGE_CAP.get(), 1)).save(consumer);

        //Orange Star
        StarForgeRecipeBuilder.starForgeRecipeBuilder(3, 4000, 200,
                Ingredient.of(SFBlocks.YELLOW_STAR.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND)
                )),
                new ItemStack(SFBlocks.ORANGE_STAR.get(), 1)).save(consumer);

        //Tier 4 Cap
        StarForgeRecipeBuilder.starForgeRecipeBuilder(3, 2000, 200,
                Ingredient.of(SFBlocks.TIER_3_STAR_FORGE_CAP.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND)
                )),
                new ItemStack(SFBlocks.TIER_4_STAR_FORGE_CAP.get(), 1)).save(consumer);

        //Red Star
        StarForgeRecipeBuilder.starForgeRecipeBuilder(4, 8000, 200,
                Ingredient.of(SFBlocks.ORANGE_STAR.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE)
                )),
                new ItemStack(SFBlocks.RED_STAR.get(), 1)).save(consumer);

        //Tier 5 Cap
        StarForgeRecipeBuilder.starForgeRecipeBuilder(4, 4000, 200,
                Ingredient.of(SFBlocks.TIER_4_STAR_FORGE_CAP.get()),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE)
                )),
                new ItemStack(SFBlocks.TIER_5_STAR_FORGE_CAP.get(), 1)).save(consumer);

        //Star Ingot
        StarForgeRecipeBuilder.starForgeRecipeBuilder(5, 16000, 400,
                Ingredient.of(Tags.Items.INGOTS_IRON),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.INGOTS_NETHERITE),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_COPPER),
                        Ingredient.of(Tags.Items.INGOTS_IRON)             ,
                        Ingredient.of(Tags.Items.INGOTS_NETHERITE),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_COPPER),
                        Ingredient.of(Tags.Items.INGOTS_IRON)
                )),
                new ItemStack(SFItems.STAR_INGOT.get(), 1)).save(consumer);

        //Star Gem
        StarForgeRecipeBuilder.starForgeRecipeBuilder(5, 16000, 400,
                Ingredient.of(Tags.Items.GEMS_DIAMOND),
                Optional.of(List.of(
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.GEMS_EMERALD),
                        Ingredient.of(Tags.Items.GEMS_QUARTZ),
                        Ingredient.of(Tags.Items.GEMS_LAPIS),
                        Ingredient.of(Tags.Items.GEMS_AMETHYST),
                        Ingredient.of(Tags.Items.GEMS_PRISMARINE)
                )),
                new ItemStack(SFItems.STAR_GEM.get(), 1)).save(consumer);


    }


}