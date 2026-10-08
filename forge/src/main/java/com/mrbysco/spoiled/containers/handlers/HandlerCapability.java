package com.mrbysco.spoiled.containers.handlers;

import com.mrbysco.spoiled.api.IContainerHandler;
import com.mrbysco.spoiled.containers.ContainerTrace;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class HandlerCapability implements IContainerHandler {
	@Override
	public boolean isContainer(Player player, ItemStack inventoryStack) {
		return inventoryStack != null && inventoryStack.getCapability(Capabilities.ItemHandler.ITEM) != null;
	}

	@Override
	public int getSignature(Player player, ItemStack inventoryStack) {
		return inventoryStack.hashCode();
	}

	@Override
	public void spoilItems(Player player, ContainerTrace trace, ItemStack inventoryStack) {
		IItemHandler itemHandler = inventoryStack.getCapability(Capabilities.ItemHandler.ITEM);
		if (itemHandler == null) return;

		Level level = player.level();
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack nestedStack = itemHandler.getStackInSlot(i);
			if (nestedStack.isEmpty()) continue;

			RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, nestedStack);
			if (recipeHolder != null) {
				SpoilHandler.spoilItemInHandler(inventoryStack, itemHandler, i, nestedStack,
						recipeHolder.value(), level.registryAccess(), level.getRandom());
			}
		}
	}
}