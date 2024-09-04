package com.mrbysco.spoiled.platform;

import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.platform.services.IPlatformHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isModLoaded(String modID) {
		return ModList.get().isLoaded(modID);
	}

	@Override
	public boolean showPercentage() {
		return SpoiledConfig.CLIENT.showPercentage.get();
	}

	@Override
	public List<String> getContainerModifier() {
		return new ArrayList<>(SpoiledConfig.COMMON.containerModifier.get());
	}

	@Override
	public int getSpoilRate() {
		return SpoiledConfig.COMMON.spoilRate.get();
	}

	@Override
	public boolean initializeSpoiling() {
		return SpoiledConfig.COMMON.initializeSpoiling.get();
	}

	@Override
	public boolean mergeSpoilingFood() {
		return SpoiledConfig.COMMON.mergeSpoilingFood.get();
	}

	@Override
	public boolean spoilEverything() {
		return SpoiledConfig.COMMON.spoilEverything.get();
	}

	@Override
	public List<String> getSpoilBlacklist() {
		return new ArrayList<>(SpoiledConfig.COMMON.spoilBlacklist.get());
	}

	@Override
	public int getDefaultSpoilTime() {
		return SpoiledConfig.COMMON.defaultSpoilTime.get();
	}

	@Override
	public String getDefaultSpoilItem() {
		return SpoiledConfig.COMMON.defaultSpoilItem.get();
	}

	@Override
	public boolean canSpoil(ItemStack stack) {
		if (SpoiledConfig.COMMON.saltCompat.get() && ModList.get().isLoaded("salt")) {
			CompoundTag tag = stack.getTag();
			return tag == null || !tag.contains("Salted");
		}
		return true;
	}
}
