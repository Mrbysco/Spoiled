package com.mrbysco.spoiled.recipe;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.SpoilRecipe.SerializerSpoilRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpoiledRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Reference.MOD_ID);

	public static final RegistryObject<RecipeType<SpoilRecipe>> SPOIL_RECIPE_TYPE = RECIPE_TYPES.register("spoil_recipe", () -> new RecipeType<>() {
	});
	public static final RegistryObject<SerializerSpoilRecipe> SPOILING_SERIALIZER = RECIPE_SERIALIZERS.register("spoil_recipe", SerializerSpoilRecipe::new);
	public static final RegistryObject<SimpleRecipeSerializer<StackFoodRecipe>> STACK_FOOD_SERIALIZER = RECIPE_SERIALIZERS.register("stack_food", () -> new SimpleRecipeSerializer<>(StackFoodRecipe::new));
}
