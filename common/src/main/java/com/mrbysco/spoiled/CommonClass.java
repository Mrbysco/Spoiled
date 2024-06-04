package com.mrbysco.spoiled;

import com.mrbysco.spoiled.registration.SpoiledComponents;
import com.mrbysco.spoiled.registration.SpoiledRecipes;

public class CommonClass {

	public static void init() {
		SpoiledComponents.loadClass();
		SpoiledRecipes.loadClass();
	}
}