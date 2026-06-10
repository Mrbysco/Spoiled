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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

public class CuriosCompat {
	public static void onCuriosTick(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player &&
				player.level().getGameTime() % SpoiledConfigCache.spoilRate == 0 && !player.getAbilities().instabuild) {
			final Level level = player.level();
			ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(player).orElse(null);
			if (curiosItemHandler == null) return;

			for (Map.Entry<String, ICurioStacksHandler> entry : curiosItemHandler.getCurios().entrySet()) {
				IDynamicStackHandler stacks = entry.getValue().getStacks();
				for (int i = 0; i < stacks.getSlots(); i++) {
					ItemStack stack = stacks.getStackInSlot(i);
					if (!stack.isEmpty()) {
						ResourceHandler<ItemResource> resourceHandler = ItemAccess.forStack(stack).getCapability(Capabilities.Item.ITEM);
						if (resourceHandler != null) {
							if (resourceHandler.size() > 0) {
								for (int j = 0; j < resourceHandler.size(); j++) {
									ItemResource nestedResource = resourceHandler.getResource(j);
									if (!nestedResource.isEmpty()) {
										int count = resourceHandler.getAmountAsInt(i);
										ItemStack nestedStack = nestedResource.toStack(count);
										RecipeHolder<SpoilRecipe> recipeHolder = SpoilHelper.getSpoilRecipe(level, nestedStack);
										if (recipeHolder != null) {
											SpoilRecipe recipe = recipeHolder.value();
											SpoilHandler.spoilItemInHandler(stack, resourceHandler, j, nestedStack, recipe, level.registryAccess(), level.getRandom());
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
}