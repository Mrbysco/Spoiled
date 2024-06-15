package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoiledConfigCache {

	public static Map<ResourceLocation, Double> containerModifier;
	public static long spoilRate;


	public static void setSpoilRate(int value) {
		spoilRate = value * 20L;
	}

	public static ItemStack getDefaultSpoilItem() {
		String value = Services.PLATFORM.getDefaultSpoilItem();
		if (value.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(value));
			if (item != null) {
				return new ItemStack(item);
			} else {
				Constants.LOGGER.error("'defaultSpoilItem' couldn't be parsed, using default");
				return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.tryParse("rotten_flesh")));
			}
		}
	}

	public static void generateContainerModifier(List<? extends String> configValues) {
		HashMap<ResourceLocation, Double> modifierMap = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (!configValue.contains(",")) {
					if (configValue.contains(":")) {
						Constants.LOGGER.error(String.format("Invalid syntax '%s' found in 'containerModifier' config values, supplying default modifier of 0", configValue));
						modifierMap.put(ResourceLocation.tryParse(configValue), 0D);
					} else {
						Constants.LOGGER.error(String.format("Invalid syntax '%s' found in 'containerModifier' config values", configValue));
					}
				} else {
					String[] values = configValue.split(",");
					if (values.length == 2) {
						if (!values[0].contains(":")) {
							Constants.LOGGER.error(String.format("Invalid resourcelocation syntax in 'containerModifier'. could not find \":\" in %s", configValue));
							return;
						}
						ResourceLocation registry = ResourceLocation.tryParse(values[0]);
						double modifier = NumberUtils.isParsable(values[1]) ? Double.parseDouble(values[1]) : -1;
						modifierMap.put(registry, modifier);
					} else {
						Constants.LOGGER.error(String.format("Tried looking for 2 values in 'containerModifier' but found more making the config value %s invalid", configValue));
					}
				}
			}
		}
		containerModifier = modifierMap;
	}
}
