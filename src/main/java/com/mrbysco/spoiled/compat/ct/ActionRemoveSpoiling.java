package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;

public class ActionRemoveSpoiling implements IUndoableAction {
    public final String uniqueID;
    public final SpoilInfo oldSpoilData;

    public ActionRemoveSpoiling(String uniqueID) {
        this.uniqueID = uniqueID;
        this.oldSpoilData = SpoilRegistry.INSTANCE.getInfoFromID(uniqueID);
    }

    @Override
    public void apply() {
        SpoilRegistry.INSTANCE.removeSpoiling(uniqueID);
    }

    @Override
    public String describe() {
        if (SpoilRegistry.INSTANCE.containsID(uniqueID)) {
            return String.format("Spoiling with unique ID: " + uniqueID + " has been removed.");
        } else {
            return String.format("Spoiling with unique ID: " + uniqueID + " could not be removed, ID does not exist");
        }
    }

    @Override
    public void undo() {
        SpoilRegistry.INSTANCE.registerSpoiling(oldSpoilData);
    }

    @Override
    public String describeUndo() {
        return String.format("Spoiling from <" + oldSpoilData.getFoodStack().getItem().getRegistryName() + "> to <" + oldSpoilData.getSpoilStack().getItem().getRegistryName() + "> has been added again, unique ID: " + oldSpoilData.getUniqueID());
    }
}
