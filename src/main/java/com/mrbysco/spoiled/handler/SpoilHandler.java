package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.mixin.RandomizableContainerBlockEntityAccessor;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import com.mrbysco.spoiled.util.ChunkHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.List;

public class SpoilHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == Phase.END && !event.world.isClientSide && event.world.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
			Level level = event.world;
			List<BlockPos> blockEntityPositions = ChunkHelper.getBlockEntityPositions(level);
			if (!blockEntityPositions.isEmpty()) {
				for (BlockPos pos : blockEntityPositions) {
					if (level.isAreaLoaded(pos, 1)) {
						BlockEntity be = level.getBlockEntity(pos);
						if (be != null && !be.isRemoved() && be.hasLevel() && be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
							if (be instanceof RandomizableContainerBlockEntity randomizeInventory && ((RandomizableContainerBlockEntityAccessor) randomizeInventory).getLootTable() != null)
								return;

							ResourceLocation location = be.getType().getRegistryName();
							double spoilRate = 1.0D;
							if (location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
								spoilRate = SpoiledConfigCache.containerModifier.get(location);
							}
							boolean spoilFlag = spoilRate > 0 && level.random.nextDouble() <= spoilRate;
							if (spoilFlag) {
								IItemHandler itemHandler = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
								if (itemHandler instanceof SidedInvWrapper) {
									itemHandler = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
								}
								if (itemHandler != null) {
									if (itemHandler.getSlots() > 0) {
										for (int i = 0; i < itemHandler.getSlots(); i++) {
											ItemStack stack = itemHandler.getStackInSlot(i);
											if (stack != null && !stack.isEmpty()) {
												SimpleContainer inventory = new SimpleContainer(stack);
												int slot = i;
												SpoilRecipe recipe = level.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get(), inventory, level).orElse(getDefaultSpoilRecipe(stack));
												if (recipe != null) {
													updateSpoilingStack(stack, recipe);

													CompoundTag tag = stack.getOrCreateTag();
													if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
														int getOldTime = tag.getInt(Reference.SPOIL_TAG);
														int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
														if (getOldTime >= getMaxTime) {
															spoilItemInHandler(itemHandler, slot, stack, recipe);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private SpoilRecipe getDefaultSpoilRecipe(ItemStack stack) {
		if (SpoiledConfig.COMMON.spoilEverything.get() && stack.isEdible() &&
				!SpoiledConfig.COMMON.spoilEverythingBlacklist.get().contains(stack.getItem().getRegistryName().toString())) {
			ItemStack spoilStack = SpoiledConfigCache.getDefaultSpoilItem();
			String result = spoilStack.isEmpty() ? "to_air" : "to_" + spoilStack.getItem().getRegistryName().getPath();
			String recipePath = "everything_" + stack.getItem().getRegistryName().getPath() + result;
			return new SpoilRecipe(new ResourceLocation(Reference.MOD_ID, recipePath), "", Ingredient.of(stack), spoilStack, SpoiledConfig.COMMON.defaultSpoilTime.get());
		}
		return null;
	}

	private void spoilItemInHandler(IItemHandler itemHandler, int slot, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem().copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			itemHandler.insertItem(slot, spoiledStack, false);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !event.player.level.isClientSide && event.player.level.getGameTime() % SpoiledConfigCache.spoilRate == 0 && !event.player.getAbilities().instabuild) {
			updateInventory(event.player);
		}
	}

	private void updateInventory(Player player) {
		Level world = player.level;
		int invCount = player.getInventory().getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
					stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								int slot = j;
								ItemStack nestedStack = itemHandler.getStackInSlot(slot);
								if (nestedStack != null && !nestedStack.isEmpty()) {
									SimpleContainer inventory = new SimpleContainer(nestedStack);
									SpoilRecipe recipe = world.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get(), inventory, world).orElse(getDefaultSpoilRecipe(stack));
									if (recipe != null) {
										updateSpoilingStack(nestedStack, recipe);

										CompoundTag tag = nestedStack.getOrCreateTag();
										if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
											int getOldTime = tag.getInt(Reference.SPOIL_TAG);
											int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
											if (getOldTime >= getMaxTime) {
												spoilItemInHandler(itemHandler, slot, nestedStack, recipe);
											}
										}
									}
								}
							}
						}
					});
				} else {
					SimpleContainer inventory = new SimpleContainer(stack);
					SpoilRecipe recipe = world.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get(), inventory, world).orElse(getDefaultSpoilRecipe(stack));
					if (recipe != null) {
						updateSpoilingStack(stack, recipe);

						CompoundTag tag = stack.getOrCreateTag();
						if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
							int getOldTime = tag.getInt(Reference.SPOIL_TAG);
							int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
							if (getOldTime >= getMaxTime) {
								spoilItemForPlayer(player, stack, recipe);
							}
						}
					}
				}
			}
		}
	}

	public void updateSpoilingStack(ItemStack stack, SpoilRecipe recipe) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.isEmpty()) {
			if (!tag.contains(Reference.SPOIL_TAG)) {
				tag.putInt(Reference.SPOIL_TAG, 0);
			}
			if (!tag.contains(Reference.SPOIL_TIME_TAG)) {
				tag.putInt(Reference.SPOIL_TIME_TAG, recipe.getSpoilTime());
			}
			stack.setTag(tag);
		} else {
			if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
				int getOldTime = tag.getInt(Reference.SPOIL_TAG);
				int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
				if (getMaxTime != recipe.getSpoilTime()) {
					tag.putInt(Reference.SPOIL_TIME_TAG, recipe.getSpoilTime());
				}
				if (getOldTime < getMaxTime) {
					getOldTime++;
					tag.putInt(Reference.SPOIL_TAG, getOldTime);
					stack.setTag(tag);
				}
			}
		}
	}

	public void spoilItemForPlayer(Player player, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem().copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			if (!player.addItem(spoiledStack)) {
				ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), spoiledStack);
				player.level.addFreshEntity(itemEntity);
			}
		}
	}
}
