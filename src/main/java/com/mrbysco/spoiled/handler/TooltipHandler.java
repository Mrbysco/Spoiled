package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
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
        if(stack.getTag() != null && !stack.getTag().isEmpty() && stack.getTag().contains(Reference.SPOIL_TAG)) {
            CompoundNBT tag = stack.getTag();
            int timer = tag.getInt(Reference.SPOIL_TAG);
            int timeMax = tag.getInt(Reference.SPOIL_TIME_TAG);
            int percentage = (int)(((double)timer / timeMax) * 100);

            TextComponent component;
            if(SpoiledConfig.CLIENT.showPercentage.get()) {
                component = (TextComponent)new TranslationTextComponent("spoiled.spoiling").mergeStyle(TextFormatting.YELLOW);
                ITextComponent amount = new StringTextComponent(String.valueOf(percentage)).mergeStyle(TextFormatting.RED);
                ITextComponent percentageComponent = new StringTextComponent("%").mergeStyle(TextFormatting.YELLOW);
                component.append(amount).append(percentageComponent);
            } else {
                if(percentage >= 0 && percentage <= 24) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.0").mergeStyle(TextFormatting.GREEN);
                } else if (percentage >= 25 && percentage <= 49) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.25").mergeStyle(TextFormatting.GREEN);
                } else if (percentage >= 50 && percentage <= 74) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.50").mergeStyle(TextFormatting.YELLOW);
                } else if (percentage >= 75 && percentage <= 99) {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.75").mergeStyle(TextFormatting.YELLOW);
                } else {
                    component = (TextComponent)new TranslationTextComponent("spoiled.spoiling.100").mergeStyle(TextFormatting.RED);
                }
            }

            event.getToolTip().add(component);
        }
    }

}
