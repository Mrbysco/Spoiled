package com.mrbysco.spoiled.handler;

import com.google.common.collect.Lists;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.mixin.RandomizableContainerBlockEntityAccessor;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.ChunkHelper;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.List;

public class SpoilHandler {

	public static InteractionResult onWorldTick(ServerLevel level) {
		if (level.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
			if (level.dimension() != Level.OVERWORLD) return InteractionResult.PASS;
			List<BlockPos> blockEntityPositions = ChunkHelper.getBlockEntityPositions(level).stream().filter(pos -> isAreaLoaded(level, pos, 1)).toList();
			if (!blockEntityPositions.isEmpty()) {
				for (BlockPos pos : blockEntityPositions) {
					BlockEntity be = level.getBlockEntity(pos);
					if (be != null && !be.isRemoved() && be.hasLevel() && be instanceof Container container) {
						if (be instanceof RandomizableContainerBlockEntity randomizeInventory && ((RandomizableContainerBlockEntityAccessor) randomizeInventory).getLootTable() != null)
							continue;

						ResourceLocation location = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType());
						double spoilRate = 1.0D;
						if (location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
							spoilRate = SpoiledConfigCache.containerModifier.get(location);
						}
						boolean spoilFlag = spoilRate == 1.0 || (spoilRate > 0 && level.random.nextDouble() <= spoilRate);
						if (spoilFlag) {
							if (container != null && container.getContainerSize() > 0) {
								for (int i = 0; i < container.getContainerSize(); i++) {
									ItemStack stack = container.getItem(i);
									if (stack != null && !stack.isEmpty()) {
										int slot = i;
										SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, stack);
										if (recipe != null) {
											SpoilHelper.updateSpoilingStack(stack, recipe);
											if (SpoilHelper.isSpoiled(stack)) {
												spoilItemInContainer(container, slot, stack, recipe, level.registryAccess());
												be.setChanged();
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
		return InteractionResult.PASS;
	}

	@SuppressWarnings("deprecation")
	public static boolean isAreaLoaded(LevelReader reader, BlockPos center, int range) {
		return reader.hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	public static void spoilItemInContainer(Container container, int slot, ItemStack stack, SpoilRecipe recipe, RegistryAccess registryAccess) {
		ItemStack spoiledStack = recipe.getResultItem(registryAccess).copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			for (int i = 0; i < container.getContainerSize(); ++i) {
				if (container.canPlaceItem(i, spoiledStack) && container.getItem(i).isEmpty()) {
					container.setItem(i, spoiledStack);
					break;
				} else {
					ItemStack stackInSlot = container.getItem(slot);
					if (ItemStack.isSameItem(spoiledStack, stackInSlot) && SpoilHelper.totalUnderMax(stackInSlot, spoiledStack)) {
						spoiledStack.setCount(stackInSlot.getCount() + spoiledStack.getCount());
						break;
					}
				}
			}
		}
	}

	public static void onPlayerTick(Player player) {
		if (!player.level().isClientSide &&
				player.level().getGameTime() % SpoiledConfigCache.spoilRate == 0 && !player.getAbilities().instabuild) {
			updateInventory(player);
		}
	}

	private static void updateInventory(Player player) {
		final Level level = player.level();
		int invCount = player.getInventory().getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty()) {
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

	private static void updateContainer(Level level, Entity entity, Container container) {
		int invCount = container.getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
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
