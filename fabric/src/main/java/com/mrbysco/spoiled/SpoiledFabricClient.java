package com.mrbysco.spoiled;

import com.mrbysco.spoiled.config.SpoiledClientConfig;
import com.mrbysco.spoiled.util.TooltipUtil;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class SpoiledFabricClient implements ClientModInitializer {
	private Thread watchThread = null;
	public static SpoiledClientConfig config;

	@Override
	public void onInitializeClient() {
		ConfigHolder<SpoiledClientConfig> holder = AutoConfig.register(SpoiledClientConfig.class, Toml4jConfigSerializer::new);

		config = holder.getConfig();

		try {
			var watchService = FileSystems.getDefault().newWatchService();
			Paths.get("config").register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			watchThread = new Thread(() -> {
				WatchKey key;
				try {
					while ((key = watchService.take()) != null) {
						if (Thread.currentThread().isInterrupted()) {
							watchService.close();
							break;
						}
						for (WatchEvent<?> event : key.pollEvents()) {
							if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							if (((Path) event.context()).endsWith("spoiled-client.toml")) {
								Constants.LOGGER.info("Reloading Spoiled's Client config");
								if (holder.load()) {
									config = holder.getConfig();
								}
							}
						}
						key.reset();
					}
				} catch (InterruptedException ignored) {
				} catch (IOException e) {
					Constants.LOGGER.error("Failed to close filesystem watcher", e);
				}
			}, "Spoiled's Client Config Watcher");
			watchThread.start();
		} catch (IOException e) {
			Constants.LOGGER.error("Failed to create filesystem watcher for configs", e);
		}

		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			Component component = TooltipUtil.getTooltip(stack);
			if (component != null) {
				lines.add(component);
			}
		});
	}
}
