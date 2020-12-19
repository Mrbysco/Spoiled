package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ActionAddModSpoiling implements IUndoableAction {
    public final String modName;
    public final ItemStack spoilStack;
    public final int spoilTime;
    public final float sanity;

    public ActionAddModSpoiling(String modName, IItemStack spoilStack, int spoilTime) {
        this.modName = modName;
        this.spoilStack = spoilStack.getInternal();
        this.spoilTime = spoilTime;
        this.sanity = 0.0F;
    }

    public ActionAddModSpoiling(String modName, IItemStack spoilStack, int spoilTime, float sanity) {
        this.modName = modName;
        this.spoilStack = spoilStack.getInternal();
        this.spoilTime = spoilTime;
        this.sanity = sanity;
    }

    @Override
    public void apply() {
        for(Item foundItem : ForgeRegistries.ITEMS.getValues()) {
            if(foundItem.isFood() && foundItem.getRegistryName().getNamespace().equals(modName)) {
                SpoilRegistry.instance().registerSpoiling(new SpoilInfo(modName + ":" + foundItem.getRegistryName().getPath(), new ItemStack(foundItem), spoilStack, spoilTime, sanity));
            }
        }
    }

    @Override
    public String describe() {
        return "Adding spoiling to all food items from the mod with the following id: " + modName;
    }

    @Override
    public void undo() {
        for(Item foundItem : ForgeRegistries.ITEMS.getValues()) {
            if(foundItem.isFood() && foundItem.getRegistryName().getNamespace().equals(modName)) {
                SpoilRegistry.instance().removeSpoiling(modName + ":" + foundItem.getRegistryName().getPath());
            }
        }
    }

    @Override
    public String describeUndo() {
        return "Removing spoiling to all food items from the mod with the following id: " + modName;
    }
}
