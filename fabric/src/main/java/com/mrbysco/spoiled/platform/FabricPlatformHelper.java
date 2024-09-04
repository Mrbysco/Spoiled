package com.mrbysco.spoiled.platform;

import com.mrbysco.spoiled.SpoiledFabric;
import com.mrbysco.spoiled.SpoiledFabricClient;
import com.mrbysco.spoiled.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FabricPlatformHelper implements IPlatformHelper {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean showPercentage() {
		return SpoiledFabricClient.config.client.showPercentage;
	}

	@Override
	public List<String> getContainerModifier() {
		return SpoiledFabric.config.get().general.containerModifier;
	}

	@Override
	public int getSpoilRate() {
		return SpoiledFabric.config.get().general.spoilRate;
	}

	@Override
	public boolean initializeSpoiling() {
		return SpoiledFabric.config.get().general.initializeSpoiling;
	}

	@Override
	public boolean mergeSpoilingFood() {
		return SpoiledFabric.config.get().general.mergeSpoilingFood;
	}

	@Override
	public boolean spoilEverything() {
		return SpoiledFabric.config.get().general.spoilEverything;
	}

	@Override
	public List<String> getSpoilBlacklist() {
		return SpoiledFabric.config.get().general.spoilBlacklist;
	}

	@Override
	public int getDefaultSpoilTime() {
		return SpoiledFabric.config.get().general.defaultSpoilTime;
	}

	@Override
	public String getDefaultSpoilItem() {
		return SpoiledFabric.config.get().general.defaultSpoilItem;
	}

	@Override
	public boolean canSpoil(ItemStack stack) {
		return true;
	}
}
