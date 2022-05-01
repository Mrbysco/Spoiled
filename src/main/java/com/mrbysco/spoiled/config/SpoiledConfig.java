package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Spoiled;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpoiledConfig {
	public static class Client {
		public final BooleanValue showPercentage;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.push("client");

			showPercentage = builder
					.comment("When enabled makes the food's tooltips show percentages")
					.define("showPercentage", false);

			builder.pop();
		}
	}

	public static class Common {
		public final ConfigValue<List<? extends String>> containerModifier;
		public final IntValue spoilRate;
		public final BooleanValue initializeSpoiling;
		public final BooleanValue spoilEntityContainers;
		public final IntValue defaultSpoilTime;
		public final ConfigValue<String> defaultSpoilItem;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			String[] containers = new String[]
					{
							"minecraft:shulker_box,0"
					};

			containerModifier = builder
					.comment("Determines the spoilrate in specific containers [Syntax: tileentity:spoil_rate]\n" +
							"Examples: \"minecraft:shulker_box,0\" would make shulker boxes not spoil food\n" +
							"\"cookingforblockheads:fridge,0.2\" would make a cooking for blockheads fridge spoil at 20% of the usual spoilrate")
					.defineListAllowEmpty(Collections.singletonList("containerModifier"), () -> Arrays.asList(containers), o -> (o instanceof String));

			spoilRate = builder
					.comment("Defines the default amount of seconds in between which the spoiling updates, " +
							"if this is changed you should update the 'defaultSpoilTime' to accommodate for the extra time [default: 10]")
					.defineInRange("spoilRate", 10, 1, Integer.MAX_VALUE);

			initializeSpoiling = builder
					.comment("When enabled Spoiled initializes spoiling for all vanilla food [default: false]")
					.define("initializeSpoiling", true);

			spoilEntityContainers = builder
					.comment("When enabled Spoiled spoils food in Entities that have a container like Chest Minecarts [default: false]")
					.define("spoilEntityContainers", false);

			defaultSpoilTime = builder
					.comment("Defines the default amount of spoilTime (in second) that is used to initialize Spoiling when 'initializeSpoiling' is enabled [default: 120]")
					.defineInRange("defaultSpoilTime", 120, 1, Integer.MAX_VALUE);

			defaultSpoilItem = builder
					.comment("Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item) [default: 'minecraft:rotten_flesh']")
					.define("defaultSpoilItem", "minecraft:rotten_flesh");

			builder.pop();
		}
	}

	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static final ForgeConfigSpec serverSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		serverSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		Spoiled.LOGGER.debug("Loaded Spoiled's config file {}", configEvent.getConfig().getFileName());
		SpoiledConfigCache.refreshCache();
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		Spoiled.LOGGER.debug("Spoiled's config just got changed on the file system!");
		SpoiledConfigCache.refreshCache();
	}
}
