package com.mrbysco.spoiled.recipe;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.SpoilRecipe.SerializerSpoilRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SpoiledRecipes {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final IRecipeType<SpoilRecipe> SPOIL_RECIPE_TYPE = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "spoil_recipe").toString());

	public static final RegistryObject<SerializerSpoilRecipe> SPOILING_SERIALIZER = RECIPE_SERIALIZERS.register("spoil_recipe", SerializerSpoilRecipe::new);
}
