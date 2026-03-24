package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoiledConfigCache {

	public static Map<Identifier, Double> containerModifier = new HashMap<>();
	public static Map<Identifier, Double> itemContainerModifier = new HashMap<>();
	public static long spoilRate;


	public static void setSpoilRate(int value) {
		spoilRate = value * 20L;
	}

	public static ItemStack getDefaultSpoilItem() {
		String value = SpoiledConfig.COMMON.defaultSpoilItem.get();
		if (value.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			Item item = BuiltInRegistries.ITEM.getValue(Identifier.tryParse(value));
			if (item != null) {
				return new ItemStack(item);
			} else {
				Constants.LOGGER.error("'defaultSpoilItem' couldn't be parsed, using default");
				return new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.tryParse("rotten_flesh")));
			}
		}
	}

	public static void generateContainerModifier(List<? extends String> containerValues, List<? extends String> itemContainerValues) {
		containerModifier = generateMap(containerValues);
		itemContainerModifier = generateMap(itemContainerValues);
	}

	private static Map<Identifier, Double> generateMap(List<? extends String> configValues) {
		Map<Identifier, Double> modifierMap = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (!configValue.contains(",")) {
					if (configValue.contains(":")) {
						Constants.LOGGER.error("Invalid syntax '{}' found in 'containerModifier' config values, supplying default modifier of 0", configValue);
						modifierMap.put(Identifier.tryParse(configValue), 0D);
					} else {
						Constants.LOGGER.error("Invalid syntax '{}' found in 'containerModifier' config values", configValue);
					}
				} else {
					String[] values = configValue.split(",");
					if (values.length == 2) {
						if (!values[0].contains(":")) {
							Constants.LOGGER.error("Invalid resourcelocation syntax in 'containerModifier'. could not find \":\" in {}", configValue);
							return modifierMap;
						}
						Identifier registry = Identifier.tryParse(values[0]);
						double modifier = NumberUtils.isParsable(values[1]) ? Double.parseDouble(values[1]) : -1;
						modifierMap.put(registry, modifier);
					} else {
						Constants.LOGGER.error("Tried looking for 2 values in 'containerModifier' but found more making the config value {} invalid", configValue);
					}
				}
			}
		}
		return modifierMap;
	}
}
