package com.mrbysco.spoiled.compat.jei;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.compat.jei.category.SpoilCategory;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.plugins.vanilla.crafting.CategoryRecipeValidator;
import mezz.jei.util.ErrorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {

	public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Reference.MOD_ID, "main");
	public static final ResourceLocation SPOILING = new ResourceLocation(Reference.MOD_ID, "spoiling");

	@Nullable
	private IRecipeCategory<SpoilRecipe> spoilCategory;

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(VanillaTypes.ITEM, new ItemStack(Items.ROTTEN_FLESH), SpoilCategory.TYPE);
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
		ErrorUtil.checkNotNull(spoilCategory, "spoilCategory");

		registration.addRecipes(SpoilCategory.TYPE, getVatRecipes(spoilCategory));
	}


	public List<SpoilRecipe> getVatRecipes(IRecipeCategory<SpoilRecipe> spoilCategory) {
		CategoryRecipeValidator<SpoilRecipe> validator = new CategoryRecipeValidator<>(spoilCategory, 1);
		return getValidHandledRecipes(Minecraft.getInstance().level.getRecipeManager(), SpoiledRecipes.SPOIL_RECIPE_TYPE.get(), validator);
	}

	private static <C extends Container, T extends Recipe<C>> List<T> getValidHandledRecipes(RecipeManager recipeManager, RecipeType<T> recipeType, CategoryRecipeValidator<T> validator) {
		return recipeManager.getAllRecipesFor(recipeType).stream().filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r)).toList();
	}
}
