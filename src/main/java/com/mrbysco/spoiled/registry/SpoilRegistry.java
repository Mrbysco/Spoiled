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

        INSTANCE.registerSpoiling(new SpoilInfo("apple", new ItemStack(Items.APPLE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("baked_potato", new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("beef", new ItemStack(Items.BEEF), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("beetroot", new ItemStack(Items.BEETROOT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("beetroot_soup", new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("bread", new ItemStack(Items.BREAD), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("carrot", new ItemStack(Items.CARROT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("chicken", new ItemStack(Items.CHICKEN), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("chorus_fruit", new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cod", new ItemStack(Items.COD), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_beef", new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_chicken", new ItemStack(Items.COOKED_CHICKEN), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_cod", new ItemStack(Items.COOKED_COD), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_mutton", new ItemStack(Items.COOKED_MUTTON), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_porkchop", new ItemStack(Items.COOKED_PORKCHOP), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_rabbit", new ItemStack(Items.COOKED_RABBIT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cooked_salmon", new ItemStack(Items.COOKED_SALMON), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("cookie", new ItemStack(Items.COOKIE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("dried_kelp", new ItemStack(Items.DRIED_KELP), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("golden_apple", new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("golden_carrot", new ItemStack(Items.GOLDEN_CARROT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("honey_bottle", new ItemStack(Items.HONEY_BOTTLE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("melon_slice", new ItemStack(Items.MELON_SLICE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("mushroom_stew", new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("mutton", new ItemStack(Items.MUTTON), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("poisonous_potato", new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("porkchop", new ItemStack(Items.PORKCHOP), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("potato", new ItemStack(Items.POTATO), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("pufferfish", new ItemStack(Items.PUFFERFISH), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("pumpkin_pie", new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("rabbit", new ItemStack(Items.RABBIT), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("rabbit_stew", new ItemStack(Items.RABBIT_STEW), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("rotten_flesh", new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("salmon", new ItemStack(Items.SALMON), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("spider_eye", new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("suspicious_stew", new ItemStack(Items.SUSPICIOUS_STEW), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("sweet_berries", new ItemStack(Items.SWEET_BERRIES), new ItemStack(Items.ROTTEN_FLESH), 1200));
        INSTANCE.registerSpoiling(new SpoilInfo("tropical_fish", new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.ROTTEN_FLESH), 1200));
    }

    public void registerSpoiling(SpoilInfo info)
    {
        spoilMap.put(info.getFoodStack().getItem().getRegistryName(), info);
    }

    public Map<ResourceLocation, SpoilInfo> getSpoilMap() {
        return spoilMap;
    }
}
