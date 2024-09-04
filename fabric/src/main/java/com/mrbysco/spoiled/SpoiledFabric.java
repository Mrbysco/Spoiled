package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.YamlConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.world.InteractionResult;

public class SpoiledFabric implements ModInitializer {
	public static ConfigHolder<SpoiledConfig> config;

	@Override
	public void onInitialize() {
		config = AutoConfig.register(SpoiledConfig.class, YamlConfigSerializer::new);
		config.registerLoadListener((holder, config) -> {
			SpoiledConfigCache.setSpoilRate(config.general.spoilRate);
			SpoiledConfigCache.generateContainerModifier(config.general.containerModifier);
			return InteractionResult.PASS;
		});
		config.registerSaveListener((holder, config) -> {
			SpoiledConfigCache.setSpoilRate(config.general.spoilRate);
			SpoiledConfigCache.generateContainerModifier(config.general.containerModifier);
			return InteractionResult.PASS;
		});

		CommonClass.init();

		ServerTickEvents.END_WORLD_TICK.register(SpoilHandler::onWorldTick);

		CommandRegistrationCallback.EVENT.register((commandDispatcher, registryAccess, environment) -> SpoiledCommands.initializeCommands(commandDispatcher));

		ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
			SpoiledConfigCache.setSpoilRate(config.get().general.spoilRate);
			SpoiledConfigCache.generateContainerModifier(config.get().general.containerModifier);
		});

		ResourceConditions.register(ResourceConditionType.create(InitializeSpoilingCondition.ID, InitializeSpoilingCondition.CODEC));
		ResourceConditions.register(ResourceConditionType.create(MergeRecipeCondition.ID, MergeRecipeCondition.CODEC));
	}
}
