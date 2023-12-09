package com.mrbysco.spoiled.util;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

public class SpoilHelper {

	/**
	 * get the Spoil Recipe for the given stack
	 *
	 * @param level The world to get the recipe manager from
	 * @param stack The stack to check against
	 * @return The Spoil Recipe matching the stack or null if none found
	 */
	public static RecipeHolder<SpoilRecipe> getSpoilRecipe(Level level, ItemStack stack) {
		String itemPath = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
		List<String> spoilBlacklist = Services.PLATFORM.getSpoilBlacklist();
		if (!spoilBlacklist.isEmpty() && spoilBlacklist.contains(itemPath)) {
			return null;
		}
		if (Services.PLATFORM.spoilEverything()) {
			final ResourceLocation stackLocation = BuiltInRegistries.ITEM.getKey(stack.getItem());
			if (stack.isEdible()) {
				ItemStack spoilStack = SpoiledConfigCache.getDefaultSpoilItem();
				String result = spoilStack.isEmpty() ? "to_air" : "to_" + BuiltInRegistries.ITEM.getKey(spoilStack.getItem()).getPath();
				String recipePath = "everything_" + stackLocation.getPath() + result;
				return new RecipeHolder<>(new ResourceLocation(Constants.MOD_ID, recipePath), new SpoilRecipe("", Ingredient.of(stack), spoilStack, Services.PLATFORM.getDefaultSpoilTime()));
			}
		} else {
			return level.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get(),
					new SimpleContainer(stack), level).orElse(null);
		}
		return null;
	}

	/**
	 * Check if a stack is spoiling
	 *
	 * @param stack The stack to check
	 * @return true if the stack is spoiling
	 */
	public static boolean isSpoiling(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(Constants.SPOIL_TAG);
	}

	/**
	 * Get the spoil time for a stack
	 *
	 * @param stack The stack to check
	 * @return The spoil time for the stack
	 */
	public static int getSpoilTime(ItemStack stack) {
		return stack.hasTag() ? stack.getTag().getInt(Constants.SPOIL_TAG) : 0;
	}

	/**
	 * Set the spoil time for a stack
	 *
	 * @param stack The stack to set the spoil time for
	 * @param time  The time to set
	 */
	public static void setSpoilTime(ItemStack stack, int time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(Constants.SPOIL_TAG, time);
		stack.setTag(tag);
	}

	/**
	 * Spoil a stack for a player
	 *
	 * @param player The player to give the spoiled item to
	 * @param stack  The stack to spoil
	 * @param recipe The spoil recipe to use
	 */
	public static void spoilItemForPlayer(Player player, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem(player.level().registryAccess()).copy();
		int oldStackCount = stack.getCount();
		stack.shrink(Integer.MAX_VALUE);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			if (!player.addItem(spoiledStack)) {
				ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), spoiledStack);
				player.level().addFreshEntity(itemEntity);
			}
		}
	}

	/**
	 * Spoil a stack for a container
	 *
	 * @param container The container to give the spoiled item to
	 * @param entity    The entity to get the world from
	 * @param stack     The stack to spoil
	 * @param recipe    The spoil recipe to use
	 */
	public static void spoilItemForEntity(Container container, Entity entity, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem(entity.level().registryAccess()).copy();
		int oldStackCount = stack.getCount();
		stack.shrink(Integer.MAX_VALUE);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			int freeSlot = getFreeSlot(container);
			if (freeSlot != -1) {
				container.setItem(freeSlot, spoiledStack);
			} else {
				ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), spoiledStack);
				entity.level().addFreshEntity(itemEntity);
			}
		}
	}

	/**
	 * Spoils a stack with a single item and replaces the instance in the associated location.
	 *
	 * @param level       The level the stack is within
	 * @param stack       The stack to spoil
	 * @param setCallback A callback to set the spoiled stack's location
	 */
	public static void spoilSingleItemAndReplace(Level level, ItemStack stack, Consumer<ItemStack> setCallback) {
		// Check gametime rate since it should spoil similarly to in entity inventory
		// Checking getcount also checks isEmpty
		if (level.getGameTime() % SpoiledConfigCache.spoilRate == 0 && stack.getCount() == 1) {
			RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, stack);
			if (recipeHolder != null) {
				SpoilRecipe recipe = recipeHolder.value();
				SpoilHelper.updateSpoilingStack(stack, recipe);
				if (SpoilHelper.isSpoiled(stack)) {
					ItemStack spoiledStack = recipe.getResultItem(level.registryAccess()).copy();
					int oldStackCount = stack.getCount();
					// Decrement stack just in case there's some weird references
					stack.shrink(Integer.MAX_VALUE);
					if (!spoiledStack.isEmpty()) {
						// Replace stack immediately
						spoiledStack.setCount(oldStackCount);
						setCallback.accept(spoiledStack);
					}
				}
			}
		}
	}

	/**
	 * Get the first free slot in a container
	 *
	 * @param container The container to check
	 * @return The first free slot or -1 if none found
	 */
	private static int getFreeSlot(Container container) {
		for (int i = 0; i < container.getContainerSize(); ++i) {
			if (container.getItem(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Update the spoil time for a stack
	 *
	 * @param stack  The stack to update
	 * @param recipe The spoil recipe to use
	 */
	public static void updateSpoilingStack(ItemStack stack, SpoilRecipe recipe) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.isEmpty()) {
			if (!tag.contains(Constants.SPOIL_TAG)) {
				tag.putInt(Constants.SPOIL_TAG, 0);
			}
			if (!tag.contains(Constants.SPOIL_TIME_TAG)) {
				tag.putInt(Constants.SPOIL_TIME_TAG, recipe.getSpoilTime());
			}
			stack.setTag(tag);
		} else {
			if (tag.contains(Constants.SPOIL_TAG) && tag.contains(Constants.SPOIL_TIME_TAG)) {
				int getOldTime = tag.getInt(Constants.SPOIL_TAG);
				int getMaxTime = tag.getInt(Constants.SPOIL_TIME_TAG);
				if (getMaxTime != recipe.getSpoilTime()) {
					tag.putInt(Constants.SPOIL_TIME_TAG, recipe.getSpoilTime());
				}
				if (getOldTime < getMaxTime) {
					getOldTime++;
					tag.putInt(Constants.SPOIL_TAG, getOldTime);
					stack.setTag(tag);
				}
			}
		}
	}

	/**
	 * Check if a stack is spoiled
	 *
	 * @param stack The stack to check
	 * @return true if the stack is spoiled
	 */
	public static boolean isSpoiled(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(Constants.SPOIL_TAG) && tag.contains(Constants.SPOIL_TIME_TAG)) {
			int getOldTime = tag.getInt(Constants.SPOIL_TAG);
			int getMaxTime = tag.getInt(Constants.SPOIL_TIME_TAG);
			return getOldTime >= getMaxTime;
		}
		return false;
	}

	/**
	 * Check if merging two stacks will have the total under the max stack size
	 *
	 * @param stack1 The first stack
	 * @param stack2 The second stack
	 * @return true if the total will be under the max stack size
	 */
	public static boolean totalUnderMax(ItemStack stack1, ItemStack stack2) {
		return stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize();
	}
}
