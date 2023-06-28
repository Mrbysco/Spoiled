package com.mrbysco.spoiled.compat.jei.validator;

import com.mrbysco.spoiled.Spoiled;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class SpoiledValidator {
	private static final int INVALID_COUNT = -1;

	private final IRecipeCategory<SpoilRecipe> recipeCategory;

	public SpoiledValidator(IRecipeCategory<SpoilRecipe> recipeCategory) {
		this.recipeCategory = recipeCategory;
	}

	public boolean isRecipeValid(SpoilRecipe recipe) {
		return hasValidInputsAndOutputs(recipe);
	}

	public boolean isRecipeHandled(SpoilRecipe recipe) {
		return this.recipeCategory.isHandled(recipe);
	}

	@SuppressWarnings("ConstantConditions")
	private boolean hasValidInputsAndOutputs(SpoilRecipe recipe) {
		if (recipe.isSpecial()) {
			return true;
		}
		ItemStack recipeOutput = recipe.getResultItem(null);
		if (recipeOutput == null || recipeOutput.isEmpty()) {
			Spoiled.LOGGER.error("Recipe has no output. {}", recipe.getId());
			return false;
		}
		List<Ingredient> ingredients = recipe.getIngredients();
		if (ingredients == null) {
			Spoiled.LOGGER.error("Recipe has no input Ingredients. {}", recipe.getId());
			return false;
		}
		int inputCount = getInputCount(ingredients);
		if (inputCount == INVALID_COUNT) {
			return false;
		} else if (inputCount > 1) {
			Spoiled.LOGGER.error("Recipe has too many inputs. {}", recipe.getId());
			return false;
		} else if (inputCount == 0 && 1 > 0) {
			Spoiled.LOGGER.error("Recipe has no inputs. {}", recipe.getId());
			return false;
		}
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	private static int getInputCount(List<Ingredient> ingredientList) {
		int inputCount = 0;
		for (Ingredient ingredient : ingredientList) {
			ItemStack[] input = ingredient.getItems();
			if (input == null) {
				return INVALID_COUNT;
			} else {
				inputCount++;
			}
		}
		return inputCount;
	}
}
