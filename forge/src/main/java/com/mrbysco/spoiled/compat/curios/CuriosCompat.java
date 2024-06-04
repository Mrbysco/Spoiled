package com.mrbysco.spoiled.compat.curios;

import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosCompat {
	public static void onCuriosTick(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player &&
				player.level().getGameTime() % SpoiledConfigCache.spoilRate == 0 && !player.getAbilities().instabuild) {
			final Level level = player.level();
			ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(player).orElse(null);
			if (curiosItemHandler == null) return;
			IItemHandlerModifiable equipped = curiosItemHandler.getEquippedCurios();
			int slots = equipped.getSlots();
			for (int i = 0; i < slots; i++) {
				ItemStack stack = equipped.getStackInSlot(i);
				if (!stack.isEmpty()) {
					IItemHandler itemHandler = stack.getCapability(Capabilities.ItemHandler.ITEM);
					if (itemHandler != null) {
						if (itemHandler.getSlots() > 0) {
							for (int j = 0; j < itemHandler.getSlots(); j++) {
								ItemStack nestedStack = itemHandler.getStackInSlot(j);
								if (nestedStack != null && !nestedStack.isEmpty()) {
									RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, nestedStack);
									if (recipeHolder != null) {
										SpoilRecipe recipe = recipeHolder.value();
										SpoilHelper.updateSpoilingStack(nestedStack, recipe);
										if (SpoilHelper.isSpoiled(nestedStack)) {
											SpoilHandler.spoilItemInHandler(itemHandler, j, nestedStack, recipe, level.registryAccess());
										}
									}
								}
							}
						}
					} else {
						RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, stack);
						if (recipeHolder != null) {
							SpoilRecipe recipe = recipeHolder.value();
							SpoilHelper.updateSpoilingStack(stack, recipe);
							if (SpoilHelper.isSpoiled(stack)) {
								SpoilHelper.spoilItemForPlayer(player, stack, recipe);
							}
						}
					}
				}
			}
		}
	}
}