package com.mrbysco.spoiled.platform.services;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IPlatformHelper {

	/**
	 * Returns true if the mod is loaded
	 *
	 * @return If the mod is loaded
	 */
	boolean isModLoaded(String modID);

	/**
	 * When enabled makes the food's tooltips show percentages
	 *
	 * @return The configured showPercentage value
	 */
	boolean showPercentage();

	/**
	 * Gets the configured container modifiers
	 *
	 * @return The configured containerModifier value
	 */
	List<String> getContainerModifier();

	/**
	 * Gets the configured spoil rate
	 *
	 * @return The configured spoilRate value
	 */
	int getSpoilRate();

	/**
	 * When enabled Spoiled initializes spoiling for all vanilla food
	 *
	 * @return The configured initializeSpoiling value
	 */
	boolean initializeSpoiling();

	/**
	 * Enables a special recipe to merge spoiling food together
	 *
	 * @return The configured mergeSpoilingFood value
	 */
	boolean mergeSpoilingFood();

	/**
	 * When enabled Spoiled makes every edible item spoil into the specified Spoil Item (This overwrites json spoiling completely)
	 *
	 * @return The configured spoilEverything value
	 */
	boolean spoilEverything();

	/**
	 * Defines a list of items that are never allowed to spoil
	 *
	 * @return The configured spoilBlacklist value
	 */
	List<String> getSpoilBlacklist();

	/**
	 * Defines the total amount of spoiling updates that is used by the default initialized spoiling when 'initializeSpoiling' is enabled
	 * (If the 'spoilRate' is 10 and the 'defaultSpoilTime' is set to 20 then the food will spoil after 20 * 10 seconds = 200 seconds)
	 *
	 * @return The configured defaultSpoilTime value
	 */
	int getDefaultSpoilTime();

	/**
	 * Defines the item the foods vanilla foods will turn into when spoiled (if empty it will clear the spoiling item)
	 *
	 * @return The configured defaultSpoilItem value
	 */
	String getDefaultSpoilItem();


	/**
	 * Check if a stack is capable of spoiling
	 *
	 * @return true if the stack can spoil
	 */
	boolean canSpoil(ItemStack stack);
}
