package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends ItemTagsProvider {
	public SpoiledItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
	                              CompletableFuture<TagLookup<Block>> blockTagProvider) {
		super(output, lookupProvider, blockTagProvider, Constants.MOD_ID);
	}

	@Override
	public void addTags(@NotNull HolderLookup.Provider lookupProvider) {
		this.tag(SpoiledTags.FOODS_BLACKLIST).add(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE);
		this.tag(SpoiledTags.FOODS).addTag(Tags.Items.FOODS);
	}
}