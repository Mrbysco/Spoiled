package com.mrbysco.spoiled.datagen.server;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SpoiledRecipeProvider extends RecipeProvider {
	public SpoiledRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";

		RecipeOutput initializeOutput = recipeOutput.withConditions(InitializeSpoilingCondition.INSTANCE);
		SpoilRecipeBuilder.spoilRecipe(Ingredient.of(SpoiledTags.FOODS_VANILLA), Items.ROTTEN_FLESH)
				.build(initializeOutput, new ResourceLocation(Constants.MOD_ID, folder + "vanilla" + toRotten));


		RecipeOutput mergeOutput = recipeOutput.withConditions(MergeRecipeCondition.INSTANCE);
		SpecialRecipeBuilder.special(SpoiledRecipes.STACK_FOOD_SERIALIZER.get())
				.save(mergeOutput, new ResourceLocation(Constants.MOD_ID, "merge_food"));
	}

	@Override
	protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
		return null;
	}
}