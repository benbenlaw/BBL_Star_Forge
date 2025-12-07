package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.block.SFBlocks;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SFLootTables extends VanillaBlockLoot {

    public SFLootTables(HolderLookup.Provider p_344962_) {
        super(p_344962_);
    }
    @Override
    protected void generate() {

        this.dropSelf(SFBlocks.BLUE_STAR.get());
        this.dropSelf(SFBlocks.WHITE_STAR.get());
        this.dropSelf(SFBlocks.YELLOW_STAR.get());
        this.dropSelf(SFBlocks.ORANGE_STAR.get());
        this.dropSelf(SFBlocks.RED_STAR.get());
        this.dropSelf(SFBlocks.STAR_FORGE.get());
        this.dropSelf(SFBlocks.PEDESTAL.get());
        this.dropSelf(SFBlocks.COSMIC_PILLAR.get());
        this.dropSelf(SFBlocks.COSMIC_PLANKS.get());
        this.dropSelf(SFBlocks.TIER_1_STAR_FORGE_CAP.get());
        this.dropSelf(SFBlocks.TIER_2_STAR_FORGE_CAP.get());
        this.dropSelf(SFBlocks.TIER_3_STAR_FORGE_CAP.get());
        this.dropSelf(SFBlocks.TIER_4_STAR_FORGE_CAP.get());
        this.dropSelf(SFBlocks.TIER_5_STAR_FORGE_CAP.get());
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }
    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    @NotNull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}
