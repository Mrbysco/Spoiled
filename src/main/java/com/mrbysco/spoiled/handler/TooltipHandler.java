package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipHandler {
	@SubscribeEvent
	public void onItemPickup(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.hasTag() && !stack.getTag().isEmpty() && stack.getTag().contains(Reference.SPOIL_TAG)) {
			CompoundTag tag = stack.getTag();
			int timer = tag.getInt(Reference.SPOIL_TAG);
			int timeMax = tag.getInt(Reference.SPOIL_TIME_TAG);
			int percentage = (int) (((double) timer / timeMax) * 100);

			BaseComponent component;
			if (SpoiledConfigCache.showPercentage) {
				component = (BaseComponent) new TranslatableComponent("spoiled.spoiling").withStyle(ChatFormatting.YELLOW);
				Component amount = new TextComponent(String.valueOf(percentage)).withStyle(ChatFormatting.RED);
				Component percentageComponent = new TextComponent("%").withStyle(ChatFormatting.YELLOW);
				component.append(amount).append(percentageComponent);
			} else {
				if (percentage >= 0 && percentage <= 24) {
					component = (BaseComponent) new TranslatableComponent("spoiled.spoiling.0").withStyle(ChatFormatting.GREEN);
				} else if (percentage >= 25 && percentage <= 49) {
					component = (BaseComponent) new TranslatableComponent("spoiled.spoiling.25").withStyle(ChatFormatting.GREEN);
				} else if (percentage >= 50 && percentage <= 74) {
					component = (BaseComponent) new TranslatableComponent("spoiled.spoiling.50").withStyle(ChatFormatting.YELLOW);
				} else if (percentage >= 75 && percentage <= 99) {
					component = (BaseComponent) new TranslatableComponent("spoiled.spoiling.75").withStyle(ChatFormatting.YELLOW);
				} else {
					component = (BaseComponent) new TranslatableComponent("spoiled.spoiling.100").withStyle(ChatFormatting.RED);
				}
			}

			event.getToolTip().add(component);
		}
	}

}
