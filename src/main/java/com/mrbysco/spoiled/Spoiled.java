package com.mrbysco.spoiled;

import com.mrbysco.spoiled.commands.SpoiledCommands;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.recipe.SpoiledRecipeTypes;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import com.mrbysco.spoiled.recipe.condition.SpoiledConditions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Spoiled {
	public static final Logger LOGGER = LogManager.getLogger();

	public Spoiled() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpoiledConfig.serverSpec);
		eventBus.register(SpoiledConfig.class);

		eventBus.addListener(this::setup);
		eventBus.register(new SpoiledConditions());

		SpoiledRecipes.RECIPE_SERIALIZERS.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new SpoilHandler());
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			//Initialize
			SpoiledRecipeTypes.init();
		});
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		SpoiledCommands.initializeCommands(event.getDispatcher());
	}
}
