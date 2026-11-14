package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class SpoiledRecipeProvider extends RecipeProvider {

	public SpoiledRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
		super(recipeOutput, advancementOutput);
	}


	@Override
	protected void buildRecipes() {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";

		RecipeOutput initializeOutput = output.withConditions(InitializeSpoilingCondition.INSTANCE);
		SpoilRecipeBuilder.spoilRecipe(tag(SpoiledTags.FOODS), Items.ROTTEN_FLESH)
				.build(initializeOutput, Constants.modLoc(folder + "initial" + toRotten));


		RecipeOutput mergeOutput = output.withConditions(MergeRecipeCondition.INSTANCE);
		SpecialRecipeBuilder.special(StackFoodRecipe::new)
				.save(mergeOutput, Constants.modLoc("merge_food").toString());
	}
}