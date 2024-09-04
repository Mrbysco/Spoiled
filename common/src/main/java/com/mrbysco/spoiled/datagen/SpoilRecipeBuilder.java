package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpoilRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private String group;
	private int priority = 1;
	private final RecipeSerializer<SpoilRecipe> recipeSerializer;

	private SpoilRecipeBuilder(ItemLike resultIn, Ingredient ingredientIn, RecipeSerializer<SpoilRecipe> serializer) {
		this.result = resultIn.asItem();
		this.ingredient = ingredientIn;
		this.recipeSerializer = serializer;
	}

	public static SpoilRecipeBuilder spoilRecipe(Ingredient ingredientIn, ItemLike resultIn) {
		return new SpoilRecipeBuilder(resultIn, ingredientIn, SpoiledRecipes.SPOILING_SERIALIZER.get());
	}

	public SpoilRecipeBuilder group(String groupIn) {
		this.group = groupIn;
		return this;
	}

	public SpoilRecipeBuilder priority(int priorityIn) {
		this.priority = priorityIn;
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumerIn) {
		this.build(consumerIn, BuiltInRegistries.ITEM.getKey(this.result));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.group == null ? "" : this.group, this.ingredient, this.priority, this.result, this.recipeSerializer));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final int priority;
		private final Item result;
		private final RecipeSerializer<SpoilRecipe> serializer;

		public Result(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, int priority, Item result, RecipeSerializer<SpoilRecipe> serializerIn) {
			this.id = idIn;
			this.group = groupIn;
			this.priority = priority;
			this.result = result;
			this.ingredient = ingredientIn;
			this.serializer = serializerIn;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			json.add("ingredient", this.ingredient.toJson());

			if (this.priority != 1) {
				json.addProperty("priority", this.priority);
			}

			JsonObject resultObject = new JsonObject();
			resultObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
			json.add("result", resultObject);
		}

		public RecipeSerializer<?> getType() {
			return this.serializer;
		}

		/**
		 * Gets the ID for the recipe.
		 */
		public ResourceLocation getId() {
			return this.id;
		}

		/**
		 * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
		 */
		@Nullable
		public JsonObject serializeAdvancement() {
			return null;
		}

		/**
		 * Gets the ID for the advancement associated with this recipe.
		 * is non-null.
		 */
		@Nullable
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
