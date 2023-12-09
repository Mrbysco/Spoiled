package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class SpoilRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private int spoilTime;
	private String group;
	private final RecipeSerializer<SpoilRecipe> recipeSerializer;

	private SpoilRecipeBuilder(ItemLike resultIn, Ingredient ingredientIn, RecipeSerializer<SpoilRecipe> serializer) {
		this.result = resultIn.asItem();
		this.ingredient = ingredientIn;
		this.recipeSerializer = serializer;
		this.spoilTime = -1;
	}

	private SpoilRecipeBuilder withSpoilTime(int spoilTime) {
		this.spoilTime = spoilTime;
		return this;
	}

	private SpoilRecipeBuilder withGroup(String group) {
		this.group = group;
		return this;
	}

	public static SpoilRecipeBuilder spoilRecipe(Ingredient ingredientIn, ItemLike resultIn) {
		return new SpoilRecipeBuilder(resultIn, ingredientIn, SpoiledRecipes.SPOILING_SERIALIZER.get());
	}

	public void build(RecipeOutput recipeOutput) {
		this.build(recipeOutput, BuiltInRegistries.ITEM.getKey(this.result));
	}

	public void build(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(new Result(id, this.group == null ? "" : this.group, this.ingredient, this.result, this.spoilTime, this.recipeSerializer));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final Item result;
		private final int spoilTime;
		private final RecipeSerializer<SpoilRecipe> serializer;

		public Result(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, Item result, int spoilTime, RecipeSerializer<SpoilRecipe> serializerIn) {
			this.id = idIn;
			this.group = groupIn;
			this.ingredient = ingredientIn;
			this.result = result;
			this.spoilTime = spoilTime;
			this.serializer = serializerIn;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			json.add("ingredient", this.ingredient.toJson(false));
			JsonObject resultObject = new JsonObject();
			resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());

			json.add("result", resultObject);
			if (this.spoilTime > 0)
				json.addProperty("spoiltime", this.spoilTime);
		}

		public RecipeSerializer<?> type() {
			return this.serializer;
		}

		/**
		 * Gets the ID for the recipe.
		 */
		public ResourceLocation id() {
			return this.id;
		}

		/**
		 * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
		 */
		@Nullable
		public AdvancementHolder advancement() {
			return null;
		}
	}
}
