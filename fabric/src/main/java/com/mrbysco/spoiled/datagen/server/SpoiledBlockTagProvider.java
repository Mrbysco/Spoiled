package com.mrbysco.spoiled.datagen.server;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SpoiledBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public SpoiledBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	public void addTags(HolderLookup.Provider lookupProvider) {

	}
}