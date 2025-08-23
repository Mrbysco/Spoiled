package com.mrbysco.spoiled.datagen;

import com.mrbysco.spoiled.datagen.client.SpoiledLanguageProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledItemTagProvider;
import com.mrbysco.spoiled.datagen.server.SpoiledRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SpoiledDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(SpoiledRecipeProvider::new);
		pack.addProvider(SpoiledLanguageProvider::new);

		pack.addProvider(SpoiledItemTagProvider::new);
	}
}
