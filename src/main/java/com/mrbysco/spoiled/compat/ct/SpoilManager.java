package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.spoiled.SpoilingManager")
public class SpoilManager implements IRecipeManager {

	@Method
	public void addSpoiling(String name, IIngredient food, IItemStack spoilStack, int spoilTime) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final Ingredient foodIngredient = food.asVanillaIngredient();
		final ItemStack resultItemStack = spoilStack.getInternal();
		final SpoilRecipe recipe = new SpoilRecipe(id, "", foodIngredient, resultItemStack, spoilTime);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Method
	public void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime) {
		if (ModList.get().isLoaded(modName)) {
			for (Item foundItem : ForgeRegistries.ITEMS.getValues()) {
				if (foundItem != spoilStack.getInternal().getItem() && foundItem.getRegistryName().getNamespace().equals(modName) && foundItem.isEdible()) {
					String itemLocation = foundItem.getRegistryName().toString().replace(":", "_");
					ResourceLocation id = new ResourceLocation("crafttweaker", itemLocation);
					SpoilRecipe recipe = new SpoilRecipe(id, "", Ingredient.of(new ItemStack(foundItem)), spoilStack.getInternal(), spoilTime);
					CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
				}
			}
		}
	}

	@Override
	public IRecipeType getRecipeType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE;
	}
}
