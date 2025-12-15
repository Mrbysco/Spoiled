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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;

public class SpoilHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level && level.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
			if (level.dimension() != Level.OVERWORLD) return;
			List<BlockPos> blockEntityPositions = ChunkHelper.getBlockEntityPositions(level).stream().filter(pos -> level.isAreaLoaded(pos, 1)).toList();
			if (!blockEntityPositions.isEmpty()) {
				for (BlockPos pos : blockEntityPositions) {
					BlockEntity be = level.getBlockEntity(pos);
					if (be != null && !be.isRemoved() && be.hasLevel()) {
						if (be instanceof RandomizableContainerBlockEntity randomizeInventory && ((RandomizableContainerBlockEntityAccessor) randomizeInventory).getLootTable() != null)
							continue;

						BlockState state = level.getBlockState(pos);
						if (state.hasProperty(BlockStateProperties.CHEST_TYPE)) {
							ChestType type = state.getValue(BlockStateProperties.CHEST_TYPE);
							// If double chest only process left side chests
							if (type == ChestType.RIGHT) {
								// Skip right side chests
								continue;
							}
						}

						ResourceLocation location = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType());
						double spoilRate = 1.0D;
						if (location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
							spoilRate = SpoiledConfigCache.containerModifier.get(location);
						}
						if (spoilRate <= 0) {
							continue; // Skip if spoil rate is 0 or less
						}
						boolean spoilFlag = spoilRate == 1.0 || (spoilRate > 0 && level.random.nextDouble() <= spoilRate);
						if (spoilFlag) {
							ResourceHandler<ItemResource> resourceHandler = level.getCapability(Capabilities.Item.BLOCK, pos, null);
							if (resourceHandler != null && resourceHandler.size() > 0) {
								ItemStack containerStack = state.getCloneItemStack(level, pos, false);
								for (int slot = 0; slot < resourceHandler.size(); slot++) {
									ItemResource resource = resourceHandler.getResource(slot);
									if (!resource.isEmpty()) {
										int count = resourceHandler.getAmountAsInt(slot);
										ItemStack stack = resource.toStack(count);
										RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, stack);
										if (recipeHolder != null) {
											SpoilRecipe recipe = recipeHolder.value();
											spoilItemInHandler(containerStack, resourceHandler, slot, stack, recipe, level.registryAccess(), level.getRandom());
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

	/**
	 * Spoils an item in an item handler based on the spoil recipe and the container's spoil rate.
	 *
	 * @param containerStack  the stack of the container item
	 * @param resourceHandler the item handler to spoil items in
	 * @param slot            the slot in the item handler to spoil the item
	 * @param stack           the item stack to spoil
	 * @param recipe          the spoil recipe to use for spoiling
	 * @param registryAccess  the registry access for getting the result item of the recipe
	 * @param random          the random source to use for determining if the item should spoil
	 */
	public static void spoilItemInHandler(ItemStack containerStack, ResourceHandler<ItemResource> resourceHandler,
	                                      int slot, ItemStack stack, SpoilRecipe recipe, RegistryAccess registryAccess,
	                                      RandomSource random) {
		ResourceLocation location = BuiltInRegistries.ITEM.getKey(containerStack.getItem());
		double spoilRate = 1.0D;
		if (location != null && (SpoiledConfigCache.itemContainerModifier.containsKey(location))) {
			spoilRate = SpoiledConfigCache.itemContainerModifier.get(location);
		}
		if (spoilRate <= 0) {
			return; // Skip if spoil rate is 0 or less
		}
		boolean spoilFlag = spoilRate == 1.0 || (spoilRate > 0 && random.nextDouble() <= spoilRate);
		if (spoilFlag) {
			try (var tx = Transaction.openRoot()) {
				int count = stack.getCount();
				resourceHandler.extract(slot, ItemResource.of(stack), count, tx);
				SpoilHelper.updateSpoilingStack(stack, recipe);
				resourceHandler.insert(slot, ItemResource.of(stack), count, tx);
				tx.commit();
			}

			if (SpoilHelper.isSpoiled(stack)) {
				ItemStack spoiledStack = recipe.getResult();
				int oldStackCount = stack.getCount();
				try (var tx = Transaction.openRoot()) {
					if (resourceHandler.extract(slot, ItemResource.of(stack), stack.getCount(), tx) != stack.getCount())
						return;
					stack.setCount(0);
					if (!spoiledStack.isEmpty()) {
						spoiledStack.setCount(oldStackCount);
						resourceHandler.insert(slot, ItemResource.of(spoiledStack), spoiledStack.getCount(), tx);
						tx.commit();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player &&
				player.level().getGameTime() % SpoiledConfigCache.spoilRate == 0 && !player.getAbilities().instabuild) {
			updateInventory(player);
		}
	}

	private void updateInventory(Player player) {
		final Level level = player.level();
		int invCount = player.getInventory().getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty()) {
				ResourceHandler<ItemResource> resourceHandler = stack.getCapability(Capabilities.Item.ITEM, null);
				if (resourceHandler != null && resourceHandler.size() > 0) {
					for (int j = 0; j < resourceHandler.size(); j++) {
						ItemResource nestedResource = resourceHandler.getResource(j);
						if (!nestedResource.isEmpty()) {
							int count = resourceHandler.getAmountAsInt(i);
							ItemStack nestedStack = nestedResource.toStack(count);
							RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, nestedStack);
							if (recipeHolder != null) {
								SpoilRecipe recipe = recipeHolder.value();
								spoilItemInHandler(stack, resourceHandler, j, nestedStack, recipe, level.registryAccess(), level.getRandom());
							}
						}
					}
				} else {
					RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, stack);
					if (recipeHolder != null) {
						SpoilRecipe recipe = recipeHolder.value();
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
				ResourceHandler<ItemResource> resourceHandler = stack.getCapability(Capabilities.Item.ITEM, null);
				if (resourceHandler != null && resourceHandler.size() > 0) {
					for (int j = 0; j < resourceHandler.size(); j++) {
						ItemResource nestedResource = resourceHandler.getResource(j);
						if (!nestedResource.isEmpty()) {
							int count = resourceHandler.getAmountAsInt(i);
							ItemStack nestedStack = nestedResource.toStack(count);
							RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, nestedStack);
							if (recipeHolder != null) {
								SpoilRecipe recipe = recipeHolder.value();
								spoilItemInHandler(stack, resourceHandler, j, nestedStack, recipe, level.registryAccess(), level.getRandom());
							}
						}
					}
				} else {
					RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, stack);
					if (recipeHolder != null) {
						SpoilRecipe recipe = recipeHolder.value();
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
