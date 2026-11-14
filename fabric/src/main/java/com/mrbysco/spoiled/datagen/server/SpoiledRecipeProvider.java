package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SpoiledRecipeProvider extends FabricRecipeProvider {
	public SpoiledRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries,
	                                              BootstrapContext<Recipe<?>> recipes,
	                                              BootstrapContext<Advancement> advancements
	) {
		return new RecipeProvider(recipes, advancements) {
			@Override
			public void buildRecipes() {
				String toRotten = "_to_rotten_flesh";
				String folder = "spoiling/";
				SpoilRecipeBuilder.spoilRecipe(tag(SpoiledTags.FOODS), Items.ROTTEN_FLESH)
						.build(withConditions(output, new InitializeSpoilingCondition()), Constants.modLoc(folder + "initial" + toRotten));

				SpecialRecipeBuilder.special(StackFoodRecipe::new)
						.save(withConditions(output, new MergeRecipeCondition()), Constants.modLoc("merge_food").toString());
			}
		};
	}

	@NotNull
	@Override
	public String getName() {
		return "Spoiled recipes";
	}
}