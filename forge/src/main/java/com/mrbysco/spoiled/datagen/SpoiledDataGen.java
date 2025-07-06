package com.mrbysco.spoiled.datagen;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.client.SpoiledLanguageProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledItemTagProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SpoiledDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new SpoiledRecipeProvider.Runner(packOutput, lookupProvider));
		BlockTagsProvider blockTagsProvider;
		generator.addProvider(true, blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider, Constants.MOD_ID) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {

			}
		});
		generator.addProvider(true, new SpoiledItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));

		generator.addProvider(true, new SpoiledLanguageProvider(packOutput));
	}
}
