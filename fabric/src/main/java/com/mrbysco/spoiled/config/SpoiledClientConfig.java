package com.mrbysco.spoiled.config;

import com.mrbysco.spoiled.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Constants.MOD_ID)
public class SpoiledClientConfig implements ConfigData {

	@CollapsibleObject
	public Client client = new Client();

	public static class Client {
		@Comment("When enabled makes the food's tooltips show percentages [default: false]")
		public boolean showPercentage = false;
	}
}