package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.ageingmobs.ChangeAgeing")
public class SpoilingCT {
    @ZenCodeType.Method
    public static void addAgeing(MCSpoiling ageing) {
        CraftTweakerAPI.apply(new ActionAddSpoiling(ageing));
    }

    @ZenCodeType.Method
    public static void removeAgeing(String uniqueID) {
        CraftTweakerAPI.apply(new ActionRemoveSpoiling(uniqueID));
    }
}
