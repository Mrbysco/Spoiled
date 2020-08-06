package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpoilHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onWorldTick(WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote && event.world.getGameTime() % 20 == 0) {
            World world = event.world;
            if(!world.tickableTileEntities.isEmpty()) {
                List<TileEntity> tickableTileEntities = new CopyOnWriteArrayList<>(world.tickableTileEntities);
                Iterator<TileEntity> iterator;
                for (iterator = tickableTileEntities.iterator(); iterator.hasNext();) {
                    TileEntity te = iterator.next();
                    if(te != null && !te.isRemoved() && te.hasWorld() && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() && !SpoiledConfig.SERVER.containerBlacklist.get().contains(te.getClass().getSimpleName())) {
                        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                            for(int i = 0; i < itemHandler.getSlots(); i++) {
                                ItemStack stack = itemHandler.getStackInSlot(i);
                                if(SpoilRegistry.INSTANCE.doesSpoil(stack)) {
                                    updateSpoilingStack(stack);

                                    if(stack.getTag() != null && !stack.getTag().isEmpty()) {
                                        CompoundNBT tag = stack.getTag();
                                        if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                                            int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                                            int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                                            if(getOldTime >= getMaxTime) {
                                                spoilItemInTE(itemHandler, i, stack);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void spoilItemInTE(IItemHandler itemHandler, int slot, ItemStack stack) {
        SpoilInfo info = SpoilRegistry.INSTANCE.getSpoilMap().get(stack.getItem().getRegistryName());
        ItemStack spoiledStack = info.getSpoilStack().copy();
        spoiledStack.setCount(stack.getCount());
        stack.shrink(stack.getCount());

        itemHandler.insertItem(slot, spoiledStack, false);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END && !event.player.world.isRemote && event.player.world.getGameTime() % 20 == 0 && !event.player.abilities.isCreativeMode) {
            updateInventory(event.player);
        }
    }

    private void updateInventory(PlayerEntity player) {
        int invCount = player.inventory.getSizeInventory();
        for(int i = 0; i < invCount; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty()) {
                if(SpoilRegistry.INSTANCE.doesSpoil(stack)) {
                    updateSpoilingStack(stack);

                    if(stack.getTag() != null && !stack.getTag().isEmpty()) {
                        CompoundNBT tag = stack.getTag();
                        int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                        int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                        if(getOldTime >= getMaxTime) {
                            spoilItemForPlayer(player, stack);
                        }
                    }
                }
            }
        }
    }

    public void updateSpoilingStack(ItemStack stack) {
        SpoilInfo info = SpoilRegistry.INSTANCE.getSpoilMap().get(stack.getItem().getRegistryName());
        if(stack.getTag() == null || stack.getTag().isEmpty()) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt(Reference.SPOIL_TAG, 0);
            tag.putInt(Reference.SPOIL_TIME_TAG, info.getSpoilTime());
            stack.setTag(tag);
        } else {
            CompoundNBT tag = stack.getTag();
            if(tag.contains(Reference.SPOIL_TAG) && tag.contains(Reference.SPOIL_TIME_TAG)) {
                int getOldTime = tag.getInt(Reference.SPOIL_TAG);
                int getMaxTime = tag.getInt(Reference.SPOIL_TIME_TAG);
                if(getOldTime < getMaxTime) {
                    getOldTime++;
                    tag.putInt(Reference.SPOIL_TAG, getOldTime);
                    stack.setTag(tag);
                }
            }
        }
    }

    public void spoilItemForPlayer(PlayerEntity player, ItemStack stack) {
        SpoilInfo info = SpoilRegistry.INSTANCE.getSpoilMap().get(stack.getItem().getRegistryName());
        ItemStack spoiledStack = info.getSpoilStack().copy();
        spoiledStack.setCount(stack.getCount());
        stack.shrink(64);
        if(player.addItemStackToInventory(spoiledStack)) {
            ItemEntity itemEntity = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ());
            itemEntity.setItem(spoiledStack);
            player.world.addEntity(itemEntity);
        }
    }
}
