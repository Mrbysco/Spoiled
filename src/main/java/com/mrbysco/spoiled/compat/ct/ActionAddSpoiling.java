package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;

public class ActionAddSpoiling implements IUndoableAction {
    public final SpoilInfo spoilInfo;

    public ActionAddSpoiling(MCSpoiling data) {
        this.spoilInfo = data.getInternal();
    }

    @Override
    public void apply() {
        SpoilRegistry.INSTANCE.registerSpoiling(spoilInfo);
    }

    @Override
    public String describe() {
        if (!SpoilRegistry.INSTANCE.containsID(spoilInfo.getUniqueID())) {
            return String.format("Spoiling from <" + spoilInfo.getFoodStack().getItem().getRegistryName() + "> to <" + spoilInfo.getSpoilStack().getItem().getRegistryName() + "> has been added with unique ID: " + spoilInfo.getUniqueID());
        } else {
            return String.format("Spoiling from <" + spoilInfo.getFoodStack().getItem().getRegistryName() + "> to <" + spoilInfo.getSpoilStack().getItem().getRegistryName() + "> could not be added, ID: " + spoilInfo.getUniqueID() + " already exists");
        }
    }

    @Override
    public void undo() {
        SpoilRegistry.INSTANCE.removeSpoiling(spoilInfo);
    }

    @Override
    public String describeUndo() {
        return String.format("Ageing from <" + spoilInfo.getFoodStack().getItem().getRegistryName() + "> to <" + spoilInfo.getSpoilStack().getItem().getRegistryName() + "> has been removed again, unique ID: " + spoilInfo.getUniqueID());
    }
}
