package com.mrbysco.spoiled.handler;

import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipHandler {
    @SubscribeEvent
    public void onItemPickup(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            CompoundNBT tag = stack.getTag();
            int timer = tag.getInt(Reference.SPOIL_TAG);
            int timeMax = tag.getInt(Reference.SPOIL_TIME_TAG);
            int percentage = (int)(((double)timer / timeMax) * 100);

            ITextComponent component;
            if(SpoiledConfig.CLIENT.showPercentage.get()) {
                component = new TranslationTextComponent("spoiled.spoiling");
                component.applyTextStyle(TextFormatting.YELLOW);
                ITextComponent amount = new StringTextComponent(String.valueOf(percentage));
                amount.applyTextStyle(TextFormatting.RED);
                ITextComponent percentageComponent = new StringTextComponent("%");
                percentageComponent.applyTextStyle(TextFormatting.YELLOW);
                component.appendSibling(amount).appendSibling(percentageComponent);
            } else {
                if(percentage >= 0 && percentage <= 24) {
                    component = new TranslationTextComponent("spoiled.spoiling.0");
                    component.applyTextStyle(TextFormatting.GREEN);
                } else if (percentage >= 25 && percentage <= 49) {
                    component = new TranslationTextComponent("spoiled.spoiling.25");
                    component.applyTextStyle(TextFormatting.GREEN);
                } else if (percentage >= 50 && percentage <= 74) {
                    component = new TranslationTextComponent("spoiled.spoiling.50");
                    component.applyTextStyle(TextFormatting.YELLOW);
                } else if (percentage >= 75 && percentage <= 99) {
                    component = new TranslationTextComponent("spoiled.spoiling.75");
                    component.applyTextStyle(TextFormatting.YELLOW);
                } else {
                    component = new TranslationTextComponent("spoiled.spoiling.100");
                    component.applyTextStyle(TextFormatting.RED);
                }
            }

            event.getToolTip().add(component);
        }
    }

}
