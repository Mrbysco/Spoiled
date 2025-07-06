package com.mrbysco.spoiled.compat.jei;

import com.google.common.collect.Lists;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.compat.jei.category.SpoilCategory;
import com.mrbysco.spoiled.compat.jei.validator.SpoiledValidator;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {

	public static final ResourceLocation PLUGIN_UID = Constants.modLoc("main");
	public static final ResourceLocation SPOILING = Constants.modLoc("spoiling");

	@Nullable
	private IRecipeCategory<SpoilRecipe> spoilCategory;

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(SpoilCategory.TYPE, VanillaTypes.ITEM_STACK, new ItemStack(Items.ROTTEN_FLESH));
	}

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(spoilCategory = new SpoilCategory(guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		assert spoilCategory != null;

		registration.addRecipes(SpoilCategory.TYPE, getSpoilRecipes(spoilCategory).stream().map(RecipeHolder::value).toList());
	}


	public List<RecipeHolder<SpoilRecipe>> getSpoilRecipes(IRecipeCategory<SpoilRecipe> spoilCategory) {
		ClientLevel level = Minecraft.getInstance().level;
		if (Services.PLATFORM.spoilEverything()) {
			List<RecipeHolder<SpoilRecipe>> recipes = Lists.newArrayList();
			BuiltInRegistries.ITEM.forEach(item -> {
				RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, new ItemStack(item));
				if (recipeHolder != null) {
					recipes.add(recipeHolder);
				}
			});
			return recipes;
		} else {
			return getValidHandledRecipes(new SpoiledValidator(spoilCategory));
		}
	}

	private static List<RecipeHolder<SpoilRecipe>> getValidHandledRecipes(SpoiledValidator validator) {
		return Constants.SPOIL_RECIPES.stream()
				.filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r)).toList();
	}
}
