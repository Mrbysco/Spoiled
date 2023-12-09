package com.mrbysco.spoiled.datagen;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.datagen.client.SpoiledLanguageProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledItemTagProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpoiledDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new SpoiledRecipeProvider(packOutput, lookupProvider));
			BlockTagsProvider blockTagsProvider;
			generator.addProvider(event.includeServer(), blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider, Constants.MOD_ID, helper) {
				@Override
				protected void addTags(HolderLookup.Provider provider) {

				}
			});
			generator.addProvider(event.includeServer(), new SpoiledItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeServer(), new SpoiledLanguageProvider(packOutput));
		}
	}
}
