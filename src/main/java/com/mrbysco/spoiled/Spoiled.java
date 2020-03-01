package com.mrbysco.spoiled;

import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.handler.TooltipHandler;
import com.mrbysco.spoiled.registry.SpoilRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

@Mod(Reference.MOD_ID)
public class Spoiled
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Spoiled() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpoiledConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SpoiledConfig.serverSpec);

        eventBus.register(SpoiledConfig.class);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SpoilHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        // InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().
        //        map(m->m.getMessageSupplier().get()).
        //        collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void serverStart(FMLServerStartingEvent event) {
        SpoilRegistry.initializeSpoiling();

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if(item.isFood()) {
                System.out.println(item.getRegistryName());
            }
        }
    }
}
