package com.mrbysco.spoiled;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MOD_NAME = "Spoiled";
	public static final String MOD_ID = "spoiled";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static final String SPOIL_TAG = MOD_PREFIX + "SpoilTimer";
	public static final String SPOIL_TIME_TAG = MOD_PREFIX + "SpoilMaxTime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static final TagKey<Item> BLACKLIST = TagKey.create(Registries.ITEM, modLoc("blacklist"));

	public static ResourceLocation modLoc(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}