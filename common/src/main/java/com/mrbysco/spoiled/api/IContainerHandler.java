package com.mrbysco.spoiled.api;

import com.mrbysco.spoiled.containers.ContainerTrace;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IContainerHandler {
	boolean isContainer(Player player, ItemStack inventoryStack);

	int getSignature(Player player, ItemStack inventoryStack);

	void spoilItems(Player player, ContainerTrace trace, ItemStack inventoryStack);
}
