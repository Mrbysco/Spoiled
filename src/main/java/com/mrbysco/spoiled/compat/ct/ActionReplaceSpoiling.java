package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;

public class ActionReplaceSpoiling implements IUndoableAction {
    public final SpoilInfo spoilInfo;
    public final SpoilInfo oldSpoilInfo;

    public ActionReplaceSpoiling(MCSpoiling data) {
        this.spoilInfo = data.getInternal();
        this.oldSpoilInfo = SpoilRegistry.INSTANCE.getInfoFromID(data.getInternal().getUniqueID());
    }

    @Override
    public void apply() {
        SpoilRegistry.INSTANCE.replaceSpoiling(spoilInfo);
    }

    @Override
    public String describe() {
        if (SpoilRegistry.INSTANCE.containsID(spoilInfo.getUniqueID())) {
            return String.format("Spoiling data with ID: " + spoilInfo.getUniqueID() + " has been replaced.");
        } else {
            return String.format("Spoiling data with ID: " + spoilInfo.getUniqueID() + " could not be replaced as it doesn't exist.");
        }
    }

    @Override
    public void undo() {
        SpoilRegistry.INSTANCE.replaceSpoiling(oldSpoilInfo);
    }

    @Override
    public String describeUndo() {
        return String.format("Spoiling data with ID: " + spoilInfo.getUniqueID() + " has been returned to it's former glory.");
    }
}
