package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.network.RecipeContentPayload;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.YamlConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.Set;

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

		PayloadTypeRegistry.playS2C().register(RecipeContentPayload.TYPE, RecipeContentPayload.STREAM_CODEC);

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

	/**
	 * Send the Spoiled recipes to a player.
	 * @param player the player to send the recipes to
	 * @param recipeMap the recipe map containing the recipes to send
	 */
	public static void sendRecipes(ServerPlayer player, Collection<RecipeHolder<?>> recipeMap) {
		var payload = RecipeContentPayload.create(Set.of(SpoiledRecipes.SPOIL_RECIPE_TYPE.get()), recipeMap);
		ServerPlayNetworking.send(player, payload);
	}
}
