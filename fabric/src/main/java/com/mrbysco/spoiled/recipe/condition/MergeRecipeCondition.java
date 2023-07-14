package com.mrbysco.spoiled.recipe.condition;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.SpoiledFabric;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.resources.ResourceLocation;

public class MergeRecipeCondition {
	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "merge_food");

	public static final ConditionJsonProvider PROVIDER = new ConditionJsonProvider() {
		@Override
		public void writeParameters(JsonObject object) {
		}

		@Override
		public ResourceLocation getConditionId() {
			return ID;
		}
	};

	public static boolean test() {
		return SpoiledFabric.config.get().general.mergeSpoilingFood;
	}
}