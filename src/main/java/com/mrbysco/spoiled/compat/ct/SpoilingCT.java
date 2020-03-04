package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.spoiled.ChangeSpoiling")
public class SpoilingCT {
    @ZenCodeType.Method
    public static void addSpoiling(MCSpoiling spoiling) {
        CraftTweakerAPI.apply(new ActionAddSpoiling(spoiling));
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
