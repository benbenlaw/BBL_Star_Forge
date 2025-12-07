package com.benbenlaw.starforge.block.entity.client;

import com.benbenlaw.starforge.block.entity.PedestalBlockEntity;
import com.benbenlaw.starforge.block.entity.StarBlockEntity;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.benbenlaw.starforge.particle.SFParticles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    private final Random random = new Random();

    public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PedestalBlockEntity entity, float tick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getItemStackHandler().getStackInSlot(0);

        if (stack.isEmpty()) return;

        poseStack.pushPose();

        Level level = entity.getLevel();
        if (level == null) {
            poseStack.popPose();
            return;
        }

        long time = level.getGameTime();
        float rotationBase = (time % 360) + tick;

        StarForgeBlockEntity forge = entity.findNearbyForge();
        float moveProgress = 0f;

        if (forge != null && forge.isCrafting() && forge.getMaxProgress() > 0) {
            if (forge.getActivePedestalPositions().contains(entity.getBlockPos())) {
                moveProgress = (float) forge.getProgress() / forge.getMaxProgress();
                moveProgress = Math.min(1f, Math.max(0f, moveProgress));
            }
        }

        Vec3 blended = getVec3(entity, forge, moveProgress);
        poseStack.translate(
                blended.x - entity.getBlockPos().getX(),
                blended.y - entity.getBlockPos().getY(),
                blended.z - entity.getBlockPos().getZ()
        );

        float spinMultiplier = 1f + moveProgress * 20f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationBase * spinMultiplier));

        float scale = 1f - (float) Math.pow(moveProgress, 1.5) * 0.8f;
        poseStack.scale(scale, scale, scale);

        BakedModel model = itemRenderer.getModel(stack, level, null, 0);
        itemRenderer.render(
                stack,
                ItemDisplayContext.GROUND,
                true,
                poseStack,
                bufferSource,
                getLightLevel(level, entity.getBlockPos().above()),
                OverlayTexture.NO_OVERLAY,
                model
        );

        if (forge != null && forge.isCrafting()) {
            renderPedestalParticles(level, blended);
        }

        poseStack.popPose();
    }

    private void renderPedestalParticles(Level level, Vec3 itemPos) {
        if (level == null) return;
        if (Minecraft.getInstance().isPaused()) return;

        double angle = random.nextDouble() * Math.PI * 2;
        double radius = 0.2 + random.nextDouble() * 0.1;
        double speed = 0.02;

        double x = itemPos.x + Math.cos(angle) * radius;
        double y = itemPos.y + 0.05 + random.nextDouble() * 0.1;
        double z = itemPos.z + Math.sin(angle) * radius;

        double vx = -Math.sin(angle) * speed;
        double vz = Math.cos(angle) * speed;
        double vy = 0;

        level.addParticle(SFParticles.PEDESTAL_ITEM.get(), x, y, z, vx, vy, vz);
    }

    private static @NotNull Vec3 getVec3(PedestalBlockEntity entity, StarForgeBlockEntity forge, float moveProgress) {
        Vec3 start = new Vec3(
                entity.getBlockPos().getX() + 0.5,
                entity.getBlockPos().getY() + 1.2,
                entity.getBlockPos().getZ() + 0.5
        );

        Vec3 end = start;

        if (forge != null) {
            end = new Vec3(
                    forge.getBlockPos().getX() + 0.5,
                    forge.getBlockPos().getY() + 1.2,
                    forge.getBlockPos().getZ() + 0.5
            );
        }

        return start.lerp(end, moveProgress);
    }

    public boolean shouldRenderOffScreen(PedestalBlockEntity p_112138_) { return true; }

    @Override
    public int getViewDistance() { return 256; }

    public boolean shouldRender(PedestalBlockEntity entity, Vec3 vec3) {
        return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0,0.0,1.0)
                .closerThan(vec3.multiply(1.0,0.0,1.0),(double)this.getViewDistance());
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(PedestalBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new net.minecraft.world.phys.AABB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1.0,1024,pos.getZ()+1.0);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
