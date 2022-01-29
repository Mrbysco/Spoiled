package com.mrbysco.spoiled.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SingularInventory implements Container {
	private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.itemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.itemStacks.get(0);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.takeItem(this.itemStacks, 0);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.itemStacks, 0);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.itemStacks.set(0, stack);
	}

	@Override
	public void setChanged() {

	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		this.itemStacks.clear();
	}
}