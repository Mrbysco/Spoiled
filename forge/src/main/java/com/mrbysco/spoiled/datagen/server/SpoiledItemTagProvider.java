package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends ItemTagsProvider {
	public SpoiledItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
	}

	@Override
	public void addTags(@NotNull HolderLookup.Provider lookupProvider) {
		this.tag(SpoiledTags.FOODS_BLACKLIST).add(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE);
		this.tag(SpoiledTags.FOODS).addTag(Tags.Items.FOODS);
	}
}