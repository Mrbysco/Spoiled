package com.mrbysco.spoiled.compat.jei.validator;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@SuppressWarnings("deprecation")
public class SpoiledValidator {
	private static final int INVALID_COUNT = -1;

	private final IRecipeCategory<SpoilRecipe> recipeCategory;

	public SpoiledValidator(IRecipeCategory<SpoilRecipe> recipeCategory) {
		this.recipeCategory = recipeCategory;
	}

	public boolean isRecipeValid(RecipeHolder<SpoilRecipe> recipe) {
		return hasValidInputsAndOutputs(recipe);
	}

	public boolean isRecipeHandled(RecipeHolder<SpoilRecipe> recipe) {
		return this.recipeCategory.isHandled(recipe.value());
	}

	@SuppressWarnings("ConstantConditions")
	private boolean hasValidInputsAndOutputs(RecipeHolder<SpoilRecipe> recipeHolder) {
		if (recipeHolder == null) {
			return false;
		}
		SpoilRecipe recipe = recipeHolder.value();
		if (recipe.isSpecial()) {
			return true;
		}
		ItemStack recipeOutput = recipe.getResult();
		if (recipeOutput == null || recipeOutput.isEmpty()) {
			Constants.LOGGER.error("Recipe has no output. {}", recipeHolder.id());
			return false;
		}
		Ingredient ingredient = recipe.getIngredient();
		if (ingredient == null) {
			Constants.LOGGER.error("Recipe has no input Ingredients. {}", recipeHolder.id());
			return false;
		}
		int inputCount = getInputCount(ingredient);
		if (inputCount == INVALID_COUNT) {
			return false;
		} else if (inputCount > 1) {
			Constants.LOGGER.error("Recipe has too many inputs. {}", recipeHolder.id());
			return false;
		} else if (inputCount == 0 && 1 > 0) {
			Constants.LOGGER.error("Recipe has no inputs. {}", recipeHolder.id());
			return false;
		}
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	private static int getInputCount(Ingredient ingredient) {
		int inputCount = 0;

		List<Holder<Item>> input = ingredient.items().toList();
		if (input == null) {
			return INVALID_COUNT;
		} else {
			inputCount++;
		}
		return inputCount;
	}
}
