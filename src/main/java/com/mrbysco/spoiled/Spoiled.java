package com.mrbysco.spoiled;

import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.registry.SpoilRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Spoiled
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Spoiled() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SpoiledConfig.serverSpec);
        eventBus.register(SpoiledConfig.class);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SpoilHandler());

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        });
    }

    @SubscribeEvent
    public void serverStart(FMLServerStartingEvent event) {
        SpoilRegistry.initializeSpoiling();
    }
}
