package com.mrbysco.spoiled.datagen.server;

import com.mrbysco.spoiled.util.SpoiledTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SpoiledItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public SpoiledItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	public void addTags(@NotNull HolderLookup.Provider lookupProvider) {
		List<ResourceKey<Item>> blacklist = List.of(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE)
				.stream().map(Item::builtInRegistryHolder).map(Holder.Reference::key).toList();
		this.builder(SpoiledTags.FOODS_BLACKLIST).addAll(blacklist);
		this.builder(SpoiledTags.FOODS).addOptionalTag(ConventionalItemTags.FOODS);
	}
}