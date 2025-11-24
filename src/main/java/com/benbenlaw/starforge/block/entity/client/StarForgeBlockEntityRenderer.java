package com.benbenlaw.starforge.block.entity.client;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.block.entity.StarForgeBlockEntity;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class StarForgeBlockEntityRenderer implements BlockEntityRenderer<StarForgeBlockEntity> {

    public static final ResourceLocation BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "textures/entity/star_beam.png");


    public StarForgeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarForgeBlockEntity entity, float tick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack inputStack = entity.getItemStackHandler().getStackInSlot(0);
        ItemStack outputStack = entity.getItemStackHandler().getStackInSlot(1);

        if (!outputStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.2, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);

            long time = entity.getLevel().getGameTime();
            float rotation = (time % 360) + tick;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            BakedModel model = itemRenderer.getModel(outputStack, null, null, 0);
            itemRenderer.render(outputStack, ItemDisplayContext.GROUND, true, poseStack, bufferSource,
                    getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos().above()), OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }

        else if (!inputStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.2, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);

            long time = entity.getLevel().getGameTime();
            float rotation = (time % 360) + tick;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            BakedModel model = itemRenderer.getModel(inputStack, null, null, 0);
            itemRenderer.render(inputStack, ItemDisplayContext.GROUND, true, poseStack, bufferSource,
                    getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos().above()), OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }

        if (entity.isCharging) {
            long gameTime = entity.getLevel().getGameTime();
            BlockPos startPos = entity.getBlockPos().above(4);
            BlockState blockState = entity.getLevel().getBlockState(startPos);
            int color;
            if (blockState.getBlock() instanceof StarBlock starBlock) {
                color = starBlock.getColor();
            } else {
                color = 0xFFFFFF;
            }

            renderDownwardBeam(poseStack, bufferSource, gameTime, 4, 0.05f, color);
        }


    }

    private void renderDownwardBeam(PoseStack poseStack, MultiBufferSource buffer, long gameTime, int height, float beamWidth, int color) {
        poseStack.pushPose();
        poseStack.translate(0.5, height, 0.5); // center on the block top

        float halfWidth = beamWidth / 2f;

        // Texture scrolling offset (slower)
        float f = (float)Math.floorMod(gameTime, 40L);
        float f1 = -f; // negative because we go downward
        float scrollSpeed = 0.05f;
        float offset = (f1 * scrollSpeed) % 1f;

        // Optional rotation
        float rotation = (gameTime % 360L) * 2f;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        VertexConsumer consumer = buffer.getBuffer(RenderType.beaconBeam(BeaconRenderer.BEAM_LOCATION, false));

        float[][] corners = {
                {-halfWidth, 0, -halfWidth},
                { halfWidth, 0, -halfWidth},
                { halfWidth, 0,  halfWidth},
                {-halfWidth, 0,  halfWidth}
        };

        float topY = -height;
        float bottomY = 0;

        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;
            float[] c1 = corners[i];
            float[] c2 = corners[next];

            float[][] verts = {
                    {c1[0], bottomY, c1[2]},
                    {c2[0], bottomY, c2[2]},
                    {c2[0], topY,    c2[2]},
                    {c1[0], topY,    c1[2]}
            };

            float u0 = 0f, u1 = 1f;
            float v0 = offset;
            float v1 = offset + height;

            float nx = (c1[2] - c2[2]);
            float ny = 0f;
            float nz = (c2[0] - c1[0]);

            for (int j = 0; j < 4; j++) {
                float[] v = verts[j];
                float u = (j == 0 || j == 3) ? u0 : u1;
                float vTex = (j < 2) ? v0 : v1;

                consumer.addVertex(poseStack.last().pose(), v[0], v[1], v[2])
                        .setColor(color)
                        .setUv(u, vTex)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(15728880)
                        .setNormal(nx, ny, nz);
            }
        }

        poseStack.popPose();
    }


    public boolean shouldRenderOffScreen(StarForgeBlockEntity p_112138_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(StarForgeBlockEntity p_173531_, Vec3 p_173532_) {
        return Vec3.atCenterOf(p_173531_.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(p_173532_.multiply(1.0, 0.0, 1.0), (double)this.getViewDistance());
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(StarForgeBlockEntity blockEntity) {
        net.minecraft.core.BlockPos pos = blockEntity.getBlockPos();
        return new net.minecraft.world.phys.AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, 1024, pos.getZ() + 1.0);
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
