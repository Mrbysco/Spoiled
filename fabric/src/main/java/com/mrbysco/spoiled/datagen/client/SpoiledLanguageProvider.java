package com.mrbysco.spoiled.datagen.client;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class SpoiledLanguageProvider extends FabricLanguageProvider {
	public SpoiledLanguageProvider(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generateTranslations(TranslationBuilder translationBuilder) {
		translationBuilder.add("spoiled.spoiling", "Spoiling progress: ");
		translationBuilder.add("spoiled.spoiling.0", "Fresh");
		translationBuilder.add("spoiled.spoiling.25", "");
		translationBuilder.add("spoiled.spoiling.50", "Stale");
		translationBuilder.add("spoiled.spoiling.75", "Stale");
		translationBuilder.add("spoiled.spoiling.100", "Rotten");

		translationBuilder.add("spoiled.gui.jei.category.spoiling", "Spoiling");
		translationBuilder.add("spoiled.gui.jei.spoil_time", "Spoil Time: %s");
		translationBuilder.add("spoiled.command.blockentity_list.message", "A list of Block Entities has been output into the log");
		translationBuilder.add("spoiled.command.food_list.message", "A list of Food has been output into the log");

		translationBuilder.add("text.autoconfig.spoiled.title", "Spoiled");
		translationBuilder.add("text.autoconfig.spoiled.option.general", "General");
		translationBuilder.add("text.autoconfig.spoiled.option.general.containerModifier", "Container Modifiers");
		translationBuilder.add("text.autoconfig.spoiled.option.general.spoilRate", "Spoil Rate");
		translationBuilder.add("text.autoconfig.spoiled.option.general.initializeSpoiling", "Initialize Spoiling");
		translationBuilder.add("text.autoconfig.spoiled.option.general.mergeSpoilingFood", "Merge Spoiling Food");
		translationBuilder.add("text.autoconfig.spoiled.option.general.spoilEverything", "Spoil Everything");
		translationBuilder.add("text.autoconfig.spoiled.option.general.spoilBlacklist", "Spoil Blacklist");
		translationBuilder.add("text.autoconfig.spoiled.option.general.defaultSpoilTime", "Default Spoil Time");
		translationBuilder.add("text.autoconfig.spoiled.option.general.defaultSpoilItem", "Default Spoil Item");
		translationBuilder.add("text.autoconfig.spoiled.option.compat", "Compat");
		translationBuilder.add("text.autoconfig.spoiled.option.compat.spoilTagBlacklist", "Spoil Tag Blacklist");
	}
}
