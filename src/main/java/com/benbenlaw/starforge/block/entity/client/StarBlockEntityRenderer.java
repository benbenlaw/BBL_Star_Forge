package com.benbenlaw.starforge.block.entity.client;

import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.block.entity.StarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class StarBlockEntityRenderer implements BlockEntityRenderer<StarBlockEntity> {

    public StarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (entity.getLevel() == null) return;

        var time = entity.getLevel().getGameTime() + tickDelta;

        // Smooth rotations
        float rotationY = (time * 1.2f) % 360; // horizontal spin
        float rotationX = (float) Math.sin(time * 0.02) * 5; // gentle vertical tilt
        float rotationZ = (float) Math.sin(time * 0.01) * 10; // vertical spin

        matrices.pushPose();

        // Center the star
        matrices.translate(0.5f, 0.5f, 0.5f);

        // Apply rotations on multiple axes
        matrices.mulPose(Axis.YP.rotationDegrees(rotationY));
        matrices.mulPose(Axis.XP.rotationDegrees(rotationX));
        matrices.mulPose(Axis.ZP.rotationDegrees(rotationZ));

        // Move back to block origin
        matrices.translate(-0.5f, -0.5f, -0.5f);

        // Render the MINI_STAR
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
    }




}