package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends ItemTagsProvider {
	public SpoiledItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
								  CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTagProvider, Constants.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags(HolderLookup.Provider lookupProvider) {
		addModFood(List.of(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE));
	}

	private void addModFood(List<Item> blacklist) {
		for (Item item : ForgeRegistries.ITEMS) {
			if (!blacklist.contains(item) && item.isEdible() && ForgeRegistries.ITEMS.getKey(item).getNamespace().equals("minecraft")) {
				this.tag(SpoiledTags.FOODS_VANILLA).add(item);
			}
		}
		this.tag(SpoiledTags.FOODS).addTag(SpoiledTags.FOODS_VANILLA);
	}
}