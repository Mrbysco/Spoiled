package com.mrbysco.spoiled.platform;

import com.mrbysco.spoiled.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isModLoaded(String modID) {
		return ModList.get().isLoaded(modID);
	}
}
