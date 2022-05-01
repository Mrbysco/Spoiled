package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpoilHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.START && !event.world.isClientSide && event.world.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
			ServerWorld world = (ServerWorld) event.world;
			if (!world.tickableBlockEntities.isEmpty()) {
				List<TileEntity> tileEntities = new CopyOnWriteArrayList<>(world.blockEntityList);
				Iterator<TileEntity> iterator;
				for (iterator = tileEntities.iterator(); iterator.hasNext(); ) {
					TileEntity te = iterator.next();
					if (te != null && !te.isRemoved() && te.hasLevel() && world.isAreaLoaded(te.getBlockPos(), 1) &&
							te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
						ResourceLocation location = te.getType().getRegistryName();
						double spoilRate = 1.0D;
						if (location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
							spoilRate = SpoiledConfigCache.containerModifier.get(location);
						}
						boolean spoilFlag = spoilRate > 0 && world.random.nextDouble() <= spoilRate;
						if (spoilFlag) {
							IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
							if (itemHandler instanceof SidedInvWrapper) {
								itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
							}
							if (itemHandler != null) {
								if (itemHandler.getSlots() > 0) {
									for (int i = 0; i < itemHandler.getSlots(); i++) {
										ItemStack stack = itemHandler.getStackInSlot(i);
										if (!stack.isEmpty()) {
											Inventory inventory = new Inventory(stack);
											int slot = i;
											SpoilRecipe recipe = world.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).orElse(null);
											if (recipe != null) {
												updateSpoilingStack(stack, recipe);

												CompoundNBT tag = stack.getOrCreateTag();
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
			if(SpoiledConfig.COMMON.spoilEntityContainers.get()) {
				List<Entity> entityList = new ArrayList<>();
				world.getAllEntities().forEach(entityList::add);
				for (Entity entity : entityList) {
					if (entity instanceof IInventory && entity.isAlive()) {
						IInventory containerEntity = (IInventory) entity;
						updateContainer(world, entity, containerEntity);
					}
				}
			}
		}
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
		if (event.phase == TickEvent.Phase.END && !event.player.level.isClientSide && event.player.level.getGameTime() % SpoiledConfigCache.spoilRate == 0 && !event.player.abilities.instabuild) {
			updateInventory(event.player);
		}
	}

	private void updateInventory(PlayerEntity player) {
		World world = player.level;
		int invCount = player.inventory.getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = player.inventory.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
					stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								int slot = j;
								ItemStack nestedStack = itemHandler.getStackInSlot(slot);
								if (!nestedStack.isEmpty()) {
									Inventory inventory = new Inventory(nestedStack);
									world.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).ifPresent(recipe -> {
										updateSpoilingStack(nestedStack, recipe);

										CompoundNBT tag = nestedStack.getOrCreateTag();
										if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
											int getOldTime = tag.getInt(Reference.SPOIL_TAG);
											int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
											if (getOldTime >= getMaxTime) {
												spoilItemInHandler(itemHandler, slot, nestedStack, recipe);
											}
										}
									});
								}
							}
						}
					});
				} else {
					Inventory inventory = new Inventory(stack);
					world.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).ifPresent(recipe -> {
						updateSpoilingStack(stack, recipe);

						CompoundNBT tag = stack.getOrCreateTag();
						if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
							int getOldTime = tag.getInt(Reference.SPOIL_TAG);
							int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
							if (getOldTime >= getMaxTime) {
								spoilItemForPlayer(player, stack, recipe);
							}
						}
					});
				}
			}
		}
	}

	public void spoilItemForPlayer(PlayerEntity player, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem().copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			if (!player.addItem(spoiledStack)) {
				ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ());
				itemEntity.setItem(spoiledStack);
				player.level.addFreshEntity(itemEntity);
			}
		}
	}

	private void updateContainer(World level, Entity entity, IInventory container) {
		int invCount = container.getContainerSize();
		for (int i = 0; i < invCount; i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
					stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								int slot = j;
								ItemStack nestedStack = itemHandler.getStackInSlot(slot);
								if (nestedStack != null && !nestedStack.isEmpty()) {
									Inventory inventory = new Inventory(nestedStack);
									SpoilRecipe recipe = level.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, level).orElse(null);
									if (recipe != null) {
										updateSpoilingStack(nestedStack, recipe);

										CompoundNBT tag = nestedStack.getOrCreateTag();
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
					Inventory inventory = new Inventory(stack);
					SpoilRecipe recipe = level.getRecipeManager().getRecipeFor(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, level).orElse(null);
					if (recipe != null) {
						updateSpoilingStack(stack, recipe);

						CompoundNBT tag = stack.getOrCreateTag();
						if (tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
							int getOldTime = tag.getInt(Reference.SPOIL_TAG);
							int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
							if (getOldTime >= getMaxTime) {
								spoilItemForEntity(container, entity, stack, recipe);
							}
						}
					}
				}
			}
		}
	}

	public void spoilItemForEntity(IInventory container, Entity entity, ItemStack stack, SpoilRecipe recipe) {
		ItemStack spoiledStack = recipe.getResultItem().copy();
		int oldStackCount = stack.getCount();
		stack.setCount(0);
		if (!spoiledStack.isEmpty()) {
			spoiledStack.setCount(oldStackCount);
			int freeSlot = getFreeSlot(container);
			if (freeSlot != -1) {
				container.setItem(freeSlot, spoiledStack);
			} else {
				ItemEntity itemEntity = new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), spoiledStack);
				entity.level.addFreshEntity(itemEntity);
			}
		}
	}

	private int getFreeSlot(IInventory container) {
		for (int i = 0; i < container.getContainerSize(); ++i) {
			if (container.getItem(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	public void updateSpoilingStack(ItemStack stack, SpoilRecipe recipe) {
		CompoundNBT tag = stack.getOrCreateTag();
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
}
