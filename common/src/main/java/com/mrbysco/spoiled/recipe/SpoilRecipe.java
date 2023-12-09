package com.mrbysco.spoiled.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class SpoilRecipe implements Recipe<Container> {
	protected final String group;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final int spoilTime;

	public SpoilRecipe(String group, Ingredient ingredient, ItemStack stack, int spoilTime) {
		this.group = group;
		this.ingredient = ingredient;
		this.result = stack;
		this.spoilTime = spoilTime;
	}

	@Override
	public RecipeType<?> getType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.getIngredients().get(0).test(inv.getItem(0));
	}

	public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
		return getResultItem(registryAccess).copy();
	}

	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result.copy();
	}

	public String getGroup() {
		return this.group;
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
		private static final Codec<SpoilRecipe> CODEC = Serializer.RawSpoilRecipe.CODEC.flatXmap(rawLootRecipe -> {
			return DataResult.success(new SpoilRecipe(
					rawLootRecipe.group,
					rawLootRecipe.ingredient,
					rawLootRecipe.result,
					rawLootRecipe.spoilTime
			));
		}, recipe -> {
			throw new NotImplementedException("Serializing SpoilRecipe is not implemented yet.");
		});

		@Override
		public Codec<SpoilRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public SpoilRecipe fromNetwork(FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack itemstack = buffer.readItem();
			int spoilTime = buffer.readVarInt();
			return new SpoilRecipe(s, ingredient, itemstack, spoilTime);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SpoilRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeItem(recipe.result);
			buffer.writeVarInt(recipe.spoilTime);
		}

		static record RawSpoilRecipe(
				String group, Ingredient ingredient, ItemStack result, int spoilTime
		) {
			public static final Codec<RawSpoilRecipe> CODEC = RecordCodecBuilder.create(
					instance -> instance.group(
									ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
									Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
									CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
									ExtraCodecs.strictOptionalField(Codec.INT, "spoiltime", -1).forGetter(recipe -> recipe.spoilTime)
							)
							.apply(instance, RawSpoilRecipe::new)
			);
		}
	}
}