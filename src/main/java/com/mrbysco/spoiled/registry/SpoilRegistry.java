package com.mrbysco.spoiled.registry;

import com.google.common.collect.Maps;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class SpoilRegistry {
    private final Map<ResourceLocation, SpoilInfo> spoilMap = Maps.newHashMap();
    private static SpoilRegistry INSTANCE;

    public static SpoilRegistry instance() {
        if (INSTANCE == null)
            INSTANCE = new SpoilRegistry();
        return INSTANCE;
    }

    public void reloadSpoiling() {
        spoilMap.clear();
        this.initializeSpoiling();
    }

    public void initializeSpoiling() {
        //Make sure the cache has been reloaded
        SpoiledConfigCache.refreshCache();

        if(SpoiledConfig.SERVER.initializeSpoiling.get()) {
            ItemStack defaultItemstack = ItemStack.EMPTY;
            int defaultTime = SpoiledConfig.SERVER.defaultSpoilTime.get();
            String defaultItem = SpoiledConfig.SERVER.defaultSpoilItem.get();
            if(!defaultItem.isEmpty()) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(defaultItem));
                if(item != null) {
                    defaultItemstack = new ItemStack(item);
                }
            }
            registerSpoiling(new SpoilInfo("apple", new ItemStack(Items.APPLE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("baked_potato", new ItemStack(Items.BAKED_POTATO), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("beef", new ItemStack(Items.BEEF), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("beetroot", new ItemStack(Items.BEETROOT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("beetroot_soup", new ItemStack(Items.BEETROOT_SOUP), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("bread", new ItemStack(Items.BREAD), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("carrot", new ItemStack(Items.CARROT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("chicken", new ItemStack(Items.CHICKEN), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("chorus_fruit", new ItemStack(Items.CHORUS_FRUIT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cod", new ItemStack(Items.COD), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_beef", new ItemStack(Items.COOKED_BEEF), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_chicken", new ItemStack(Items.COOKED_CHICKEN), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_cod", new ItemStack(Items.COOKED_COD), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_mutton", new ItemStack(Items.COOKED_MUTTON), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_porkchop", new ItemStack(Items.COOKED_PORKCHOP), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_rabbit", new ItemStack(Items.COOKED_RABBIT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cooked_salmon", new ItemStack(Items.COOKED_SALMON), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("cookie", new ItemStack(Items.COOKIE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("dried_kelp", new ItemStack(Items.DRIED_KELP), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("golden_apple", new ItemStack(Items.GOLDEN_APPLE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("golden_carrot", new ItemStack(Items.GOLDEN_CARROT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("melon_slice", new ItemStack(Items.MELON_SLICE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("mushroom_stew", new ItemStack(Items.MUSHROOM_STEW), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("mutton", new ItemStack(Items.MUTTON), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("poisonous_potato", new ItemStack(Items.POISONOUS_POTATO), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("porkchop", new ItemStack(Items.PORKCHOP), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("potato", new ItemStack(Items.POTATO), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("pufferfish", new ItemStack(Items.PUFFERFISH), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("pumpkin_pie", new ItemStack(Items.PUMPKIN_PIE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("rabbit", new ItemStack(Items.RABBIT), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("rabbit_stew", new ItemStack(Items.RABBIT_STEW), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("salmon", new ItemStack(Items.SALMON), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("spider_eye", new ItemStack(Items.SPIDER_EYE), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("suspicious_stew", new ItemStack(Items.SUSPICIOUS_STEW), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("sweet_berries", new ItemStack(Items.SWEET_BERRIES), defaultItemstack, defaultTime));
            registerSpoiling(new SpoilInfo("tropical_fish", new ItemStack(Items.TROPICAL_FISH), defaultItemstack, defaultTime));
        }
    }

    public void registerSpoiling(SpoilInfo info) {
        if(!containsID(info.getUniqueID())) {
            spoilMap.put(info.getFoodStack().getItem().getRegistryName(), info);
        }
    }

    public void removeSpoiling(SpoilInfo info)
    {
        removeSpoiling(info.getUniqueID());
    }

    public void removeSpoiling(String uniqueID) {
        if(containsID(uniqueID)) {
            for (Map.Entry<ResourceLocation, SpoilInfo> entry : spoilMap.entrySet()) {
                SpoilInfo value = entry.getValue();
                if(value.getUniqueID().equals(uniqueID))
                    spoilMap.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    public void replaceSpoiling(SpoilInfo info) {
        if(containsID(info.getUniqueID())) {
            SpoilInfo oldInfo = getInfoFromID(info.getUniqueID());
            spoilMap.remove(oldInfo.getFoodStack().getItem().getRegistryName(), oldInfo);
            spoilMap.put(info.getFoodStack().getItem().getRegistryName(), info);
        }
    }

    public Map<ResourceLocation, SpoilInfo> getSpoilMap() {
        return spoilMap;
    }

    public boolean containsID(String uniqueID) {
        if(!spoilMap.isEmpty()) {
            for (Map.Entry<ResourceLocation, SpoilInfo> entry : spoilMap.entrySet()) {
                SpoilInfo value = entry.getValue();
                if(value.getUniqueID().equals(uniqueID))
                    return true;
            }
        }
        return false;
    }

    public SpoilInfo getInfoFromID(String uniqueID) {
        if(containsID(uniqueID)) {
            for (Map.Entry<ResourceLocation, SpoilInfo> entry : spoilMap.entrySet()) {
                SpoilInfo value = entry.getValue();
                if(value.getUniqueID().equals(uniqueID))
                    return value;
            }
        }
        return null;
    }

    public boolean doesSpoil(ItemStack stack) {
        return spoilMap.containsKey(stack.getItem().getRegistryName());
    }
}
