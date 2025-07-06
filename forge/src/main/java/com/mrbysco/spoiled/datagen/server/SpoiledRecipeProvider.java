package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class SpoiledRecipeProvider extends RecipeProvider {
	public SpoiledRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";

		RecipeOutput initializeOutput = output.withConditions(InitializeSpoilingCondition.INSTANCE);
		SpoilRecipeBuilder.spoilRecipe(Ingredient.of(tagSet(SpoiledTags.FOODS)), Items.ROTTEN_FLESH)
				.build(initializeOutput, Constants.modLoc(folder + "initial" + toRotten));


		RecipeOutput mergeOutput = output.withConditions(MergeRecipeCondition.INSTANCE);
		SpecialRecipeBuilder.special(StackFoodRecipe::new)
				.save(mergeOutput, Constants.modLoc("merge_food").toString());
	}

	private HolderSet<Item> tagSet(TagKey<Item> tagKey) {
		return this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(tagKey);
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new SpoiledRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Spoiled Recipes";
		}
	}
}