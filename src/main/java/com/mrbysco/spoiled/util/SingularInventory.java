package com.mrbysco.spoiled.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SingularInventory implements IInventory {
	private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);

	@Override
	public int getSizeInventory() {
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
	public ItemStack getStackInSlot(int index) {
		return this.itemStacks.get(0);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndRemove(this.itemStacks, 0);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.itemStacks, 0);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.itemStacks.set(0, stack);
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		this.itemStacks.clear();
	}
}