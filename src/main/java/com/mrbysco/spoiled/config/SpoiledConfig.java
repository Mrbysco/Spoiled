package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Spoiled;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

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

    public static class Server {
        public final ConfigValue<List<? extends String>> containerBlacklist;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server settings")
                    .push("Server");

            String[] containers = new String[]
                    {
                            "ShulkerBoxTileEntity"
                    };

            containerBlacklist = builder
                    .comment("A list of containers in which food does not spoil. To blacklist add the tileentity's class name")
                    .defineList("containerBlacklist", Arrays.asList(containers), o -> (o instanceof String));

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

    public static final ForgeConfigSpec serverSpec;
    public static final SpoiledConfig.Server SERVER;

    static {
        final Pair<SpoiledConfig.Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(SpoiledConfig.Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
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
