package com.benbenlaw.starforge.block;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.custom.CapBlock;
import com.benbenlaw.starforge.block.custom.PedestalBlock;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.block.custom.StarForgeBlock;
import com.benbenlaw.starforge.item.SFItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SFBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(StarForge.MOD_ID);


    //Stars in order of power
    public static final DeferredBlock<Block> BLUE_STAR = registerBlock("blue_star",
            () -> new StarBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .lightLevel(state -> 15), 1, 4000,0x0505E3));

    public static final DeferredBlock<Block> WHITE_STAR = registerBlock("white_star",
            () -> new StarBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .lightLevel(state -> 15), 2, 8000, 0xDFDFDF));

    public static final DeferredBlock<Block> YELLOW_STAR = registerBlock("yellow_star",
            () -> new StarBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .lightLevel(state -> 15), 4, 16000, 0xE3CD05));

    public static final DeferredBlock<Block> ORANGE_STAR = registerBlock("orange_star",
            () -> new StarBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .lightLevel(state -> 15), 8, 32000, 0xE37105));

    public static final DeferredBlock<Block> RED_STAR = registerBlock("red_star",
            () -> new StarBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .lightLevel(state -> 15), 20, 128000, 0xE30505));

    //Forge Blocks
    public static final DeferredBlock<Block> STAR_FORGE = registerBlock("star_forge",
            () -> new StarForgeBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> PEDESTAL = registerBlock("pedestal",
            () -> new PedestalBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    //Decorative Blocks
    public static final DeferredBlock<Block> COSMIC_PLANKS = registerBlock("cosmic_planks",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final DeferredBlock<Block> COSMIC_PILLAR = registerBlock("cosmic_pillar",
            () -> new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    //Caps
    public static final DeferredBlock<Block> TIER_1_STAR_FORGE_CAP = registerBlock("tier_1_star_forge_cap",
            () -> new CapBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> TIER_2_STAR_FORGE_CAP = registerBlock("tier_2_star_forge_cap",
            () -> new CapBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> TIER_3_STAR_FORGE_CAP = registerBlock("tier_3_star_forge_cap",
            () -> new CapBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> TIER_4_STAR_FORGE_CAP = registerBlock("tier_4_star_forge_cap",
            () -> new CapBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> TIER_5_STAR_FORGE_CAP = registerBlock("tier_5_star_forge_cap",
            () -> new CapBlock(Block.Properties.ofFullCopy(Blocks.IRON_BLOCK)));





    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = (DeferredBlock<T>) BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        SFItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));

    }
}
