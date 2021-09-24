package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import com.mrbysco.spoiled.util.InventoryHelper;
import com.mrbysco.spoiled.util.SingularInventory;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpoilHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onWorldTick(WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote && event.world.getGameTime() % SpoiledConfigCache.spoilRate == 0) {
            World world = event.world;
            if(!world.tickableTileEntities.isEmpty()) {
                List<TileEntity> tileEntities = new CopyOnWriteArrayList<>(world.loadedTileEntityList);
                Iterator<TileEntity> iterator;
                for (iterator = tileEntities.iterator(); iterator.hasNext();) {
                    TileEntity te = iterator.next();
                    if(te != null && !te.isRemoved() && te.hasWorld() && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                        ResourceLocation location = te.getType().getRegistryName();
                        double spoilRate = 1.0D;
                        if(location != null && (SpoiledConfigCache.containerModifier.containsKey(location))) {
                            spoilRate = SpoiledConfigCache.containerModifier.get(location);
                        }
                        boolean spoilFlag = spoilRate > 0 && world.rand.nextDouble() <= spoilRate;
                        if(spoilFlag) {
                            IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                            if(itemHandler instanceof SidedInvWrapper) {
                                itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                            }
                            if(itemHandler != null) {
                                if(itemHandler.getSlots() > 0) {
                                    for(int i = 0; i < itemHandler.getSlots(); i++) {
                                        ItemStack stack = itemHandler.getStackInSlot(i);
                                        if(!stack.isEmpty()) {
                                            SingularInventory inventory = InventoryHelper.createSingularInventory(stack);
                                            int slot = i;
                                            SpoilRecipe recipe = world.getRecipeManager().getRecipe(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).orElse(null);
                                            if(recipe != null) {
                                                updateSpoilingStack(stack, recipe);

                                                CompoundNBT tag = stack.getOrCreateTag();
                                                if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                                                    int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                                                    int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                                                    if(getOldTime >= getMaxTime) {
                                                        spoilItemInItemhandler(itemHandler, slot, stack, recipe);
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

    private void spoilItemInItemhandler(IItemHandler itemHandler, int slot, ItemStack stack, SpoilRecipe recipe) {
        ItemStack spoiledStack = recipe.getRecipeOutput().copy();
        int oldStackCount = stack.getCount();
        stack.setCount(0);
        if(!spoiledStack.isEmpty()) {
            spoiledStack.setCount(oldStackCount);
            itemHandler.insertItem(slot, spoiledStack, false);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END && !event.player.world.isRemote && event.player.world.getGameTime() % SpoiledConfigCache.spoilRate == 0 && !event.player.abilities.isCreativeMode) {
            updateInventory(event.player);
        }
    }

    private void updateInventory(PlayerEntity player) {
        World world = player.world;
        int invCount = player.inventory.getSizeInventory();
        for(int i = 0; i < invCount; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty()) {
                if(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                    stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                        if(itemHandler.getSlots() > 0) {
                            for(int j = 0; j < itemHandler.getSlots(); j++) {
                                int slot = j;
                                ItemStack nestedStack = itemHandler.getStackInSlot(slot);
                                if(!nestedStack.isEmpty()) {
                                    SingularInventory inventory = InventoryHelper.createSingularInventory(nestedStack);
                                    world.getRecipeManager().getRecipe(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).ifPresent(recipe -> {
                                        updateSpoilingStack(nestedStack, recipe);

                                        CompoundNBT tag = nestedStack.getOrCreateTag();
                                        if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                                            int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                                            int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                                            if(getOldTime >= getMaxTime) {
                                                spoilItemInItemhandler(itemHandler, slot, nestedStack, recipe);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    SingularInventory inventory = InventoryHelper.createSingularInventory(stack);
                    world.getRecipeManager().getRecipe(SpoiledRecipes.SPOIL_RECIPE_TYPE, inventory, world).ifPresent(recipe -> {
                        updateSpoilingStack(stack, recipe);

                        CompoundNBT tag = stack.getOrCreateTag();
                        if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                            int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                            int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                            if(getOldTime >= getMaxTime) {
                                spoilItemForPlayer(player, stack, recipe);
                            }
                        }
                    });
                }
            }
        }
    }

    public void updateSpoilingStack(ItemStack stack, SpoilRecipe recipe) {
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.isEmpty()) {
            if(!tag.contains(Reference.SPOIL_TAG)) {
                tag.putInt(Reference.SPOIL_TAG, 0);
            }
            if(!tag.contains(Reference.SPOIL_TIME_TAG)) {
                tag.putInt(Reference.SPOIL_TIME_TAG, recipe.getSpoilTime());
            }
            stack.setTag(tag);
        } else {
            if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                if(getMaxTime != recipe.getSpoilTime()) {
                    tag.putInt(Reference.SPOIL_TIME_TAG, recipe.getSpoilTime());
                }
                if(getOldTime < getMaxTime) {
                    getOldTime++;
                    tag.putInt(Reference.SPOIL_TAG, getOldTime);
                    stack.setTag(tag);
                }
            }
        }
    }

    public void spoilItemForPlayer(PlayerEntity player, ItemStack stack, SpoilRecipe recipe) {
        ItemStack spoiledStack = recipe.getRecipeOutput().copy();
        int oldStackCount = stack.getCount();
        stack.setCount(0);
        if(!spoiledStack.isEmpty()) {
            spoiledStack.setCount(oldStackCount);
            if(!player.addItemStackToInventory(spoiledStack)) {
                ItemEntity itemEntity = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ());
                itemEntity.setItem(spoiledStack);
                player.world.addEntity(itemEntity);
            }
        }
    }
}
