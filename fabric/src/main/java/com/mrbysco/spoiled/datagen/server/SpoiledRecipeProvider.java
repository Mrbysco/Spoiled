package com.mrbysco.spoiled.datagen.server;

import com.google.common.base.Preconditions;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.SpoilRecipeBuilder;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SpoiledRecipeProvider extends FabricRecipeProvider {
	public SpoiledRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@NotNull
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return new Provider(provider, recipeOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Spoiled recipes";
	}

	public static class Provider extends RecipeProvider {
		public Provider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			super(provider, recipeOutput);
		}

		@Override
		public void buildRecipes() {
			String toRotten = "_to_rotten_flesh";
			String folder = "spoiling/";
			SpoilRecipeBuilder.spoilRecipe(Ingredient.of(tagSet(SpoiledTags.FOODS)), Items.ROTTEN_FLESH)
					.build(withConditions(output, new InitializeSpoilingCondition()), Constants.modLoc(folder + "initial" + toRotten));

			SpecialRecipeBuilder.special(StackFoodRecipe::new)
					.save(withConditions(output, new MergeRecipeCondition()), Constants.modLoc("merge_food").toString());
		}

		protected RecipeOutput withConditions(RecipeOutput exporter, ResourceCondition... conditions) {
			Preconditions.checkArgument(conditions.length > 0, "Must add at least one condition.");
			return new RecipeOutput() {
				@Override
				public void accept(@NotNull ResourceKey<Recipe<?>> key, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementEntry) {
					FabricDataGenHelper.addConditions(recipe, conditions);
					exporter.accept(key, recipe, advancementEntry);
				}

				@Override
				public Advancement.Builder advancement() {
					return exporter.advancement();
				}

				@Override
				public void includeRootAdvancement() {
				}
			};
		}

		private HolderSet<Item> tagSet(TagKey<Item> tagKey) {
			return this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(tagKey);
		}
	}
}