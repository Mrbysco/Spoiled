package com.mrbysco.spoiled.recipe;

import com.google.common.collect.Lists;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class StackFoodRecipe extends CustomRecipe {
	public StackFoodRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override

	public boolean matches(CraftingContainer container, Level level) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemstack = container.getItem(i);
			if (!itemstack.isEmpty()) {
				list.add(itemstack);
				if (list.size() > 1) {
					ItemStack stack1 = list.get(0);
					if (itemstack.getItem() != stack1.getItem() || !SpoilHelper.totalUnderMax(itemstack, stack1) || !SpoilHelper.isSpoiling(stack1)) {
						return false;
					}
				}
			}
		}

		return list.size() == 2;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, HolderLookup.Provider registryAccess) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemstack = container.getItem(i);
			if (!itemstack.isEmpty()) {
				list.add(itemstack);
				if (list.size() > 1) {
					ItemStack stack1 = list.get(0);
					if (itemstack.getItem() != stack1.getItem() || !SpoilHelper.isSpoiling(stack1)) {
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (list.size() == 2) {
			ItemStack stack1 = list.get(0);
			ItemStack stack2 = list.get(1);
			if (ItemStack.isSameItem(stack1, stack2) && SpoilHelper.isSpoiling(stack1) && SpoilHelper.totalUnderMax(stack1, stack2)) {
				int spoil1 = SpoilHelper.getSpoilTime(stack1);
				int spoil2 = SpoilHelper.getSpoilTime(stack2);
				int averageSpoil = (int) ((spoil1 + spoil2) / 2.0);

				ItemStack stackCopy = stack1.copy();
				int combinedCount = stack1.getCount() + stack2.getCount();
				stackCopy.setCount(combinedCount);
				SpoilHelper.setSpoilTime(stackCopy, averageSpoil);

				return stackCopy;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		for (int i = 0; i < container.getContainerSize(); i++) {
			container.setItem(i, ItemStack.EMPTY);
		}
		return super.getRemainingItems(container);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpoiledRecipes.STACK_FOOD_SERIALIZER.get();
	}
}
