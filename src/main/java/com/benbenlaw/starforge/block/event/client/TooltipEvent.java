package com.benbenlaw.starforge.block.event.client;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.config.SFConfig;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = StarForge.MOD_ID ,value = Dist.CLIENT)
public class TooltipEvent {


    @SubscribeEvent
    public static void addCapInformation(ItemTooltipEvent event) {

        ItemStack itemStack = event.getItemStack();
        int capacity;

        if (itemStack.is(SFTags.Items.STAR_FORGE_TIER_1_CAP)) {
            capacity = SFConfig.forgeCapacityCap1.get();
        }
        else if (itemStack.is(SFTags.Items.STAR_FORGE_TIER_2_CAP)) {
            capacity = SFConfig.forgeCapacityCap2.get();
        }
        else if (itemStack.is(SFTags.Items.STAR_FORGE_TIER_3_CAP)) {
            capacity = SFConfig.forgeCapacityCap3.get();
        }
        else if (itemStack.is(SFTags.Items.STAR_FORGE_TIER_4_CAP)) {
            capacity = SFConfig.forgeCapacityCap4.get();
        }
        else if (itemStack.is(SFTags.Items.STAR_FORGE_TIER_5_CAP)) {
            capacity = SFConfig.forgeCapacityCap5.get();
        }
        else {
            return ;
        }

        if (Screen.hasShiftDown()) {
            event.getToolTip().add(Component.literal("Allows the Star Forge to storge : " + capacity  + " Star Power").withStyle(ChatFormatting.YELLOW));
        }

        else {
            event.getToolTip().add(Component.translatable("tooltips.bblcore.shift").withStyle(ChatFormatting.YELLOW));
        }

    }
}
