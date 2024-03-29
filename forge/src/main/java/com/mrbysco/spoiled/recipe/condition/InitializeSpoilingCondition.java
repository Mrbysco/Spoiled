package com.mrbysco.spoiled.recipe.condition;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class InitializeSpoilingCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "initialize_spoiling");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		return SpoiledConfig.COMMON.initializeSpoiling.get();
	}

	public static class Serializer implements IConditionSerializer<InitializeSpoilingCondition> {
		public static final Serializer INSTANCE = new Serializer();

		public void write(JsonObject json, InitializeSpoilingCondition value) {

		}

		public InitializeSpoilingCondition read(JsonObject json) {
			return new InitializeSpoilingCondition();
		}

		public ResourceLocation getID() {
			return InitializeSpoilingCondition.ID;
		}
	}
}