package com.mrbysco.spoiled.recipe;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.SpoilRecipe.SerializerSpoilRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SpoiledRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final RecipeType<SpoilRecipe> SPOIL_RECIPE_TYPE = RecipeType.register(new ResourceLocation(Reference.MOD_ID, "spoil_recipe").toString());

	public static final RegistryObject<SerializerSpoilRecipe> SPOILING_SERIALIZER = RECIPE_SERIALIZERS.register("spoil_recipe", SerializerSpoilRecipe::new);
}
