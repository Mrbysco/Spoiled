package com.mrbysco.spoiled.registration;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoilRecipe.Serializer;
import com.mrbysco.spoiled.recipe.StackFoodRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SpoiledRecipes {
	public static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
	public static final RegistrationProvider<RecipeType<?>> RECIPE_TYPES = RegistrationProvider.get(Registries.RECIPE_TYPE, Constants.MOD_ID);

	public static final RegistryObject<RecipeType<SpoilRecipe>> SPOIL_RECIPE_TYPE = RECIPE_TYPES.register("spoil_recipe", () -> new RecipeType<>() {
	});
	public static final RegistryObject<Serializer> SPOILING_SERIALIZER = RECIPE_SERIALIZERS.register("spoil_recipe", Serializer::new);
	public static final RegistryObject<RecipeSerializer<StackFoodRecipe>> STACK_FOOD_SERIALIZER = RECIPE_SERIALIZERS.register("stack_food", () -> new CustomRecipe.Serializer<>(StackFoodRecipe::new));

	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
