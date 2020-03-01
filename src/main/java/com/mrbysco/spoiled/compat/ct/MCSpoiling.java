package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.mrbysco.spoiled.registry.SpoilInfo;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.spoiled.SpoilData")
public class MCSpoiling {
    private final SpoilInfo internal;

    public MCSpoiling(SpoilInfo data) {
        this.internal = data;
    }

    @ZenCodeType.Constructor
    public MCSpoiling(String uniqueID, IItemStack foodStack, IItemStack spoilStack, int spoilTime) {
        this(new SpoilInfo(uniqueID, foodStack.getInternal(), spoilStack.getInternal(), spoilTime));
    }

    @ZenCodeType.Constructor
    public MCSpoiling(String uniqueID, IItemStack foodStack, IItemStack spoilStack, int spoilTime, float sanity) {
        this(new SpoilInfo(uniqueID, foodStack.getInternal(), spoilStack.getInternal(), spoilTime, sanity));
    }

    @ZenCodeType.Method
    public MCSpoiling setSanity(float sanity) {
        SpoilInfo internal = this.internal;
        internal.setSanity(sanity);

        return new MCSpoiling(internal);
    }

    public SpoilInfo getInternal() {
        return this.internal;
    }
}
