package com.mrbysco.spoiled.containers;

import com.mrbysco.spoiled.CommonClass;
import com.mrbysco.spoiled.containers.handlers.HandlerBundle;
import com.mrbysco.spoiled.containers.handlers.HandlerShulkerbox;
import com.mrbysco.spoiled.platform.Services;

public class ContainerRegistrar {
	public static void register() {
		CommonClass.containerManager.register(new HandlerShulkerbox());
		CommonClass.containerManager.register(new HandlerBundle());
		Services.PLATFORM.registerContainerManagers();
	}
}
