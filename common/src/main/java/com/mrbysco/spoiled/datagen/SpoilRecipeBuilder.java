package com.mrbysco.spoiled.datagen;

import com.mrbysco.spoiled.recipe.SpoilRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SpoilRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private int spoilTime;
	private int priority = 1;
	private String group;

	private SpoilRecipeBuilder(ItemLike resultIn, Ingredient ingredientIn) {
		this.result = resultIn.asItem();
		this.ingredient = ingredientIn;
	}

	private SpoilRecipeBuilder withSpoilTime(int spoilTime) {
		this.spoilTime = spoilTime;
		return this;
	}

	private SpoilRecipeBuilder withGroup(String group) {
		this.group = group;
		return this;
	}

	private SpoilRecipeBuilder withPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public static SpoilRecipeBuilder spoilRecipe(Ingredient ingredientIn, ItemLike resultIn) {
		return new SpoilRecipeBuilder(resultIn, ingredientIn);
	}

	public void build(RecipeOutput recipeOutput) {
		this.build(recipeOutput, BuiltInRegistries.ITEM.getKey(this.result));
	}

	public void build(RecipeOutput recipeOutput, ResourceLocation id) {
		SpoilRecipe recipe = new SpoilRecipe(this.group == null ? "" : this.group, this.ingredient, new ItemStack(this.result), this.spoilTime, this.priority);
		recipeOutput.accept(id, recipe, null);
	}
}
