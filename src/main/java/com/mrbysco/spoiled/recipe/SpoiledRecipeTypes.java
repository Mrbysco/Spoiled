package com.mrbysco.spoiled.recipe;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.SpoilRecipe.SerializerSpoilRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpoiledRecipeTypes {
	public static final RecipeType<SpoilRecipe> SPOIL_RECIPE_TYPE = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "spoil_recipe").toString());

	public static void init() {
		//For initializing the static final fields
	}
}
