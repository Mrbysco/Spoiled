package com.mrbysco.spoiled.registry;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class SpoilRegistry {
    public static SpoilRegistry INSTANCE = new SpoilRegistry();

    private Map<ResourceLocation, SpoilInfo> spoilMap = Maps.newHashMap();

    public static void initializeSpoiling() {
        Map<ResourceLocation, SpoilInfo> nameToAgeing = INSTANCE.getSpoilMap();

        INSTANCE.registerAgeing(new SpoilInfo("BeefToFlesh", new ItemStack(Items.BEEF), new ItemStack(Items.ROTTEN_FLESH), 20));
        INSTANCE.registerAgeing(new SpoilInfo("AppleToSeeds", new ItemStack(Items.APPLE), new ItemStack(Items.ROTTEN_FLESH), 20));
        INSTANCE.registerAgeing(new SpoilInfo("BeefToFlesh", new ItemStack(Items.BREAD), new ItemStack(Items.ROTTEN_FLESH), 20));
    }

    public void registerAgeing(SpoilInfo info)
    {
        spoilMap.put(info.getFoodStack().getItem().getRegistryName(), info);
    }

    public Map<ResourceLocation, SpoilInfo> getSpoilMap() {
        return spoilMap;
    }
}
