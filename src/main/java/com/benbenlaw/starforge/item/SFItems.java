package com.benbenlaw.starforge.item;

import com.benbenlaw.starforge.StarForge;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SFItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StarForge.MOD_ID);

    public static final DeferredItem<Item> STAR_INGOT = ITEMS.register("star_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STAR_GEM = ITEMS.register("star_gem",
            () -> new Item(new Item.Properties()));
}
