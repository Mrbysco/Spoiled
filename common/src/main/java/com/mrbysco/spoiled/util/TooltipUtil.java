package com.mrbysco.spoiled.util;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TooltipUtil {
	/**
	 * Get spoiling tooltip for itemstack if it has the spoil tag
	 * @param stack The itemstack to check
	 * @return The tooltip to display or null if it doesn't have the spoil tag
	 */
	@Nullable
	public static Component getTooltip(ItemStack stack) {
		if (stack.hasTag() && !stack.getTag().isEmpty() && stack.getTag().contains(Constants.SPOIL_TAG)) {
			CompoundTag tag = stack.getTag();
			int timer = tag.getInt(Constants.SPOIL_TAG);
			int timeMax = tag.getInt(Constants.SPOIL_TIME_TAG);
			int percentage = (int) (((double) timer / timeMax) * 100);

			MutableComponent component;
			if (Services.PLATFORM.showPercentage()) {
				component = Component.translatable("spoiled.spoiling").withStyle(ChatFormatting.YELLOW);
				Component amount = Component.literal(String.valueOf(percentage)).withStyle(ChatFormatting.RED);
				Component percentageComponent = Component.literal("%").withStyle(ChatFormatting.YELLOW);
				component.append(amount).append(percentageComponent);
			} else {
				if (percentage >= 0 && percentage <= 24) {
					component = Component.translatable("spoiled.spoiling.0").withStyle(ChatFormatting.GREEN);
				} else if (percentage >= 25 && percentage <= 49) {
					component = Component.translatable("spoiled.spoiling.25").withStyle(ChatFormatting.GREEN);
				} else if (percentage >= 50 && percentage <= 74) {
					component = Component.translatable("spoiled.spoiling.50").withStyle(ChatFormatting.YELLOW);
				} else if (percentage >= 75 && percentage <= 99) {
					component = Component.translatable("spoiled.spoiling.75").withStyle(ChatFormatting.YELLOW);
				} else {
					component = Component.translatable("spoiled.spoiling.100").withStyle(ChatFormatting.RED);
				}
			}

			return component;
		}
		return null;
	}

}
