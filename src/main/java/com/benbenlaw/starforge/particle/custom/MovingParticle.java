package com.benbenlaw.starforge.particle.custom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingParticle extends SimpleAnimatedParticle {


    private Vec3 orbitCenter;
    private double radius;
    private double angle;
    private double orbitSpeed;
    private boolean verticalOrbit;

    protected MovingParticle(ClientLevel level, double x, double y, double z,
                             SpriteSet sprites,
                             Vec3 orbitCenter, double radius, double initialAngle, double orbitSpeed, boolean verticalOrbit) {
        super(level, x, y, z, sprites, 0.0125F);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize *= 0.25F;
        this.lifetime = 100;
        this.setFadeColor(15916745);
        this.setSpriteFromAge(sprites);

        this.orbitCenter = orbitCenter;
        this.radius = radius;
        this.angle = initialAngle;
        this.orbitSpeed = orbitSpeed;
        this.verticalOrbit = verticalOrbit;
    }

    @Override
    public void tick() {
        super.tick();

        angle += orbitSpeed;

        if (verticalOrbit) {
            this.x = orbitCenter.x;
            this.y = orbitCenter.y + Math.cos(angle) * radius;
            this.z = orbitCenter.z + Math.sin(angle) * radius;
        } else {
            this.x = orbitCenter.x + Math.cos(angle) * radius;
            this.y = orbitCenter.y;
            this.z = orbitCenter.z + Math.sin(angle) * radius;
        }
    }


    @Override public void move(double p_106550_, double p_106551_, double p_106552_) {
        this.setBoundingBox(this.getBoundingBox().move(p_106550_, p_106551_, p_106552_));
        this.setLocationFromBoundingbox();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {

            Vec3 center = new Vec3(x, y, z); // center of orbit
            double radius = 0.3;
            double initialAngle = Math.random() * Math.PI * 2;
            double orbitSpeed = 0.05;
            boolean verticalOrbit = false; // horizontal orbit

            return new MovingParticle(level, x, y, z, sprites, center, radius, initialAngle, orbitSpeed, verticalOrbit);
        }
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}