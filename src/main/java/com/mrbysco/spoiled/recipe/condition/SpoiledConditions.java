package com.mrbysco.spoiled.recipe.condition;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpoiledConditions {
	@SubscribeEvent
	public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		CraftingHelper.register(InitializeSpoilingCondition.Serializer.INSTANCE);
	}
}
