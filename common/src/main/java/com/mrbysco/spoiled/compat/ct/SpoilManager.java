package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

import java.util.List;

@ZenRegister
@Name("mods.spoiled.SpoilingManager")
public class SpoilManager implements IRecipeManager<SpoilRecipe> {

	@Method
	public void addSpoiling(String name, IIngredient food, IItemStack spoilStack, int spoilTime, int priority) {
		final ResourceLocation id = ResourceLocation.fromNamespaceAndPath("crafttweaker", name);
		final Ingredient foodIngredient = food.asVanillaIngredient();
		final ItemStack resultItemStack = spoilStack.getInternal();
		final SpoilRecipe recipe = new SpoilRecipe("", foodIngredient, resultItemStack, spoilTime, priority);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(id, recipe)));
	}

	@Method
	public void addSpoiling(String name, IIngredient food, IItemStack spoilStack, int spoilTime) {
		addSpoiling(name, food, spoilStack, spoilTime, 1);
	}

	@Method
	public void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime, int priority) {
		if (Services.PLATFORM.isModLoaded(modName)) {
			List<Item> edibleFoodList = BuiltInRegistries.ITEM.stream().filter(item -> item.getDefaultInstance().has(DataComponents.FOOD)).toList();
			for (Item foundItem : edibleFoodList) {
				ResourceLocation location = BuiltInRegistries.ITEM.getKey(foundItem);
				if (foundItem != spoilStack.getInternal().getItem() && location != null && location.getNamespace().equals(modName)) {
					String itemLocation = location.toString().replace(":", "_");
					ResourceLocation id = ResourceLocation.fromNamespaceAndPath("crafttweaker", itemLocation);
					SpoilRecipe recipe = new SpoilRecipe("", Ingredient.of(new ItemStack(foundItem)), spoilStack.getInternal(), spoilTime, priority);
					CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new RecipeHolder<>(id, recipe)));
				}
			}
		}
	}

	@Method
	public void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime) {
		addModSpoiling(modName, spoilStack, spoilTime, 1);
	}

	@Override
	public RecipeType<SpoilRecipe> getRecipeType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE.get();
	}
}
