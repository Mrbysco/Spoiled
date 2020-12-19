package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.spoiled.ChangeSpoiling")
public class SpoilingCT {
    @ZenCodeType.Method
    public static void addSpoiling(MCSpoiling spoiling) {
        CraftTweakerAPI.apply(new ActionAddSpoiling(spoiling));
    }

    @ZenCodeType.Method
    public static void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime) {
        CraftTweakerAPI.apply(new ActionAddModSpoiling(modName, spoilStack, spoilTime));
    }

    @ZenCodeType.Method
    public static void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime, float sanity) {
        CraftTweakerAPI.apply(new ActionAddModSpoiling(modName, spoilStack, spoilTime, sanity));
    }

    @ZenCodeType.Method
    public static void replaceSpoiling(MCSpoiling spoiling) {
        CraftTweakerAPI.apply(new ActionReplaceSpoiling(spoiling));
    }

    @ZenCodeType.Method
    public static void removeSpoiling(String uniqueID) {
        CraftTweakerAPI.apply(new ActionRemoveSpoiling(uniqueID));
    }
}
