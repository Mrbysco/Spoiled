package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SpoiledConfig {
	public final static List<String> DEFAULT_SPOIL_BLACKLIST = List.of("minecraft:rotten_flesh", "minecraft:enchanted_golden_apple");

	public static class Client {
		public final BooleanValue showPercentage;

		Client(ModConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.push("client");

			showPercentage = builder
					.comment("When enabled makes the food's tooltips show percentages")
					.define("showPercentage", false);

			builder.pop();
		}
	}

	public static class Common {
		public final ModConfigSpec.ConfigValue<List<? extends String>> containerModifier;
		public final IntValue spoilRate;
		public final BooleanValue initializeSpoiling;
		public final BooleanValue mergeSpoilingFood;
		public final BooleanValue spoilEverything;
		public final ModConfigSpec.ConfigValue<List<? extends String>> spoilBlacklist;
		public final IntValue defaultSpoilTime;
		public final ModConfigSpec.ConfigValue<String> defaultSpoilItem;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			String[] containers = new String[]
					{
							"minecraft:shulker_box,0"
					};

			containerModifier = builder
					.comment("""
							Determines the spoilrate in specific containers [Syntax: tileentity:spoil_rate]
							Examples: "minecraft:shulker_box,0" would make shulker boxes not spoil food
							"cookingforblockheads:fridge,0.2" would make a cooking for blockheads fridge spoil at 20% of the usual spoilrate""")
					.defineListAllowEmpty(List.of("containerModifier"), () -> List.of(containers), o -> (o instanceof String));

			spoilRate = builder
					.comment("""
							Defines the default total amount of spoiling updates for each food item\s
							(For example, a defaultSpoilTime of 40 means any default food item will have 40 total updates before it spoils.\s
							This can be altered when custom food spoiling is set using a datapack) [default: 30]""")
					.defineInRange("spoilRate", 30, 1, Integer.MAX_VALUE);

			initializeSpoiling = builder
					.comment("When enabled Spoiled initializes spoiling for all vanilla food [default: true]")
					.define("initializeSpoiling", true);

			mergeSpoilingFood = builder
					.comment("When enabled enables a special recipe to merge spoiling food together [default: false]")
					.define("mergeSpoilingFood", false);

			spoilEverything = builder
					.comment("When enabled Spoiled makes every edible item spoil into the specified Spoil Item (This overwrites json spoiling completely) [default: false]")
					.define("spoilEverything", false);

			spoilBlacklist = builder
					.comment("Defines a list of items that are never allowed to spoil")
					.defineListAllowEmpty(List.of("spoilBlacklist"), () -> DEFAULT_SPOIL_BLACKLIST, o -> (o instanceof String));

			defaultSpoilTime = builder
					.comment("Defines the total amount of spoiling updates that is used by the default initialized spoiling when 'initializeSpoiling' is enabled \n" +
							"(If the 'spoilRate' is 10 and the 'defaultSpoilTime' is set to 20 then the food will spoil after 20 * 10 seconds = 200 seconds) [default: 40]")
					.defineInRange("defaultSpoilTime", 40, 1, Integer.MAX_VALUE);

			defaultSpoilItem = builder
					.comment("Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item) [default: 'minecraft:rotten_flesh']")
					.define("defaultSpoilItem", "minecraft:rotten_flesh");

			builder.pop();
		}
	}

	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static final ModConfigSpec serverSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		serverSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent configEvent) {
		ModConfig config = configEvent.getConfig();
		Constants.LOGGER.debug("Loaded Spoiled's config file {}", config.getFileName());
		refreshCache(config.getType());
	}

	private static void refreshCache(ModConfig.Type type) {
		if (type == ModConfig.Type.COMMON) {
			SpoiledConfigCache.setSpoilRate(SpoiledConfig.COMMON.spoilRate.get());
			SpoiledConfigCache.generateContainerModifier(SpoiledConfig.COMMON.containerModifier.get());
		}
	}
}
