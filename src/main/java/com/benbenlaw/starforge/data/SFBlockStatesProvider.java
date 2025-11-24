package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class SFBlockStatesProvider extends BlockStateProvider {

    public SFBlockStatesProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, StarForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        generateStarBlock(SFBlocks.BLUE_STAR);
        generateStarBlock(SFBlocks.WHITE_STAR);
        generateStarBlock(SFBlocks.ORANGE_STAR);
        generateStarBlock(SFBlocks.YELLOW_STAR);
        generateStarBlock(SFBlocks.RED_STAR);

        generateStarForgeBlock(SFBlocks.STAR_FORGE);
        generatePedestalBlock(SFBlocks.PEDESTAL);

        blockWithItem(SFBlocks.COSMIC_PLANKS);
        generatePillarBlock(SFBlocks.COSMIC_PILLAR);

        blockWithItem(SFBlocks.TIER_1_STAR_FORGE_CAP);
        blockWithItem(SFBlocks.TIER_2_STAR_FORGE_CAP);
        blockWithItem(SFBlocks.TIER_3_STAR_FORGE_CAP);
        blockWithItem(SFBlocks.TIER_4_STAR_FORGE_CAP);
        blockWithItem(SFBlocks.TIER_5_STAR_FORGE_CAP);




    }

    private void generateStarBlock(DeferredBlock<Block> blockRegistryObject) {
        String path = blockRegistryObject.getId().getPath();
        models().withExistingParent(path, modLoc("block/mini_star"))
                .texture("all", modLoc("block/" + path))
                .texture("particle", modLoc("block/" + path)).renderType("cutout");

        simpleBlockWithItem(blockRegistryObject.get(), models().getBuilder(path));
    }

    private void generateStarForgeBlock(DeferredBlock<Block> blockRegistryObject) {
        String path = blockRegistryObject.getId().getPath();
        models().withExistingParent(path, modLoc("block/star_forge_model"))
                .texture("all", modLoc("block/" + path))
                .texture("particle", modLoc("block/" + path)).renderType("cutout");

        simpleBlockWithItem(blockRegistryObject.get(), models().getBuilder(path));
    }

    private void generatePedestalBlock(DeferredBlock<Block> blockRegistryObject) {
        String path = blockRegistryObject.getId().getPath();
        models().withExistingParent(path, modLoc("block/pedestal_model"))
                .texture("all", modLoc("block/" + path))
                .texture("particle", modLoc("block/" + path)).renderType("cutout");

        simpleBlockWithItem(blockRegistryObject.get(), models().getBuilder(path));
    }

    private void generatePillarBlock(DeferredBlock<Block> blockRegistryObject) {
        String path = blockRegistryObject.getId().getPath();

        this.logBlock((RotatedPillarBlock) blockRegistryObject.get());

        itemModels().simpleBlockItem(blockRegistryObject.get());

        //simpleBlockWithItem(blockRegistryObject.get(), models().getBuilder(path));
    }


    private void blockWithItem(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void fluidBlocks(String name, Block block) {
        simpleBlock(block, models().getBuilder(name).texture("particle", ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "block/" + name + "_still")));
    }


    @Override
    public String getName() {
        return StarForge.MOD_ID + " Block States";
    }
}
