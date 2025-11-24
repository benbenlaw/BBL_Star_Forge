package com.benbenlaw.starforge.block.event.client;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.entity.PedestalBlockEntity;
import com.benbenlaw.starforge.block.entity.client.PedestalBlockEntityRenderer;
import com.benbenlaw.starforge.block.entity.client.StarBlockEntityRenderer;
import com.benbenlaw.starforge.block.entity.client.StarForgeBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = StarForge.MOD_ID, value = Dist.CLIENT)
public class RenderersEvent {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SFBlockEntities.STAR_FORGE_BLOCK_ENTITY.get(), StarForgeBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SFBlockEntities.STAR_BLOCK_ENTITY.get(), StarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SFBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntityRenderer::new);
    }
}
