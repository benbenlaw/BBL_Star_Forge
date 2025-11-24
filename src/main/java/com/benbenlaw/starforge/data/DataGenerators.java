package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = StarForge.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new SFRecipes(packOutput, event.getLookupProvider()));

        //generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
        //        List.of(new LootTableProvider.SubProviderEntry(StrainersLootTableProvider::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));

        SFBlockTags blockTags = new SFBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);

        SFItemTags itemTags = new SFItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), itemTags);

        generator.addProvider(event.includeClient(), new SFItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new SFBlockStatesProvider(packOutput, event.getExistingFileHelper()));


    }


}