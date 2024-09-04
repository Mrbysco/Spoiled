package com.mrbysco.spoiled.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpoilCategory implements IRecipeCategory<SpoilRecipe> {
	public static final RecipeType<SpoilRecipe> TYPE = RecipeType.create(Constants.MOD_ID, "spoil_recipe", SpoilRecipe.class);
	private final IDrawable background;
	private final IDrawable icon;
	private final Component title;

	private final IDrawableStatic slotDrawable;

	public SpoilCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(140, 40);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ROTTEN_FLESH));
		this.title = Component.translatable("spoiled.gui.jei.category.spoiling");
		this.slotDrawable = guiHelper.getSlotDrawable();
	}

	@Override
	public RecipeType<SpoilRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SpoilRecipe recipe, IFocusGroup focuses) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();

		builder.addSlot(RecipeIngredientRole.INPUT, 10, 14).addIngredients(recipe.getIngredients().getFirst());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 14).addItemStack(recipe.getResultItem(registryAccess));
	}

	@Override
	public void draw(SpoilRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.slotDrawable.draw(guiGraphics, 9, 13);

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(1, 0, 0);
		Font font = Minecraft.getInstance().font;
		MutableComponent component = Component.translatable("spoiled.gui.jei.spoil_time", recipe.getSpoilTime());
		guiGraphics.drawString(font, component, 0, 0, 8, false);
		poseStack.popPose();

		this.slotDrawable.draw(guiGraphics, 112, 13);
	}
}
