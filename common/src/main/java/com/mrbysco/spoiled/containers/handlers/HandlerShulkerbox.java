package com.mrbysco.spoiled.containers.handlers;

import com.mrbysco.spoiled.api.IContainerHandler;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.containers.ContainerTrace;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class HandlerShulkerbox implements IContainerHandler {
	private final int SLOTS = 27;

	@Override
	public boolean isContainer(Player player, ItemStack inventoryStack) {
		return inventoryStack != null && inventoryStack.getCount() == 1 && Block.byItem(inventoryStack.getItem()) instanceof ShulkerBoxBlock;
	}

	@Override
	public int getSignature(Player player, ItemStack inventoryStack) {
		return inventoryStack.hashCode();
	}

	@Override
	public void spoilItems(Player player, ContainerTrace trace, ItemStack inventoryStack) {
		Level level = player.level();
		ResourceLocation containerKey = BuiltInRegistries.ITEM.getKey(inventoryStack.getItem());
		double modifier = SpoiledConfigCache.itemContainerModifier.getOrDefault(containerKey, 1.0D);
		if (modifier <= 0) return;

		NonNullList<ItemStack> itemList = getItemList(inventoryStack);
		boolean changed = false;

		for (int i = 0; i < itemList.size(); i++) {
			ItemStack stack = itemList.get(i);
			if (stack.isEmpty()) continue;

			RecipeHolder<SpoilRecipe> holder = SpoilHelper.getSpoilRecipe(level, stack);
			if (holder == null) continue;
			if (modifier != 1.0D && level.getRandom().nextDouble() > modifier) continue;

			SpoilRecipe recipe = holder.value();
			SpoilHelper.updateSpoilingStack(stack, recipe);
			changed = true;

			if (SpoilHelper.isSpoiled(stack)) {
				ItemStack spoiledStack = recipe.getResultItem(level.registryAccess()).copy();
				if (!spoiledStack.isEmpty()) spoiledStack.setCount(stack.getCount());
				itemList.set(i, spoiledStack.isEmpty() ? ItemStack.EMPTY : spoiledStack);
			}
		}

		if (changed) {
			setItemList(inventoryStack, itemList);
			player.getInventory().setChanged();
		}
	}

	private NonNullList<ItemStack> getItemList(ItemStack itemStack) {
		NonNullList<ItemStack> itemStacks = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
		itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(itemStacks);
		return itemStacks;
	}

	private void setItemList(ItemStack itemStack, NonNullList<ItemStack> itemStacks) {
		itemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(itemStacks));
	}
}
