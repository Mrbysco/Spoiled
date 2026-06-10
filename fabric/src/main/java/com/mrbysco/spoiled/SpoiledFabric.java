package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.neoforged.fml.config.ModConfig;

import java.util.ArrayList;

public class SpoiledFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigRegistry.INSTANCE.register("spoiled", ModConfig.Type.COMMON, SpoiledConfig.serverSpec);

		RecipeSynchronization.synchronizeRecipeSerializer(SpoiledRecipes.SPOILING_SERIALIZER.get());

		CommonClass.init();

		ServerTickEvents.END_LEVEL_TICK.register(SpoilHandler::onWorldTick);

		CommandRegistrationCallback.EVENT.register((commandDispatcher, registryAccess, environment) -> SpoiledCommands.initializeCommands(commandDispatcher));

		ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
			SpoiledConfigCache.setSpoilRate(SpoiledConfig.COMMON.spoilRate.getAsInt());
			SpoiledConfigCache.generateContainerModifier(
					SpoiledConfig.COMMON.containerModifier.get(), new ArrayList<>()
			);
		});

		ResourceConditions.register(ResourceConditionType.create(InitializeSpoilingCondition.ID, InitializeSpoilingCondition.CODEC));
		ResourceConditions.register(ResourceConditionType.create(MergeRecipeCondition.ID, MergeRecipeCondition.CODEC));
	}
}
