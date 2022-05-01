package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Spoiled;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoiledConfigCache {
	public static boolean showPercentage;

	public static Map<ResourceLocation, Double> containerModifier;
	public static long spoilRate;

	public static void refreshCache() {
		Spoiled.LOGGER.info("Refreshing config cache");
		showPercentage = SpoiledConfig.CLIENT.showPercentage.get();

		generateContainerModifier(SpoiledConfig.COMMON.containerModifier.get());
		spoilRate = SpoiledConfig.COMMON.spoilRate.get() * 20L;
	}

	public static ItemStack getDefaultSpoilItem() {
		String value = SpoiledConfig.COMMON.defaultSpoilItem.get();
		if (value.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(value));
			if (item != null) {
				return new ItemStack(item);
			} else {
				Spoiled.LOGGER.error("'defaultSpoilItem' couldn't be parsed, using default");
				return new ItemStack(ForgeRegistries.ITEMS.getValue(Items.ROTTEN_FLESH.getRegistryName()));
			}
		}
	}

	public static void generateContainerModifier(List<? extends String> configValues) {
		HashMap<ResourceLocation, Double> modifierMap = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (!configValue.contains(",")) {
					if (configValue.contains(":")) {
						Spoiled.LOGGER.error(String.format("Invalid syntax '%s' found in 'containerModifier' config values, supplying default modifier of 0", configValue));
						modifierMap.put(new ResourceLocation(configValue), 0D);
					} else {
						Spoiled.LOGGER.error(String.format("Invalid syntax '%s' found in 'containerModifier' config values", configValue));
					}
				} else {
					String[] values = configValue.split(",");
					if (values.length == 2) {
						if (!values[0].contains(":")) {
							Spoiled.LOGGER.error(String.format("Invalid resourcelocation syntax in 'containerModifier'. could not find \":\" in %s", configValue));
							return;
						}
						ResourceLocation registry = new ResourceLocation(values[0]);
						double modifier = NumberUtils.isParsable(values[1]) ? Double.parseDouble(values[1]) : -1;
						modifierMap.put(registry, modifier);
					} else {
						Spoiled.LOGGER.error(String.format("Tried looking for 2 values in 'containerModifier' but found more making the config value %s invalid", configValue));
					}
				}
			}
		}
		containerModifier = modifierMap;
	}
}
