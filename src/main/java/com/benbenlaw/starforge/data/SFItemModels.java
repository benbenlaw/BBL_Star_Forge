package com.benbenlaw.starforge.data;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.item.SFItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class SFItemModels extends ItemModelProvider {

    public SFItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, StarForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(SFItems.STAR_INGOT);
        simpleItem(SFItems.STAR_GEM);


    }

    private void simpleItem(DeferredItem<Item> item) {
        withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "item/" + item.getId().getPath()));
    }
}