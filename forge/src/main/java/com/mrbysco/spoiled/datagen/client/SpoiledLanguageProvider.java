package com.mrbysco.spoiled.datagen.client;

import com.mrbysco.spoiled.Constants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class SpoiledLanguageProvider extends LanguageProvider {
	public SpoiledLanguageProvider(PackOutput packOutput) {
		super(packOutput, Constants.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("spoiled.spoiling", "Spoiling progress: ");
		add("spoiled.spoiling.0", "Fresh");
		add("spoiled.spoiling.25", "");
		add("spoiled.spoiling.50", "Stale");
		add("spoiled.spoiling.75", "Stale");
		add("spoiled.spoiling.100", "Rotten");

		add("spoiled.gui.jei.category.spoiling", "Spoiling");
		add("spoiled.gui.jei.spoil_time", "Spoil Time: %s");
		add("spoiled.command.blockentity_list.message", "A list of Block Entities has been output into the log");
		add("spoiled.command.food_list.message", "A list of Food has been output into the log");
	}
}
