package com.benbenlaw.starforge.block;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.block.entity.PedestalBlockEntity;
import com.benbenlaw.starforge.block.entity.StarBlockEntity;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.benbenlaw.starforge.item.SFItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SFBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, StarForge.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StarForgeBlockEntity>> STAR_FORGE_BLOCK_ENTITY =
            register("star_forge_block_entity", () ->
                    BlockEntityType.Builder.of(StarForgeBlockEntity::new, SFBlocks.STAR_FORGE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PedestalBlockEntity>> PEDESTAL_BLOCK_ENTITY =
            register("pedestal_block_entity", () ->
                    BlockEntityType.Builder.of(PedestalBlockEntity::new, SFBlocks.PEDESTAL.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StarBlockEntity>> STAR_BLOCK_ENTITY =
            register("star_block_entity", () ->
                    BlockEntityType.Builder.of(StarBlockEntity::new,
                            SFBlocks.BLUE_STAR.get(),
                            SFBlocks.WHITE_STAR.get(),
                            SFBlocks.YELLOW_STAR.get(),
                            SFBlocks.ORANGE_STAR.get(),
                            SFBlocks.RED_STAR.get()
                    ));


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SFBlockEntities.STAR_FORGE_BLOCK_ENTITY.get(), StarForgeBlockEntity::getItemHandlerCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SFBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntity::getItemHandlerCapability);
    }


    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(@Nonnull String name, @Nonnull Supplier<BlockEntityType.Builder<T>> initializer) {
        return BLOCK_ENTITIES.register(name, () -> initializer.get().build(null));
    }
}
