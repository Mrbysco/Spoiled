package com.mrbysco.spoiled.util;

import net.minecraft.item.ItemStack;

public class InventoryHelper {
	public static SingularInventory createSingularInventory(ItemStack stack) {
		SingularInventory inventory = new SingularInventory();
		inventory.setInventorySlotContents(0, stack);
		return inventory;
	}
}
