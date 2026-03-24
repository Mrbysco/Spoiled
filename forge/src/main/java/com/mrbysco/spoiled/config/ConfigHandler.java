package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

public class ConfigHandler {
	@SubscribeEvent
	public static void onLoad(final ModConfigEvent configEvent) {
		ModConfig config = configEvent.getConfig();
		Constants.LOGGER.debug("Loaded Spoiled's config file {}", config.getFileName());
		if (config.getType() == ModConfig.Type.COMMON) {
			SpoiledConfig.refreshCommonCache();
		}
	}
}
