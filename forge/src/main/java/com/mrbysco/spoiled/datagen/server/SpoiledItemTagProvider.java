package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Map;
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
		for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
			Item item = entry.getValue();
			ResourceLocation id = entry.getKey().location();
			if (!blacklist.contains(item) && item.isEdible() && id.getNamespace().equals("minecraft")) {
				this.tag(SpoiledTags.FOODS_VANILLA).add(item);
			}
		}
		this.tag(SpoiledTags.FOODS).addTag(SpoiledTags.FOODS_VANILLA);
	}
}