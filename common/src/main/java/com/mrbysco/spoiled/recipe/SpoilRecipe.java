package com.mrbysco.spoiled.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpoilRecipe implements Recipe<SingleRecipeInput> {
	public static final MapCodec<SpoilRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
							Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
							Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
							ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
							Codec.INT.optionalFieldOf("spoiltime", -1).forGetter(recipe -> recipe.spoilTime),
							Codec.INT.optionalFieldOf("priority", 1).forGetter(recipe -> recipe.priority)
					)
					.apply(instance, SpoilRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, SpoilRecipe> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			o -> o.group,
			Ingredient.CONTENTS_STREAM_CODEC,
			o -> o.ingredient,
			ItemStackTemplate.STREAM_CODEC,
			o -> o.result,
			ByteBufCodecs.INT,
			o -> o.spoilTime,
			ByteBufCodecs.INT,
			o -> o.priority,
			SpoilRecipe::new
	);
	public static final RecipeSerializer<SpoilRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

	protected final String group;
	protected final Ingredient ingredient;
	protected final ItemStackTemplate result;
	protected final int spoilTime;
	protected final int priority; // higher numbers are higher priority

	public SpoilRecipe(String group, Ingredient ingredient, ItemStackTemplate stack, int spoilTime, int priority) {
		this.group = group;
		this.ingredient = ingredient;
		this.result = stack;
		this.spoilTime = spoilTime;
		this.priority = priority;
	}

	@NotNull
	@Override
	public RecipeType<SpoilRecipe> getType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE.get();
	}

	@NotNull
	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@NotNull
	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	@Override
	public boolean matches(SingleRecipeInput recipeInput, @NotNull Level level) {
		return this.getIngredient().test(recipeInput.getItem(0));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull SingleRecipeInput recipeInput) {
		return getResult();
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public ItemStack getResult() {
		return result.create();
	}

	@NotNull
	@Override
	public String group() {
		return this.group;
	}

	public int getPriority() {
		return this.priority;
	}

	public int getSpoilTime() {
		if (spoilTime == -1)
			return SpoiledConfig.COMMON.defaultSpoilTime.get();
		return spoilTime;
	}

	@NotNull
	@Override
	public RecipeSerializer<SpoilRecipe> getSerializer() {
		return SpoiledRecipes.SPOILING_SERIALIZER.get();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return false;
	}
}