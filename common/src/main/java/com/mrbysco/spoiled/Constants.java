package com.mrbysco.spoiled;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MOD_NAME = "Spoiled";
	public static final String MOD_ID = "spoiled";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static final String SPOIL_TAG = MOD_PREFIX + "SpoilTimer";
	public static final String SPOIL_TIME_TAG = MOD_PREFIX + "SpoilMaxTime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}