package com.benbenlaw.starforge.block.entity.client;

import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.block.entity.StarBlockEntity;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.benbenlaw.starforge.particle.SFParticles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class StarBlockEntityRenderer implements BlockEntityRenderer<StarBlockEntity> {

    private final Random random = new Random();

    public StarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (entity.getLevel() == null) return;

        var time = entity.getLevel().getGameTime() + tickDelta;

        float rotationY = (time * 1.2f) % 360;
        float rotationX = (float) Math.sin(time * 0.02) * 5;
        float rotationZ = (float) Math.sin(time * 0.01) * 10;

        matrices.pushPose();

        matrices.translate(0.5f, 0.5f, 0.5f);

        matrices.mulPose(Axis.YP.rotationDegrees(rotationY));
        matrices.mulPose(Axis.XP.rotationDegrees(rotationX));
        matrices.mulPose(Axis.ZP.rotationDegrees(rotationZ));

        matrices.translate(-0.5f, -0.5f, -0.5f);

        Minecraft.getInstance().getBlockRenderer().renderBatched(
                entity.getBlockState(),
                entity.getBlockPos(),
                entity.getLevel(),
                matrices,
                vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(entity.getBlockState())),
                true,
                entity.getLevel().random
        );

        matrices.popPose();

        spawnOrbitParticles(entity.getLevel(), new Vec3(
                entity.getBlockPos().getX() + 0.5,
                entity.getBlockPos().getY() + 0.5,
                entity.getBlockPos().getZ() + 0.5
        ));

    }


    private final double[] orbitAngleH = new double[20];
    private final double[] orbitAngleV = new double[20];


    private void spawnOrbitParticles(Level level, Vec3 itemPos) {
        if (level == null) return;
        if (Minecraft.getInstance().isPaused()) return;

        int particleCount = 1;
        double radiusMin = 0.5;
        double radiusMax = 2.0;
        double orbitSpeed = 0.02;
        double heightVariation = 0.3;

        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = radiusMin + random.nextDouble() * (radiusMax - radiusMin);

            double xH = itemPos.x + Math.cos(angle) * radius;
            double zH = itemPos.z + Math.sin(angle) * radius;
            double yH = itemPos.y - 0.1 + random.nextDouble() * heightVariation;
            double vxH = -Math.sin(angle) * orbitSpeed;
            double vzH = Math.cos(angle) * orbitSpeed;
            double vyH = 0;
            level.addParticle(SFParticles.PEDESTAL_ITEM.get(), xH, yH, zH, vxH, vyH, vzH);
        }
    }

    public boolean shouldRenderOffScreen(StarBlockEntity p_112138_) { return true; }

    @Override
    public int getViewDistance() { return 256; }

    public boolean shouldRender(StarBlockEntity entity, Vec3 vec3) {
        return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0,0.0,1.0)
                .closerThan(vec3.multiply(1.0,0.0,1.0),(double)this.getViewDistance());
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(StarBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new net.minecraft.world.phys.AABB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1.0,1024,pos.getZ()+1.0);
    }

}