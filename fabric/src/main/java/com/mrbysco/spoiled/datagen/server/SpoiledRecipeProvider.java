package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class SpoiledRecipeProvider extends FabricRecipeProvider {
	public SpoiledRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";
		SpoilRecipeBuilder.spoilRecipe(Ingredient.of(SpoiledTags.FOODS), Items.ROTTEN_FLESH)
				.build(withConditions(consumer, new InitializeSpoilingCondition()), Constants.modLoc(folder + "initial" + toRotten));

		SpecialRecipeBuilder.special(StackFoodRecipe::new)
				.save(withConditions(consumer, new MergeRecipeCondition()),Constants.modLoc("merge_food").toString());
	}
}