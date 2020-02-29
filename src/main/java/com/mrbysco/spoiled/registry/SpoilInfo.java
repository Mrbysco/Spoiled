package com.mrbysco.spoiled.registry;

import net.minecraft.item.ItemStack;

public class SpoilInfo {
    private String uniqueID;
    private ItemStack foodStack;
    private ItemStack spoilStack;
    private int spoilTime;
    private float sanity;

    public SpoilInfo(String uniqueID, ItemStack food, ItemStack spoiled, int time) {
        this.uniqueID = uniqueID;
        this.foodStack = food;
        this.spoilStack = spoiled;
        this.spoilTime = time;
        this.sanity = 0.0F;
    }

    public SpoilInfo(String uniqueID, ItemStack food, ItemStack spoiled, int time, float sanity) {
        this.uniqueID = uniqueID;
        this.foodStack = food;
        this.spoilStack = spoiled;
        this.spoilTime = time;
        this.sanity = sanity;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public ItemStack getFoodStack() {
        return foodStack;
    }

    public ItemStack getSpoilStack() {
        return spoilStack;
    }

    public int getSpoilTime() {
        return spoilTime;
    }

    public void setSanity(float sanity) {
        this.sanity = sanity;
    }

    public float getSanity() {
        return sanity;
    }
}
