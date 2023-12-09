package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.recipe.condition.SpoiledConditions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class SpoiledForge {

	public SpoiledForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpoiledConfig.serverSpec);
		eventBus.register(SpoiledConfig.class);

		SpoiledConditions.CONDITION_CODECS.register(eventBus);

		NeoForge.EVENT_BUS.register(new SpoilHandler());
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);

		CommonClass.init();

		if (ModList.get().isLoaded("curios")) {
			NeoForge.EVENT_BUS.addListener(com.mrbysco.spoiled.compat.curios.CuriosCompat::onCuriosTick);
		}

		if (FMLEnvironment.dist.isClient()) {
			NeoForge.EVENT_BUS.register(new TooltipHandler());
		}
	}


	public void onCommandRegister(RegisterCommandsEvent event) {
		SpoiledCommands.initializeCommands(event.getDispatcher());
	}
}