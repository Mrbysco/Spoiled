package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SpoilHandler {

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getGameTime() % 20 == 0) {
            World world = event.world;
            for(TileEntity te : world.tickableTileEntities) {
                if(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                    IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null); //Should never be null.
                    for(int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stack = itemHandler.getStackInSlot(i);

                        if(doesSpoil(stack)) {
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
                }
            }
        }
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
                if(doesSpoil(stack)) {
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

    public boolean doesSpoil(ItemStack stack) {
        return SpoilRegistry.INSTANCE.getSpoilMap().containsKey(stack.getItem().getRegistryName());
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

    public void spoilItemInTE(IItemHandler itemHandler, int slot, ItemStack stack) {
        SpoilInfo info = SpoilRegistry.INSTANCE.getSpoilMap().get(stack.getItem().getRegistryName());
        ItemStack spoiledStack = info.getSpoilStack().copy();
        spoiledStack.setCount(stack.getCount());
        stack.shrink(64);

        itemHandler.insertItem(slot, spoiledStack, false);
    }
}
