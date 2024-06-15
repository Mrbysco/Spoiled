package com.mrbysco.spoiled.util;

import com.mrbysco.spoiled.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SpoiledTags {
	public static final TagKey<Item> FOODS = TagKey.create(Registries.ITEM, Constants.modLoc("foods"));
	public static final TagKey<Item> FOODS_BLACKLIST = TagKey.create(Registries.ITEM, Constants.modLoc("foods_blacklist"));
}
