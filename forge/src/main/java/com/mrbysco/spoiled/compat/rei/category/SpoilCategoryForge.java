package com.mrbysco.spoiled.compat.rei.category;

import com.mrbysco.spoiled.compat.rei.REIPluginForge;
import com.mrbysco.spoiled.compat.rei.display.SpoilDisplayForge;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SpoilCategoryForge implements DisplayCategory<SpoilDisplayForge> {
    @Override
    public CategoryIdentifier<? extends SpoilDisplayForge> getCategoryIdentifier() {
        return REIPluginForge.SPOILING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("spoiled.gui.jei.category.spoiling");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.ROTTEN_FLESH);
    }

    @Override
    public List<Widget> setupDisplay(SpoilDisplayForge display, Rectangle bounds) {
        Point centerPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 10, bounds.getMinY() + 12)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createArrow(new Point(centerPoint.getX() - 12, centerPoint.getY() - 8)));
        widgets.add(Widgets.createSlot(new Point(bounds.getMaxX() - 26, bounds.getMinY() + 12)).entries(display.getOutputEntries().get(0)).markOutput());

        return widgets;
    }

    @Override
    public int getDisplayWidth(SpoilDisplayForge display) {
        return 100;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }
}
