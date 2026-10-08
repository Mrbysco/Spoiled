package com.mrbysco.spoiled;

import com.mrbysco.spoiled.containers.ContainerManager;
import com.mrbysco.spoiled.registration.SpoiledComponents;
import com.mrbysco.spoiled.registration.SpoiledRecipes;

public class CommonClass {

	public static ContainerManager containerManager;

	public static void init() {
		containerManager = new ContainerManager();

		SpoiledComponents.loadClass();
		SpoiledRecipes.loadClass();
	}
}