package com.benbenlaw.starforge;

import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.config.SFConfig;
import com.benbenlaw.starforge.item.SFCreativeTab;
import com.benbenlaw.starforge.item.SFItems;
import com.benbenlaw.starforge.particle.SFParticles;
import com.benbenlaw.starforge.recipe.SFRecipes;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(StarForge.MOD_ID)
public class StarForge{
    public static final String MOD_ID = "starforge";
    public static final Logger LOGGER = LogManager.getLogger();

    public StarForge(final IEventBus eventBus, final ModContainer modContainer) {

        SFItems.ITEMS.register(eventBus);
        SFBlocks.BLOCKS.register(eventBus);
        SFBlockEntities.BLOCK_ENTITIES.register(eventBus);
        SFRecipes.SERIALIZER.register(eventBus);
        SFRecipes.TYPES.register(eventBus);
        SFCreativeTab.CREATIVE_MODE_TABS.register(eventBus);
        SFParticles.PARTICLE_TYPES.register(eventBus);

        eventBus.addListener(this::registerCapabilities);

        modContainer.registerConfig(ModConfig.Type.STARTUP, SFConfig.SPEC, "bbl/starforge/starforge.toml");

    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        SFBlockEntities.registerCapabilities(event);
    }
}
