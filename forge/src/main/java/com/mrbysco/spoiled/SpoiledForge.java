package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.recipe.condition.SpoiledConditions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class SpoiledForge {

	public SpoiledForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpoiledConfig.serverSpec);
		eventBus.register(SpoiledConfig.class);

		eventBus.register(new SpoiledConditions());

		MinecraftForge.EVENT_BUS.register(new SpoilHandler());
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		CommonClass.init();

		if (ModList.get().isLoaded("curios")) {
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.spoiled.compat.curios.CuriosCompat::onCuriosTick);
		}

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		});
	}


	public void onCommandRegister(RegisterCommandsEvent event) {
		SpoiledCommands.initializeCommands(event.getDispatcher());
	}
}