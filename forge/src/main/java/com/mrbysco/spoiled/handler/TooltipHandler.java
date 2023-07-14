package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipHandler {
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		Component component = TooltipUtil.getTooltip(event.getItemStack());
		if (component != null) {
			event.getToolTip().add(component);
		}
	}
}
