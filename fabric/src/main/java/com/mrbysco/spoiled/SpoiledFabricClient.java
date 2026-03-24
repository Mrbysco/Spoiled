package com.mrbysco.spoiled;

import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import com.mrbysco.spoiled.util.TooltipUtil;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.fml.config.ModConfig;

public class SpoiledFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register("spoiled", ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);

		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			Component component = TooltipUtil.getTooltip(stack);
			if (component != null) {
				lines.add(component);
			}
		});

		ClientRecipeSynchronizedEvent.EVENT.register((mc, synchronizedRecipes) -> {
			RecipeMap recipeMap = RecipeMap.create(synchronizedRecipes.recipes());
			Constants.SPOIL_RECIPES.clear();
			Constants.SPOIL_RECIPES.addAll(recipeMap.byType(SpoiledRecipes.SPOIL_RECIPE_TYPE.get()));
		});
	}
}
