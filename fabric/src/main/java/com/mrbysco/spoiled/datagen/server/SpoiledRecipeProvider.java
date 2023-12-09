package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class SpoiledRecipeProvider extends FabricRecipeProvider {
	public SpoiledRecipeProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";
		SpoilRecipeBuilder.spoilRecipe(Ingredient.of(SpoiledTags.FOODS_VANILLA), Items.ROTTEN_FLESH)
				.build(withConditions(consumer, InitializeSpoilingCondition.PROVIDER), new ResourceLocation(Constants.MOD_ID, folder + "vanilla" + toRotten));

		SpecialRecipeBuilder.special(SpoiledRecipes.STACK_FOOD_SERIALIZER.get())
				.save(withConditions(consumer, MergeRecipeCondition.PROVIDER), new ResourceLocation(Constants.MOD_ID, "merge_food").toString());
	}
}