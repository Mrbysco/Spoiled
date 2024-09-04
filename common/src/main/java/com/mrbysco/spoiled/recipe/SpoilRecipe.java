package com.mrbysco.spoiled.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class SpoilRecipe implements Recipe<SingleRecipeInput> {
	protected final String group;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final int spoilTime;
	protected final int priority; // higher numbers are higher priority

	public SpoilRecipe(String group, Ingredient ingredient, ItemStack stack, int spoilTime, int priority) {
		this.group = group;
		this.ingredient = ingredient;
		this.result = stack;
		this.spoilTime = spoilTime;
		this.priority = priority;
	}

	@Override
	public RecipeType<?> getType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(SingleRecipeInput recipeInput, Level level) {
		return this.getIngredients().get(0).test(recipeInput.getItem(0));
	}

	@Override
	public ItemStack assemble(SingleRecipeInput recipeInput, HolderLookup.Provider registryAccess) {
		return getResultItem(registryAccess).copy();
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return this.result.copy();
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public int getPriority() {
		return this.priority;
	}

	public int getSpoilTime() {
		if (spoilTime == -1)
			return Services.PLATFORM.getDefaultSpoilTime();
		return spoilTime;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpoiledRecipes.SPOILING_SERIALIZER.get();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class Serializer implements RecipeSerializer<SpoilRecipe> {
		public static final MapCodec<SpoilRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
								Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
								Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
								ItemStack.SINGLE_ITEM_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
								Codec.INT.optionalFieldOf("spoiltime", -1).forGetter(recipe -> recipe.spoilTime),
								Codec.INT.optionalFieldOf("priority", 1).forGetter(recipe -> recipe.spoilTime)
						)
						.apply(instance, SpoilRecipe::new)
		);
		public static final StreamCodec<RegistryFriendlyByteBuf, SpoilRecipe> STREAM_CODEC = StreamCodec.of(
				SpoilRecipe.Serializer::toNetwork, SpoilRecipe.Serializer::fromNetwork
		);

		@Override
		public MapCodec<SpoilRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SpoilRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		public static SpoilRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			ItemStack itemstack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
			int spoilTime = buffer.readVarInt();
			int priority = buffer.readVarInt();
			return new SpoilRecipe(s, ingredient, itemstack, spoilTime, priority);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buffer, SpoilRecipe recipe) {
			buffer.writeUtf(recipe.group);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
			buffer.writeVarInt(recipe.spoilTime);
			buffer.writeVarInt(recipe.priority);
		}
	}
}