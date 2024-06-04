package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends ItemTagsProvider {
	public SpoiledItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, FabricTagProvider.BlockTagProvider blockTagsProvider) {
		super(output, completableFuture, blockTagsProvider.contentsGetter());
	}

	@Override
	public void addTags(HolderLookup.Provider lookupProvider) {
		this.tag(SpoiledTags.FOODS_BLACKLIST).add(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE);
		this.tag(SpoiledTags.FOODS).addOptionalTag(ConventionalItemTags.FOODS.location());
	}
}