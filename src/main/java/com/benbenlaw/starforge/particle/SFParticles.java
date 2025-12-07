package com.benbenlaw.starforge.particle;

import com.benbenlaw.starforge.StarForge;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SFParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, StarForge.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PEDESTAL_ITEM =
            PARTICLE_TYPES.register("pedestal_item", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOVING =
            PARTICLE_TYPES.register("moving", () -> new SimpleParticleType(true));
}
