package com.mrbysco.spoiled.compat.ct;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.mrbysco.spoiled.registry.SpoilInfo;
import com.mrbysco.spoiled.registry.SpoilRegistry;

public class ActionReplaceSpoiling implements IUndoableAction {
    public final SpoilInfo spoilInfo;
    public final SpoilInfo oldSpoilInfo;

    public ActionReplaceSpoiling(MCSpoiling data) {
        this.spoilInfo = data.getInternal();
        this.oldSpoilInfo = SpoilRegistry.instance().getInfoFromID(data.getInternal().getUniqueID());
    }

    @Override
    public void apply() {
        SpoilRegistry.instance().replaceSpoiling(spoilInfo);
    }

    @Override
    public String describe() {
        if (SpoilRegistry.instance().containsID(spoilInfo.getUniqueID())) {
            return "Spoiling data with ID: " + spoilInfo.getUniqueID() + " has been replaced.";
        } else {
            return "Spoiling data with ID: " + spoilInfo.getUniqueID() + " could not be replaced as it doesn't exist.";
        }
    }

    @Override
    public void undo() {
        SpoilRegistry.instance().replaceSpoiling(oldSpoilInfo);
    }

    @Override
    public String describeUndo() {
        return "Spoiling data with ID: " + spoilInfo.getUniqueID() + " has been returned to it's former glory.";
    }
}
