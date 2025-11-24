package com.benbenlaw.starforge.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record StarForgeRecipe(int tier, int starPower, int duration, Ingredient input, Optional<List<Ingredient>> extraIngredients, ItemStack result) implements Recipe<StarForgeRecipeInput> {

    @Override
    public boolean matches(StarForgeRecipeInput recipeInput, Level level) {

        if (!input.test(recipeInput.getItem(0))) {
            return false;
        }

        if (extraIngredients().isPresent()) {
            List<Ingredient> extras = extraIngredients().get();
            for (int i = 0; i < extras.size(); i++) {
                if (!extras.get(i).test(recipeInput.getItem(i + 1))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(StarForgeRecipeInput p_345149_, HolderLookup.Provider p_346030_) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_336125_) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return StarForgeRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return StarForgeRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<StarForgeRecipe> {
        private Type() {

        }
        public static final StarForgeRecipe.Type INSTANCE = new StarForgeRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<StarForgeRecipe> {
        public static final StarForgeRecipe.Serializer INSTANCE = new Serializer();

        public final MapCodec<StarForgeRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Codec.INT.fieldOf("tier").forGetter(StarForgeRecipe::tier),
                        Codec.INT.fieldOf("star_power").forGetter(StarForgeRecipe::starPower),
                        Codec.INT.fieldOf("duration").forGetter(StarForgeRecipe::duration),
                        Ingredient.CODEC.fieldOf("input").forGetter(StarForgeRecipe::input),
                        Codec.list(Ingredient.CODEC).optionalFieldOf("extra_ingredients").forGetter(StarForgeRecipe::extraIngredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(StarForgeRecipe::result)
                ).apply(instance, StarForgeRecipe::new)
        );

        private static StarForgeRecipe read(RegistryFriendlyByteBuf buffer) {
            int tier = buffer.readInt();
            int starPower = buffer.readInt();
            int duration = buffer.readInt();
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int extraSize = buffer.readVarInt();
            Optional<List<Ingredient>> extraIngredients;

            if (extraSize == 0) {
                extraIngredients = Optional.empty();
            } else {
                List<Ingredient> extras = new ArrayList<>();
                for (int i = 0; i < extraSize; i++) {
                    extras.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
                }
                extraIngredients = Optional.of(extras);
            }

            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new StarForgeRecipe(tier, starPower, duration, input, extraIngredients, result);
        }

        private static StarForgeRecipe write(RegistryFriendlyByteBuf buffer, StarForgeRecipe recipe) {
            buffer.writeInt(recipe.tier());
            buffer.writeInt(recipe.starPower());
            buffer.writeInt(recipe.duration());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input());

            if (recipe.extraIngredients().isPresent()) {
                List<Ingredient> extras = recipe.extraIngredients().get();
                buffer.writeVarInt(extras.size());
                for (Ingredient ing : extras) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.extraIngredients().get().get(extras.indexOf(ing)));
                }
            } else {
                buffer.writeVarInt(0);
            }            ItemStack.STREAM_CODEC.encode(buffer, recipe.result());

            return recipe;
        }

        @Override
        public MapCodec<StarForgeRecipe> codec() {
            return CODEC;
        }

        private final StreamCodec<RegistryFriendlyByteBuf, StarForgeRecipe> STREAM_CODEC = StreamCodec.of(
                StarForgeRecipe.Serializer::write, StarForgeRecipe.Serializer::read);

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StarForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

