package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Spoiled;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class SpoiledConfig {
    public static class Client {
        public final BooleanValue showPercentage;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client settings")
                    .push("client");

            showPercentage = builder
                    .comment("When enabled makes the food's tooltips show percentages")
                    .define("showPercentage", false);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec clientSpec;
    public static final SpoiledConfig.Client CLIENT;
    static {
        final Pair<SpoiledConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SpoiledConfig.Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        Spoiled.LOGGER.debug("Loaded forge config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        Spoiled.LOGGER.debug("Forge config just got changed on the file system!");
    }
}
