package com.mrbysco.spoiled.containers.handlers;

import com.mrbysco.spoiled.api.IContainerHandler;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.containers.ContainerTrace;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Stream;

public class HandlerBundle implements IContainerHandler {
	@Override
	public boolean isContainer(Player player, ItemStack inventoryStack) {
		return inventoryStack != null && inventoryStack.getCount() == 1 &&
				(inventoryStack.getItem() == Items.BUNDLE || inventoryStack.getItem() instanceof BundleItem);
	}

	@Override
	public int getSignature(Player player, ItemStack inventoryStack) {
		return inventoryStack.hashCode();
	}

	@Override
	public void spoilItems(Player player, ContainerTrace trace, ItemStack inventoryStack) {
		Level level = player.level();
		ResourceLocation containerKey = BuiltInRegistries.ITEM.getKey(inventoryStack.getItem());
		double modifier = SpoiledConfigCache.itemContainerModifier.getOrDefault(containerKey, 1.0D);
		if (modifier <= 0) return;

		List<ItemStack> itemStacks = getContents(inventoryStack).map(stack -> {
			RecipeHolder<SpoilRecipe> holder = SpoilHelper.getSpoilRecipe(level, stack);
			if (holder == null) return stack;
			if (modifier != 1.0D && level.getRandom().nextDouble() > modifier) return stack;

			SpoilRecipe recipe = holder.value();
			SpoilHelper.updateSpoilingStack(stack, recipe);
			if (!SpoilHelper.isSpoiled(stack)) return stack;

			ItemStack spoiledStack = recipe.getResultItem(level.registryAccess()).copy();
			if (spoiledStack.isEmpty()) return ItemStack.EMPTY;
			spoiledStack.setCount(stack.getCount());
			return spoiledStack;
		}).filter(stack -> !stack.isEmpty()).toList();

		setItemList(inventoryStack, itemStacks);
	}

	private Stream<ItemStack> getContents(ItemStack bundleStack) {
		BundleContents contents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		return contents.itemCopyStream();
	}

	private void setItemList(ItemStack itemStack, List<ItemStack> itemStacks) {
		BundleContents contents = new BundleContents(itemStacks);
		itemStack.set(DataComponents.BUNDLE_CONTENTS, contents);
	}
}
