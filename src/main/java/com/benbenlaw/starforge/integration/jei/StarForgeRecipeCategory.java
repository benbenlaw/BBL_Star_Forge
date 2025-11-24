package com.benbenlaw.starforge.integration.jei;

import com.benbenlaw.starforge.StarForge;
import com.benbenlaw.starforge.block.SFBlocks;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import com.benbenlaw.starforge.util.SFTags;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IScrollGridWidgetFactory;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@SuppressWarnings("removal")

public class StarForgeRecipeCategory implements IRecipeCategory<StarForgeRecipe> {

    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "star_forge");
    public final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(StarForge.MOD_ID, "textures/gui/jei_star_forge.png");

    public static final RecipeType<StarForgeRecipe> RECIPE_TYPE = RecipeType.create(StarForge.MOD_ID, "star_forge", StarForgeRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    @SuppressWarnings("removal")
    private final IScrollGridWidgetFactory<?> scrollGridWidgetFactory;

    @SuppressWarnings("removal")
    public StarForgeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 100);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(SFBlocks.STAR_FORGE.get()));
        this.scrollGridWidgetFactory = helper.createScrollGridFactory(5, 2);
        this.scrollGridWidgetFactory.setPosition(34, 0);
    }

    @Override
    public RecipeType<StarForgeRecipe> getRecipeType() {
        return JEISFPlugin.STAR_FORGE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Star Forge");
    }

    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public @Nullable ResourceLocation getRegistryName(StarForgeRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(StarForgeRecipe.Type.INSTANCE).stream()
                .filter(recipeHolder -> recipeHolder.value().equals(recipe))
                .map(RecipeHolder::id)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("removal")
    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, StarForgeRecipe recipe, IFocusGroup iFocusGroup) {

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.input());

        if (recipe.extraIngredients().isPresent()) {
            for (var extraInputs : recipe.extraIngredients().get()) {
                iRecipeLayoutBuilder.addSlotToWidget(RecipeIngredientRole.INPUT, this.scrollGridWidgetFactory)
                        .addIngredients(extraInputs)
                        .addRichTooltipCallback((slotView, tooltip) -> {
                            tooltip.add(Component.literal("On a Pedestal").withStyle(ChatFormatting.GOLD));
                        });
            }
        }

        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 117, 76).addItemStack(recipe.result());


    }

    @Override
    public void draw(StarForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();

        RenderSystem.enableDepthTest();

        poseStack.pushPose();

        poseStack.translate(39, 77, 50);

        float scale = 9f;
        poseStack.scale(scale, -scale, scale);

        float centerX = 0f;
        float centerY = 0.5f;
        float centerZ = 0f;
        poseStack.translate(-centerX, -centerY, -centerZ);

        poseStack.mulPose(Axis.XP.rotationDegrees(17f));
        poseStack.mulPose(Axis.YP.rotationDegrees(20f));

        renderStructure(poseStack, mc, recipe);

        poseStack.popPose();
        RenderSystem.disableDepthTest();
    }

    private void renderStructure(PoseStack poseStack, Minecraft mc, StarForgeRecipe recipe) {
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        if (recipe.tier() != -1) {
            // Base
            TagKey<Block> baseTag = SFTags.Blocks.STAR_FORGE_BASE_BLOCKS;
            List<Holder<Block>> baseBlocks = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTagOrEmpty(baseTag).forEach(baseBlocks::add);

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && z == 0) continue;
                    if (!baseBlocks.isEmpty()) {
                        long index = (mc.level.getGameTime() / 20 + x + z) % baseBlocks.size();
                        BlockState state = baseBlocks.get((int) index).value().defaultBlockState();
                        renderBlock(state, poseStack, dispatcher, buffer, x, -1, z);
                    }
                }
            }

            // Pillars
            TagKey<Block> pillarTag = SFTags.Blocks.STAR_FORGE_PILLAR_BLOCKS;
            List<Holder<Block>> pillarBlocks = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTagOrEmpty(pillarTag).forEach(pillarBlocks::add);

            int[] xs = {-3, 3};
            int[] zs = {-3, 3};
            for (int x : xs) {
                for (int z : zs) {
                    for (int y = -1; y < 3; y++) {
                        if (!pillarBlocks.isEmpty()) {
                            long index = (mc.level.getGameTime() / 20 + x + z + y) % pillarBlocks.size();
                            BlockState state = pillarBlocks.get((int) index).value().defaultBlockState();
                            renderBlock(state, poseStack, dispatcher, buffer, x, y, z);
                        }
                    }
                }
            }

            // Caps
            TagKey<Block> capTag = getTierTag(recipe.tier());
            if (capTag != null) {
                var iterator = BuiltInRegistries.BLOCK.getTagOrEmpty(capTag).iterator();
                if (iterator.hasNext()) {
                    Holder<Block> firstBlock = iterator.next();
                    BlockState state = firstBlock.value().defaultBlockState();
                    for (int x : xs) {
                        for (int z : zs) {
                            renderBlock(state, poseStack, dispatcher, buffer, x, 3, z);
                        }
                    }
                }
            }
        }

        // Center Star Forge block
        renderBlock(SFBlocks.STAR_FORGE.get().defaultBlockState(), poseStack, dispatcher, buffer, 0, 0, 0);

        buffer.endBatch();
    }

    public TagKey<Block> getTierTag(int tier) {
        return switch (tier) {
            case 1 -> SFTags.Blocks.STAR_FORGE_TIER_1_CAP;
            case 2 -> SFTags.Blocks.STAR_FORGE_TIER_2_CAP;
            case 3 -> SFTags.Blocks.STAR_FORGE_TIER_3_CAP;
            case 4 -> SFTags.Blocks.STAR_FORGE_TIER_4_CAP;
            case 5 -> SFTags.Blocks.STAR_FORGE_TIER_5_CAP;
            default -> null;
        };
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, StarForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        double left = 1;
        double top = 50;
        double right = 115;
        double bottom = 100;

        if (mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom) {

            tooltip.add(Component.literal("Tier " + recipe.tier() + " Star Forge").withStyle(net.minecraft.ChatFormatting.GOLD));
            tooltip.add(Component.literal("Star Power: " + recipe.starPower()).withStyle(net.minecraft.ChatFormatting.GOLD));
            tooltip.add(Component.literal("Duration: " + recipe.duration()).withStyle(net.minecraft.ChatFormatting.GOLD));


        }
    }


    private void renderBlock(BlockState state, PoseStack poseStack, BlockRenderDispatcher dispatcher,
                             MultiBufferSource buffer, int x, int y, int z) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        dispatcher.renderSingleBlock(state, poseStack, buffer, 0xF000F0, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        poseStack.popPose();
    }
}
