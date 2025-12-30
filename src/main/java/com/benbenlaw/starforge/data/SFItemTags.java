package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class SFItemTags extends ItemTagsProvider {

    SFItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags.contentsGetter(), StarForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(SFTags.Items.STAR_FORGE_BASE_BLOCKS).add(SFBlocks.COSMIC_PLANKS.get().asItem());

        tag(SFTags.Items.STAR_FORGE_PILLAR_BLOCKS).add(SFBlocks.COSMIC_PILLAR.get().asItem());

        tag(SFTags.Items.STAR_FORGE_TIER_1_CAP)
                .add(SFBlocks.TIER_1_STAR_FORGE_CAP.get().asItem())
                .addTag(SFTags.Items.STAR_FORGE_TIER_2_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_3_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_4_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_5_CAP);

        tag(SFTags.Items.STAR_FORGE_TIER_2_CAP)
                .add(SFBlocks.TIER_2_STAR_FORGE_CAP.get().asItem())
                .addTag(SFTags.Items.STAR_FORGE_TIER_3_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_4_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_5_CAP);

        tag(SFTags.Items.STAR_FORGE_TIER_3_CAP)
                .add(SFBlocks.TIER_3_STAR_FORGE_CAP.get().asItem())
                .addTag(SFTags.Items.STAR_FORGE_TIER_4_CAP)
                .addTag(SFTags.Items.STAR_FORGE_TIER_5_CAP);

        tag(SFTags.Items.STAR_FORGE_TIER_4_CAP)
                .add(SFBlocks.TIER_4_STAR_FORGE_CAP.get().asItem())
                .addTag(SFTags.Items.STAR_FORGE_TIER_5_CAP);

        tag(SFTags.Items.STAR_FORGE_TIER_5_CAP)
                .add(SFBlocks.TIER_5_STAR_FORGE_CAP.get().asItem());

        tag(SFTags.Items.STARS).add(
                SFBlocks.BLUE_STAR.get().asItem(),
                SFBlocks.WHITE_STAR.get().asItem(),
                SFBlocks.YELLOW_STAR.get().asItem(),
                SFBlocks.ORANGE_STAR.get().asItem(),
                SFBlocks.RED_STAR.get().asItem()
        );
    }

    @Override
    public String getName() {
        return StarForge.MOD_ID + " Item Tags";
    }
}
