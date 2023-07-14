package com.mrbysco.spoiled.datagen.server;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SpoiledRecipeProvider extends RecipeProvider {
	public SpoiledRecipeProvider(PackOutput gen) {
		super(gen);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		String toRotten = "_to_rotten_flesh";
		String folder = "spoiling/";
		ConditionalRecipe.builder()
				.addCondition(new InitializeSpoilingCondition())
				.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.of(SpoiledTags.FOODS_VANILLA), Items.ROTTEN_FLESH)::build)
				.build(consumer, Constants.MOD_ID, folder + "vanilla" + toRotten);


		ConditionalRecipe.builder()
				.addCondition(new MergeRecipeCondition())
				.addRecipe(c -> SpecialRecipeBuilder.special(SpoiledRecipes.STACK_FOOD_SERIALIZER.get())
						.save(c, new ResourceLocation(Constants.MOD_ID, "merge_food").toString()))
				.build(consumer, new ResourceLocation(Constants.MOD_ID, "merge_food"));
	}

	@Override
	protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
		return null;
	}
}