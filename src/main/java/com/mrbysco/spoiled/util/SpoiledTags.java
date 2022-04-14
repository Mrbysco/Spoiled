package com.mrbysco.spoiled.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SpoiledTags {
	public static final TagKey<Item> FOODS = ItemTags.create(new ResourceLocation("spoiled", "foods"));
	public static final TagKey<Item> FOODS_VANILLA = ItemTags.create(new ResourceLocation("spoiled", "foods/vanilla"));
}
