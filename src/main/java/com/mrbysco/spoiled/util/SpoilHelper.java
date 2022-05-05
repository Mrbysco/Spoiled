package com.mrbysco.spoiled.util;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SpoilHelper {

	public static SpoilRecipe getSpoilRecipe(Level level, ItemStack stack) {
		SimpleContainer inventory = new SimpleContainer(stack);
		for (SpoilRecipe recipe : level.getRecipeManager().getAllRecipesFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get())) {
			ItemStack[] stacks = recipe.getIngredients().get(0).getItems();
			for (ItemStack theStack : stacks) {
				System.out.println(theStack.getItem().getRegistryName());
			}
		}
		return level.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get(), inventory, level).orElse(getDefaultSpoilRecipe(stack));
	}

	private static SpoilRecipe getDefaultSpoilRecipe(ItemStack stack) {
		if (SpoiledConfig.COMMON.spoilEverything.get() && stack.isEdible() &&
				!SpoiledConfig.COMMON.spoilEverythingBlacklist.get().contains(stack.getItem().getRegistryName().toString())) {
			ItemStack spoilStack = SpoiledConfigCache.getDefaultSpoilItem();
			String result = spoilStack.isEmpty() ? "to_air" : "to_" + spoilStack.getItem().getRegistryName().getPath();
			String recipePath = "everything_" + stack.getItem().getRegistryName().getPath() + result;
			return new SpoilRecipe(new ResourceLocation(Reference.MOD_ID, recipePath), "", Ingredient.of(stack), spoilStack, SpoiledConfig.COMMON.defaultSpoilTime.get());
		}
		return null;
	}

	public static boolean isSpoiling(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(Reference.SPOIL_TAG);
	}

	public static int getSpoilTime(ItemStack stack) {
		return stack.hasTag() ? stack.getTag().getInt(Reference.SPOIL_TAG) : 0;
	}

	public static void setSpoilTime(ItemStack stack, int time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(Reference.SPOIL_TAG, time);
		stack.setTag(tag);
	}
}
