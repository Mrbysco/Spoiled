package com.mrbysco.spoiled.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class SpoiledTags {
	public static final ITag.INamedTag<Item> FOODS = ItemTags.bind(new ResourceLocation("spoiled", "foods").toString());
	public static final ITag.INamedTag<Item> FOODS_VANILLA = ItemTags.bind(new ResourceLocation("spoiled", "foods/vanilla").toString());
}
