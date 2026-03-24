package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.ConfigHandler;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.recipe.condition.SpoiledConditions;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class SpoiledNeoForge {

	public SpoiledNeoForge(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, SpoiledConfig.serverSpec);
		eventBus.register(ConfigHandler.class);

		SpoiledConditions.CONDITION_CODECS.register(eventBus);

		NeoForge.EVENT_BUS.register(new SpoilHandler());
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
		NeoForge.EVENT_BUS.addListener(this::onDatapackSync);
		NeoForge.EVENT_BUS.addListener(this::onRecipeReceived);

		CommonClass.init();

		if (ModList.get().isLoaded("curios")) {
			NeoForge.EVENT_BUS.addListener(com.mrbysco.spoiled.compat.curios.CuriosCompat::onCuriosTick);
		}

		if (dist.isClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
			NeoForge.EVENT_BUS.register(new TooltipHandler());
		}
	}

	public void onDatapackSync(OnDatapackSyncEvent event) {
		event.sendRecipes(SpoiledRecipes.SPOIL_RECIPE_TYPE.get());
	}

	public void onRecipeReceived(RecipesReceivedEvent event) {
		Constants.SPOIL_RECIPES.clear();
		Constants.SPOIL_RECIPES.addAll(event.getRecipeMap().byType(SpoiledRecipes.SPOIL_RECIPE_TYPE.get()));
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		SpoiledCommands.initializeCommands(event.getDispatcher());
	}
}