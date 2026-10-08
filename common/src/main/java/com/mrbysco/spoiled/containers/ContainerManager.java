package com.mrbysco.spoiled.containers;

import com.mrbysco.spoiled.api.IContainerHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class ContainerManager {
	private final ArrayList<IContainerHandler> handlers;

	public ContainerManager() {
		handlers = new ArrayList<IContainerHandler>();
	}

	public boolean register(IContainerHandler handler) {
		return handlers.add(handler);
	}

	public boolean spoilItems(Player player, ContainerTrace trace, ItemStack inventoryStack) {
		boolean handled = false;
		for (IContainerHandler handler : handlers) {
			if (handler.isContainer(player, inventoryStack)) {
				handled = true;
				int sig = handler.getSignature(player, inventoryStack);
				if (trace.push(sig)) {
					handler.spoilItems(player, trace, inventoryStack);
				}
			}
		}
		return handled;
	}
}
