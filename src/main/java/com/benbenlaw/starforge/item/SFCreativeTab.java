package com.benbenlaw.starforge.item;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SFCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StarForge.MOD_ID);

    public static final Supplier<CreativeModeTab> STRAINERS_TAB = CREATIVE_MODE_TABS.register("strainers", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> SFBlocks.BLUE_STAR.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.starforge"))
            .displayItems((parameters, output) -> {
            }).displayItems(SFItems.ITEMS.getEntries()).build()
    );

}

