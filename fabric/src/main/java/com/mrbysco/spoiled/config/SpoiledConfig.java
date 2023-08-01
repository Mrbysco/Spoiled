package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.List;

@Config(name = Constants.MOD_ID)
public class SpoiledConfig implements ConfigData {

	@CollapsibleObject
	public General general = new General();

	public static class General {
		@Comment("""
				Determines the spoilrate in specific containers [Syntax: tileentity:spoil_rate]
				Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food
				"cookingforblockheads:fridge,0.2" would make a cooking for blockheads fridge spoil at 20% of the usual spoilrate""")
		public List<String> containerModifier = List.of("minecraft:shulker_box,0");

		@Comment("""
				Defines the default total amount of spoiling updates for each food item\s
							(For example, a defaultSpoilTime of 40 means any default food item will have 40 total updates before it spoils.\s
							This can be altered when custom food spoiling is set using a datapack) [default: 30]""")
		public int spoilRate = 30;

		@Comment("When enabled Spoiled initializes spoiling for all vanilla food [default: true]")
		public boolean initializeSpoiling = true;

		@Comment("When enabled enables a special recipe to merge spoiling food together [default: false]")
		public boolean mergeSpoilingFood = false;

		@Comment("When enabled Spoiled makes every edible item spoil into the specified Spoil Item (This overwrites json spoiling completely) [default: false]")
		public boolean spoilEverything = false;

		@Comment("Defines a list of items that are never allowed to spoil")
		public List<String> spoilBlacklist = List.of("minecraft:rotten_flesh", "minecraft:enchanted_golden_apple");

		@Comment("Defines the total amount of spoiling updates that is used by the default initialized spoiling when 'initializeSpoiling' is enabled \n" +
				"(If the 'spoilRate' is 10 and the 'defaultSpoilTime' is set to 20 then the food will spoil after 20 * 10 seconds = 200 seconds) [default: 40]")
		public int defaultSpoilTime = 40;

		@Comment("Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item) [default: 'minecraft:rotten_flesh']")
		public String defaultSpoilItem = "minecraft:rotten_flesh";

	}
}