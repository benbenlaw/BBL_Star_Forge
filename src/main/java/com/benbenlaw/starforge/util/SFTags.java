package com.benbenlaw.starforge.util;

import com.benbenlaw.core.util.CoreTags;
import com.benbenlaw.starforge.StarForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SFTags  {

    public static class Blocks {

        public static final TagKey<Block> STAR_FORGE_BASE_BLOCKS = tag("star_forge_base_blocks");
        public static final TagKey<Block> STAR_FORGE_PILLAR_BLOCKS = tag("star_forge_pillar_blocks");
        public static final TagKey<Block> STAR_FORGE_TIER_1_CAP = tag("star_forge_tier_1_cap");
        public static final TagKey<Block> STAR_FORGE_TIER_2_CAP = tag("star_forge_tier_2_cap");
        public static final TagKey<Block> STAR_FORGE_TIER_3_CAP = tag("star_forge_tier_3_cap");
        public static final TagKey<Block> STAR_FORGE_TIER_4_CAP = tag("star_forge_tier_4_cap");
        public static final TagKey<Block> STAR_FORGE_TIER_5_CAP = tag("star_forge_tier_5_cap");
        public static final TagKey<Block> STARS = tag("stars");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Items {

        public static final TagKey<Item> STAR_FORGE_BASE_BLOCKS = tag("star_forge_base_blocks");
        public static final TagKey<Item> STAR_FORGE_PILLAR_BLOCKS = tag("star_forge_pillar_blocks");
        public static final TagKey<Item> STAR_FORGE_TIER_1_CAP = tag("star_forge_tier_1_cap");
        public static final TagKey<Item> STAR_FORGE_TIER_2_CAP = tag("star_forge_tier_2_cap");
        public static final TagKey<Item> STAR_FORGE_TIER_3_CAP = tag("star_forge_tier_3_cap");
        public static final TagKey<Item> STAR_FORGE_TIER_4_CAP = tag("star_forge_tier_4_cap");
        public static final TagKey<Item> STAR_FORGE_TIER_5_CAP = tag("star_forge_tier_5_cap");
        public static final TagKey<Item> STARS = tag("stars");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }



}
