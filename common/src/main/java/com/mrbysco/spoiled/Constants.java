package com.mrbysco.spoiled;

import com.mrbysco.spoiled.recipe.SpoilRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Constants {
	public static final String MOD_NAME = "Spoiled";
	public static final String MOD_ID = "spoiled";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static final String SPOIL_TAG = MOD_PREFIX + "SpoilTimer";
	public static final String SPOIL_TIME_TAG = MOD_PREFIX + "SpoilMaxTime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public final static List<RecipeHolder<SpoilRecipe>> SPOIL_RECIPES = new ArrayList<>();

	public static List<RecipeHolder<SpoilRecipe>> getRecipesFor(SingleRecipeInput input, Level serverLevel) {
		return SPOIL_RECIPES.stream()
				.filter(holder -> holder.value().matches(input, serverLevel))
				.collect(Collectors.toList());
	}
}