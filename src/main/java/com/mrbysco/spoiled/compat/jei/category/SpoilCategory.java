package com.mrbysco.spoiled.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.spoiled.Reference;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpoilCategory implements IRecipeCategory<SpoilRecipe> {
	public static final RecipeType<SpoilRecipe> TYPE = RecipeType.create(Reference.MOD_ID, "spoil_recipe", SpoilRecipe.class);
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
		builder.addSlot(RecipeIngredientRole.INPUT, 10, 14).addIngredients(recipe.getIngredients().get(0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 14).addItemStack(recipe.getResultItem());
	}

	@Override
	public void draw(SpoilRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);

		this.slotDrawable.draw(stack, 9, 13);

		stack.pushPose();
		stack.translate(1, 0, 0);
		Font font = Minecraft.getInstance().font;
		MutableComponent component = Component.translatable("spoiled.gui.jei.spoil_time", recipe.getSpoilTime());
		font.draw(stack, component, 0, 0, 8);
		stack.popPose();

		this.slotDrawable.draw(stack, 112, 13);

	}
}
