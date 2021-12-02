package com.mrbysco.spoiled.util;

import net.minecraft.world.item.ItemStack;

public class InventoryHelper {
	public static SingularInventory createSingularInventory(ItemStack stack) {
		SingularInventory inventory = new SingularInventory();
		inventory.setItem(0, stack);
		return inventory;
	}
}
