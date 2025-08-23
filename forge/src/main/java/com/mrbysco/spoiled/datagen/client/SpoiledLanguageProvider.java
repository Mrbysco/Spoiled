package com.mrbysco.spoiled.datagen.client;

import com.mrbysco.spoiled.Constants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.Nullable;

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

		addConfig("title", "Spoiled", "Spoiled Config");
		addConfig("General", "General", "General Settings");
		addConfig("containerModifier", "Container Modifiers", """
							Determines the spoilrate in specific containers [Syntax: tileentity:spoil_rate]
							Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food
							"cookingforblockheads:fridge,0.2" would make a cooking for blockheads fridge spoil at 20% of the usual spoilrate""");
		addConfig("itemContainerModifier", "Item Container Modifiers", """
							Determines the spoilrate in specific item containers [Syntax: modid:item_id]
							Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food""");
		addConfig("spoilRate", "Spoil Rate", """
							Defines the default total amount of spoiling updates for each food item\s
							(For example, a defaultSpoilTime of 40 means any default food item will have 40 total updates before it spoils.\s
							This can be altered when custom food spoiling is set using a datapack) [default: 30]""");
		addConfig("initializeSpoiling", "Initialize Spoiling", "When enabled Spoiled initializes spoiling for all vanilla food [default: true]");
		addConfig("mergeSpoilingFood", "Merge Spoiling Food", "When enabled enables a special recipe to merge spoiling food together [default: false]");
		addConfig("spoilEverything", "Spoil Everything", "When enabled Spoiled makes every edible item spoil into the specified Spoil Item (This overwrites json spoiling completely) [default: false]");
		addConfig("spoilBlacklist", "Spoil Blacklist", "Defines a list of items that are never allowed to spoil");
		addConfig("defaultSpoilTime", "Default Spoil Time", "Defines the total amount of spoiling updates that is used by the default initialized spoiling when 'initializeSpoiling' is enabled \n" +
				"(If the 'spoilRate' is 10 and the 'defaultSpoilTime' is set to 20 then the food will spoil after 20 * 10 seconds = 200 seconds) [default: 40]");
		addConfig("defaultSpoilItem", "Default Spoil Item", "Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item) [default: 'minecraft:rotten_flesh']");
		addConfig("client", "Client", "Client Settings");
		addConfig("showPercentage", "Show Percentage", "When enabled makes the food's tooltips show percentages");
	}

	/**
	 * Add the translation for a config entry
	 *
	 * @param path        The path of the config entry
	 * @param name        The name of the config entry
	 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
	 */
	private void addConfig(String path, String name, @Nullable String description) {
		this.add("spoiled.configuration." + path, name);
		if (description != null && !description.isEmpty())
			this.add("spoiled.configuration." + path + ".tooltip", description);
	}
}
