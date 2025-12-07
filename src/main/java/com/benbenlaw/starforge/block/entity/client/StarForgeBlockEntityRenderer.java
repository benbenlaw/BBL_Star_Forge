package com.benbenlaw.starforge.block.entity.client;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.benbenlaw.starforge.particle.SFParticles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Random;

public class StarForgeBlockEntityRenderer implements BlockEntityRenderer<StarForgeBlockEntity> {

    public static final ResourceLocation BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "textures/entity/star_beam.png");
    private final Random random = new Random();
    float beamWidth = 0.05f;

    public StarForgeBlockEntityRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(StarForgeBlockEntity entity, float tick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack inputStack = entity.getItemStackHandler().getStackInSlot(0);
        ItemStack outputStack = entity.getItemStackHandler().getStackInSlot(1);

        if (!outputStack.isEmpty() || !inputStack.isEmpty()) {
            ItemStack stack = !outputStack.isEmpty() ? outputStack : inputStack;

            poseStack.pushPose();
            poseStack.translate(0.5, 1.2, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);

            long time = entity.getLevel().getGameTime();
            float rotation = (time % 360) + tick;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            BakedModel model = itemRenderer.getModel(stack, null, null, 0);
            itemRenderer.render(stack, ItemDisplayContext.GROUND, true, poseStack, bufferSource,
                    getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos().above()),
                    OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }

        BlockPos startPos = entity.getBlockPos().above(4);
        BlockState blockState = entity.getLevel().getBlockState(startPos);
        int color = blockState.getBlock() instanceof StarBlock starBlock ? starBlock.getColor() : 0xFFFFFF;

        if (entity.isCharging) {
            long gameTime = entity.getLevel().getGameTime();
            renderDownwardBeam(poseStack, bufferSource, gameTime, 4, beamWidth, color);
        }

        if (entity.isCrafting()) {
            long gameTime = entity.getLevel().getGameTime();

            Vec3 itemPos = new Vec3(0.5, 1.2, 0.5);

            BlockState state = entity.getBlockState();
            BlockPos pos = entity.getBlockPos();
            var aabb = state.getShape(entity.getLevel(), pos).bounds();
            if (aabb == null || aabb.getYsize() <= 0) aabb = new net.minecraft.world.phys.AABB(0, 0, 0, 1, 1, 1);

            double minX = aabb.minX + 0.8, maxX = aabb.maxX - 0.8;
            double minZ = aabb.minZ + 0.8, maxZ = aabb.maxZ - 0.8;
            double maxY = aabb.maxY;

            Vec3[] corners = new Vec3[] {
                    new Vec3(minX, maxY, minZ),
                    new Vec3(maxX, maxY, minZ),
                    new Vec3(maxX, maxY, maxZ),
                    new Vec3(minX, maxY, maxZ)
            };


            for (Vec3 corner : corners) {
                renderCornerBeam(poseStack, bufferSource, gameTime, corner, itemPos.add(0, 0.15, 0), beamWidth, color);
            }


            itemPos = new Vec3(
                    pos.getX() + 0.5,
                    pos.getY() + 1.2,
                    pos.getZ() + 0.5
            );
            renderCraftingOrbitParticles(entity.getLevel(), itemPos);

        } else {
            removeCraftingParticles(entity);
        }
    }

    private void removeCraftingParticles(StarForgeBlockEntity entity) {
        if (entity.getLevel() == null) return;

        Level level = entity.getLevel();
        BlockPos pos = entity.getBlockPos();
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.2;
        double centerZ = pos.getZ() + 0.5;
        double radius = 0.5;

        level.getEntitiesOfClass(net.minecraft.world.entity.Entity.class,
                new net.minecraft.world.phys.AABB(
                        centerX - radius, centerY - radius, centerZ - radius,
                        centerX + radius, centerY + radius, centerZ + radius),
                e -> e instanceof net.minecraft.world.entity.item.ItemEntity
        ).forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
    }

    private void renderCornerBeam(PoseStack poseStack, MultiBufferSource buffer, long gameTime, Vec3 start, Vec3 end, float beamWidth, int color) {
        poseStack.pushPose();
        Vec3 dir = end.subtract(start);
        float length = (float) dir.length();
        Vec3 norm = dir.normalize();

        poseStack.translate(start.x, start.y, start.z);

        float yaw = (float) Math.atan2(norm.x, norm.z);
        float pitch = (float) -Math.asin(norm.y);

        poseStack.mulPose(Axis.YP.rotation(yaw));
        poseStack.mulPose(Axis.XP.rotation(pitch));

        VertexConsumer consumer = buffer.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, false));
        float scroll = (gameTime % 40) * -0.05f;
        float half = beamWidth / 2f;

        float[][] corners = {
                {-half, -half}, { half, -half}, { half, half}, {-half, half}
        };

        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;
            float x1 = corners[i][0], y1 = corners[i][1], x2 = corners[next][0], y2 = corners[next][1];
            float u0 = 0f, u1 = 1f, v0 = scroll, v1 = scroll + length;
            float nx = y2 - y1, ny = -(x2 - x1), nz = 0;

            consumer.addVertex(poseStack.last().pose(), x1, y1, 0).setColor(color).setUv(u0, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), x2, y2, 0).setColor(color).setUv(u1, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), x2, y2, length).setColor(color).setUv(u1, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), x1, y1, length).setColor(color).setUv(u0, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
        }

        poseStack.popPose();
    }

    private void renderCraftingOrbitParticles(Level level, Vec3 itemPos) {
        if (level == null) return;
        if (Minecraft.getInstance().isPaused()) return;

        int particleCount = 3;
        double radiusMin = 0.15;
        double radiusMax = 0.25;
        double orbitSpeed = 0.02;

        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = radiusMin + random.nextDouble() * (radiusMax - radiusMin);

            // Horizontal orbit
            double xH = itemPos.x + Math.cos(angle) * radius;
            double zH = itemPos.z + Math.sin(angle) * radius;
            double yH = itemPos.y + 0.05 + random.nextDouble() * 0.05;
            double vxH = -Math.sin(angle) * orbitSpeed;
            double vzH = Math.cos(angle) * orbitSpeed;
            double vyH = 0;
            level.addParticle(SFParticles.MOVING.get(), xH, yH, zH, vxH, vyH, vzH);

            // Vertical orbit
            double xV = itemPos.x;
            double yV = itemPos.y + Math.cos(angle) * radius + 0.08;
            double zV = itemPos.z + Math.sin(angle) * radius;
            double vxV = 0;
            double vyV = -Math.sin(angle) * orbitSpeed;
            double vzV = Math.cos(angle) * orbitSpeed;
            level.addParticle(SFParticles.MOVING.get(), xV, yV, zV, vxV, vyV, vzV);
        }
    }

    private void renderDownwardBeam(PoseStack poseStack, MultiBufferSource buffer, long gameTime, int height, float beamWidth, int color) {
        poseStack.pushPose();
        poseStack.translate(0.5, height + 0.5, 0.5);
        float halfWidth = beamWidth / 2f;
        float scroll = (gameTime % 40L) * -0.05f;
        float rotation = (gameTime % 360L) * 2f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        VertexConsumer consumer = buffer.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, false));

        // Corners
        float[][] corners = {
                {-halfWidth, -halfWidth},
                { halfWidth, -halfWidth},
                { halfWidth,  halfWidth},
                {-halfWidth,  halfWidth}
        };

        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;

            float[] c1 = corners[i];
            float[] c2 = corners[next];
            float[][] verts = {
                    {c1[0], 0, c1[1]},
                    {c2[0], 0, c2[1]},
                    {c2[0], -height, c2[1]},
                    {c1[0], -height, c1[1]}
            };

            float u0 = 0f, u1 = 1f;
            float v0 = scroll, v1 = scroll + height;

            float nx = c2[1] - c1[1];
            float ny = 0f;
            float nz = c1[0] - c2[0];

            consumer.addVertex(poseStack.last().pose(), verts[0][0], verts[0][1], verts[0][2]).setColor(color).setUv(u0, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), verts[1][0], verts[1][1], verts[1][2]).setColor(color).setUv(u1, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), verts[2][0], verts[2][1], verts[2][2]).setColor(color).setUv(u1, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
            consumer.addVertex(poseStack.last().pose(), verts[3][0], verts[3][1], verts[3][2]).setColor(color).setUv(u0, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(nx, ny, nz);
        }

        poseStack.popPose();
    }

    public boolean shouldRenderOffScreen(StarForgeBlockEntity p_112138_) { return true; }

    @Override
    public int getViewDistance() { return 256; }

    public boolean shouldRender(StarForgeBlockEntity entity, Vec3 vec3) {
        return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0,0.0,1.0)
                .closerThan(vec3.multiply(1.0,0.0,1.0),(double)this.getViewDistance());
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(StarForgeBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new net.minecraft.world.phys.AABB(pos.getX(),pos.getY(),pos.getZ(),pos.getX()+1.0,1024,pos.getZ()+1.0);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
