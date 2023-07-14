package com.mrbysco.spoiled.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SpoiledTags {
	public static final TagKey<Item> FOODS = TagKey.create(Registries.ITEM, new ResourceLocation("spoiled", "foods"));
	public static final TagKey<Item> FOODS_VANILLA = TagKey.create(Registries.ITEM, new ResourceLocation("spoiled", "foods/vanilla"));
}
