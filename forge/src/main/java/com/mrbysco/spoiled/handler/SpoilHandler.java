package com.mrbysco.spoiled.handler;

import com.google.common.collect.Lists;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.mixin.RandomizableContainerBlockEntityAccessor;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.ChunkHelper;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SpoilHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldTick(LevelTickEvent event) {
		if (event.phase == Phase.END && event.side == LogicalSide.SERVER && event.level.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
			ServerLevel level = (ServerLevel) event.level;
			if (level.dimension() != Level.OVERWORLD) return;
			List<BlockPos> blockEntityPositions = ChunkHelper.getBlockEntityPositions(level).stream().filter(pos -> level.isAreaLoaded(pos, 1)).toList();
			if (!blockEntityPositions.isEmpty()) {
				for (BlockPos pos : blockEntityPositions) {
					BlockEntity be = level.getBlockEntity(pos);
					if (be != null && !be.isRemoved() && be.hasLevel() && be.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
						if (be instanceof RandomizableContainerBlockEntity randomizeInventory && ((RandomizableContainerBlockEntityAccessor) randomizeInventory).getLootTable() != null)
							continue;

						ResourceLocation location = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(be.getType());
						double spoilRate = 1.0D;
						if (location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
							spoilRate = SpoiledConfigCache.containerModifier.get(location);
						}
						boolean spoilFlag = spoilRate == 1.0 || (spoilRate > 0 && level.random.nextDouble() <= spoilRate);
						if (spoilFlag) {
							IItemHandler itemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
							if (itemHandler != null && itemHandler.getSlots() > 0) {
								for (int i = 0; i < itemHandler.getSlots(); i++) {
									ItemStack stack = itemHandler.getStackInSlot(i);
									if (stack != null && !stack.isEmpty()) {
										int slot = i;
										SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, stack);
										if (recipe != null) {
											SpoilHelper.updateSpoilingStack(stack, recipe);
											if (SpoilHelper.isSpoiled(stack)) {
												spoilItemInHandler(itemHandler, slot, stack, recipe, level.registryAccess());
											}
										}
									}
								}
							}
						}
					}
				}
			}
			List<Entity> entityList = Lists.newArrayList();
			level.getAllEntities().forEach(entityList::add);
			List<Entity> containerEntities = entityList.stream().filter(e -> e instanceof Container && e.isAlive()).toList();
			for (Entity entity : containerEntities) {
				updateContainer(level, entity, (Container) entity);
			}
		}
	}

	public static void spoilItemInHandler(IItemHandler itemHandler, int slot, ItemStack stack, SpoilRecipe recipe, RegistryAccess registryAccess) {
		ItemStack spoiledStack = recipe.getResultItem(registryAccess).copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			itemHandler.insertItem(slot, spoiledStack, false);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == Phase.END && !event.player.level().isClientSide &&
				event.player.level().getGameTime() % SpoiledConfigCache.spoilRate == 0 && !event.player.getAbilities().instabuild) {
			updateInventory(event.player);
		}
	}

	private void updateInventory(Player player) {
		final Level level = player.level();
		int invCount = player.getInventory().getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
					stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								ItemStack nestedStack = itemHandler.getStackInSlot(j);
								if (nestedStack != null && !nestedStack.isEmpty()) {
									SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, nestedStack);
									if (recipe != null) {
										SpoilHelper.updateSpoilingStack(nestedStack, recipe);
										if (SpoilHelper.isSpoiled(nestedStack)) {
											spoilItemInHandler(itemHandler, j, nestedStack, recipe, level.registryAccess());
										}
									}
								}
							}
						}
					});
				} else {
					SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, stack);
					if (recipe != null) {
						SpoilHelper.updateSpoilingStack(stack, recipe);
						if (SpoilHelper.isSpoiled(stack)) {
							SpoilHelper.spoilItemForPlayer(player, stack, recipe);
						}
					}
				}
			}
		}
	}

	private void updateContainer(Level level, Entity entity, Container container) {
		int invCount = container.getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
					stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								ItemStack nestedStack = itemHandler.getStackInSlot(j);
								if (nestedStack != null && !nestedStack.isEmpty()) {
									SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, nestedStack);
									if (recipe != null) {
										SpoilHelper.updateSpoilingStack(nestedStack, recipe);
										if (SpoilHelper.isSpoiled(nestedStack)) {
											spoilItemInHandler(itemHandler, j, nestedStack, recipe, level.registryAccess());
										}
									}
								}
							}
						}
					});
				} else {
					SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, stack);
					if (recipe != null) {
						SpoilHelper.updateSpoilingStack(stack, recipe);
						if (SpoilHelper.isSpoiled(stack)) {
							SpoilHelper.spoilItemForEntity(container, entity, stack, recipe);
						}
					}
				}
			}
		}
	}
}
