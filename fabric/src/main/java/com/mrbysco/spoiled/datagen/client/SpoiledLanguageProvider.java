package com.mrbysco.spoiled.datagen.client;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SpoiledLanguageProvider extends FabricLanguageProvider {
	public SpoiledLanguageProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
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

		addConfig(translationBuilder, "title", "Spoiled", "Spoiled Config");
		addConfig(translationBuilder, "General", "General", "General Settings");
		addConfig(translationBuilder, "containerModifier", "Container Modifiers", """
							Determines the spoilrate in specific containers [Syntax: tileentity:spoil_rate]
							Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food
							"cookingforblockheads:fridge,0.2" would make a cooking for blockheads fridge spoil at 20% of the usual spoilrate""");
		addConfig(translationBuilder, "itemContainerModifier", "Item Container Modifiers", """
							Determines the spoilrate in specific item containers [Syntax: modid:item_id]
							Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food""");
		addConfig(translationBuilder, "spoilRate", "Spoil Rate", """
							Defines the default total amount of spoiling updates for each food item\s
							(For example, a defaultSpoilTime of 40 means any default food item will have 40 total updates before it spoils.\s
							This can be altered when custom food spoiling is set using a datapack) [default: 30]""");
		addConfig(translationBuilder, "initializeSpoiling", "Initialize Spoiling", "When enabled Spoiled initializes spoiling for all vanilla food [default: true]");
		addConfig(translationBuilder, "mergeSpoilingFood", "Merge Spoiling Food", "When enabled enables a special recipe to merge spoiling food together [default: false]");
		addConfig(translationBuilder, "spoilEverything", "Spoil Everything", "When enabled Spoiled makes every edible item spoil into the specified Spoil Item (This overwrites json spoiling completely) [default: false]");
		addConfig(translationBuilder, "spoilBlacklist", "Spoil Blacklist", "Defines a list of items that are never allowed to spoil");
		addConfig(translationBuilder, "defaultSpoilTime", "Default Spoil Time", "Defines the total amount of spoiling updates that is used by the default initialized spoiling when 'initializeSpoiling' is enabled \n" +
				"(If the 'spoilRate' is 10 and the 'defaultSpoilTime' is set to 20 then the food will spoil after 20 * 10 seconds = 200 seconds) [default: 40]");
		addConfig(translationBuilder, "defaultSpoilItem", "Default Spoil Item", "Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item) [default: 'minecraft:rotten_flesh']");
		addConfig(translationBuilder, "client", "Client", "Client Settings");
		addConfig(translationBuilder, "showPercentage", "Show Percentage", "When enabled makes the food's tooltips show percentages");

		addConfig(translationBuilder, "compatibility", "Compatibility", "Compatibility Settings");
		addConfig(translationBuilder, "ignoredComponents", "Ignored Component Identifiers", "Any components with these ids will cause items not to spoil when they are present on the item");

	}

	private void addConfig(TranslationBuilder translationBuilder, String path, String name, @Nullable String description) {
		translationBuilder.add("spoiled.configuration." + path, name);
		if (description != null && !description.isEmpty())
			translationBuilder.add("spoiled.configuration." + path + ".tooltip", description);
	}
}
