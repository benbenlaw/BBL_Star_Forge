package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SFBlockTags extends BlockTagsProvider {

    SFBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, StarForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(SFTags.Blocks.STAR_FORGE_BASE_BLOCKS).add(SFBlocks.COSMIC_PLANKS.get());

        tag(SFTags.Blocks.STAR_FORGE_PILLAR_BLOCKS).add(SFBlocks.COSMIC_PILLAR.get());

        tag(SFTags.Blocks.STAR_FORGE_TIER_1_CAP).add(SFBlocks.TIER_1_STAR_FORGE_CAP.get());
        tag(SFTags.Blocks.STAR_FORGE_TIER_2_CAP).add(SFBlocks.TIER_2_STAR_FORGE_CAP.get());
        tag(SFTags.Blocks.STAR_FORGE_TIER_3_CAP).add(SFBlocks.TIER_3_STAR_FORGE_CAP.get());
        tag(SFTags.Blocks.STAR_FORGE_TIER_4_CAP).add(SFBlocks.TIER_4_STAR_FORGE_CAP.get());
        tag(SFTags.Blocks.STAR_FORGE_TIER_5_CAP).add(SFBlocks.TIER_5_STAR_FORGE_CAP.get());

        tag(SFTags.Blocks.STARS).add(
                SFBlocks.BLUE_STAR.get(),
                SFBlocks.WHITE_STAR.get(),
                SFBlocks.YELLOW_STAR.get(),
                SFBlocks.ORANGE_STAR.get(),
                SFBlocks.RED_STAR.get()
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                SFBlocks.BLUE_STAR.get(),
                SFBlocks.WHITE_STAR.get(),
                SFBlocks.YELLOW_STAR.get(),
                SFBlocks.ORANGE_STAR.get(),
                SFBlocks.RED_STAR.get(),
                SFBlocks.STAR_FORGE.get(),
                SFBlocks.PEDESTAL.get(),
                SFBlocks.TIER_1_STAR_FORGE_CAP.get(),
                SFBlocks.TIER_2_STAR_FORGE_CAP.get(),
                SFBlocks.TIER_3_STAR_FORGE_CAP.get(),
                SFBlocks.TIER_4_STAR_FORGE_CAP.get(),
                SFBlocks.TIER_5_STAR_FORGE_CAP.get()
        );

        tag(BlockTags.MINEABLE_WITH_AXE).add(
                SFBlocks.COSMIC_PLANKS.get(),
                SFBlocks.COSMIC_PILLAR.get()
        );

    }


    @Override
    public String getName() {
        return StarForge.MOD_ID + " Block Tags";
    }
}
