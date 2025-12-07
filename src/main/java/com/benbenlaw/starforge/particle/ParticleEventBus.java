package com.benbenlaw.starforge.particle;

import com.benbenlaw.starforge.particle.custom.PedestalParticle;
import com.benbenlaw.starforge.particle.custom.MovingParticle;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ParticleEventBus {

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {

        Minecraft.getInstance().particleEngine.register(SFParticles.PEDESTAL_ITEM.get(),
                PedestalParticle.Provider::new);

        Minecraft.getInstance().particleEngine.register(SFParticles.MOVING.get(),
                MovingParticle.Provider::new);



    }
}
