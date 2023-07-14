package com.mrbysco.spoiled.recipe.condition;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class SpoiledConditions {
	@SubscribeEvent
	public void onRegisterSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(InitializeSpoilingCondition.Serializer.INSTANCE);
			CraftingHelper.register(MergeRecipeCondition.Serializer.INSTANCE);
		}
	}
}
