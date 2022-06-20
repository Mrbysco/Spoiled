package com.mrbysco.spoiled.recipe.condition;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class MergeRecipeCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "merge_food");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@SuppressWarnings("removal")
	@Override
	public boolean test() {
		return SpoiledConfig.COMMON.mergeSpoilingFood.get();
	}

	public static class Serializer implements IConditionSerializer<MergeRecipeCondition> {
		public static final MergeRecipeCondition.Serializer INSTANCE = new MergeRecipeCondition.Serializer();

		public void write(JsonObject json, MergeRecipeCondition value) {

		}

		public MergeRecipeCondition read(JsonObject json) {
			return new MergeRecipeCondition();
		}

		public ResourceLocation getID() {
			return MergeRecipeCondition.ID;
		}
	}
}