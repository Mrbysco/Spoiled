package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipHandler {
    @SubscribeEvent
    public void onItemPickup(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(stack.hasTag() && !stack.getTag().isEmpty() && stack.getTag().contains(Reference.SPOIL_TAG)) {
            CompoundNBT tag = stack.getTag();
            int timer = tag.getInt(Reference.SPOIL_TAG);
            int timeMax = tag.getInt(Reference.SPOIL_TIME_TAG);
            int percentage = (int)(((double)timer / timeMax) * 100);

            TextComponent component;
            if(SpoiledConfigCache.showPercentage) {
                component = (TextComponent)new TranslationTextComponent("spoiled.spoiling").withStyle(TextFormatting.YELLOW);
                ITextComponent amount = new StringTextComponent(String.valueOf(percentage)).withStyle(TextFormatting.RED);
                ITextComponent percentageComponent = new StringTextComponent("%").withStyle(TextFormatting.YELLOW);
                component.append(amount).append(percentageComponent);
            } else {
                if(percentage >= 0 && percentage <= 24) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.0").withStyle(TextFormatting.GREEN);
                } else if (percentage >= 25 && percentage <= 49) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.25").withStyle(TextFormatting.GREEN);
                } else if (percentage >= 50 && percentage <= 74) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.50").withStyle(TextFormatting.YELLOW);
                } else if (percentage >= 75 && percentage <= 99) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.75").withStyle(TextFormatting.YELLOW);
                } else {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.100").withStyle(TextFormatting.RED);
                }
            }

            event.getToolTip().add(component);
        }
    }

}
