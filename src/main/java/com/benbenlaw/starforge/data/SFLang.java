package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class SFLang extends LanguageProvider {

    public SFLang(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, StarForge.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.starforge", "Star Forge");

        //Blocks
        add("block.starforge.star_forge", "Star Forge");
        add("block.starforge.pedestal", "Star Forge Pedestal");
        add("block.starforge.cosmic_planks", "Cosmic Planks");
        add("block.starforge.cosmic_pillar", "Cosmic Pillar");
        add("block.starforge.tier_1_star_forge_cap", "Tier 1 Star Forge Cap");
        add("block.starforge.tier_2_star_forge_cap", "Tier 2 Star Forge Cap");
        add("block.starforge.tier_3_star_forge_cap", "Tier 3 Star Forge Cap");
        add("block.starforge.tier_4_star_forge_cap", "Tier 4 Star Forge Cap");
        add("block.starforge.tier_5_star_forge_cap", "Tier 5 Star Forge Cap");
        add("block.starforge.blue_star", "Blue Star");
        add("block.starforge.white_star", "White Star");
        add("block.starforge.yellow_star", "Yellow Star");
        add("block.starforge.orange_star", "Orange Star");
        add("block.starforge.red_star", "Red Star");

        //Item
        add("item.starforge.star_ingot", "Star Ingot");
        add("item.starforge.star_gem", "Star Gem");





    }

}