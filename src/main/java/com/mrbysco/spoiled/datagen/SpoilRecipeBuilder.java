package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SpoilRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private String group;
	private final IRecipeSerializer<SpoilRecipe> recipeSerializer;

	private SpoilRecipeBuilder(IItemProvider resultIn, Ingredient ingredientIn, IRecipeSerializer<SpoilRecipe> serializer) {
		this.result = resultIn.asItem();
		this.ingredient = ingredientIn;
		this.recipeSerializer = serializer;
	}

	public static SpoilRecipeBuilder spoilRecipe(Ingredient ingredientIn, IItemProvider resultIn) {
		return new SpoilRecipeBuilder(resultIn, ingredientIn, SpoiledRecipes.SPOILING_SERIALIZER.get());
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, Registry.ITEM.getKey(this.result));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new SpoilRecipeBuilder.Result(id, this.group == null ? "" : this.group, this.ingredient, this.recipeSerializer));
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Ingredient ingredient;
		private final IRecipeSerializer<SpoilRecipe> serializer;

		public Result(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, IRecipeSerializer<SpoilRecipe> serializerIn) {
			this.id = idIn;
			this.group = groupIn;
			this.ingredient = ingredientIn;
			this.serializer = serializerIn;
		}

		public void serializeRecipeData(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			json.add("ingredient", this.ingredient.toJson());
		}

		public IRecipeSerializer<?> getType() {
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
		 * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
		 * is non-null.
		 */
		@Nullable
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
