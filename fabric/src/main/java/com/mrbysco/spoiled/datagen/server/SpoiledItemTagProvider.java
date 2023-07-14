package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends ItemTagsProvider {
	public SpoiledItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, FabricTagProvider.BlockTagProvider blockTagsProvider) {
		super(output, completableFuture, blockTagsProvider.contentsGetter());
	}

	@Override
	public void addTags(HolderLookup.Provider lookupProvider) {
		addModFood(List.of(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE));
	}

	private void addModFood(List<Item> blacklist) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (!blacklist.contains(item) && item.isEdible() && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft")) {
				this.tag(SpoiledTags.FOODS_VANILLA).add(item);
			}
		}
		this.tag(SpoiledTags.FOODS).addTag(SpoiledTags.FOODS_VANILLA);
	}
}