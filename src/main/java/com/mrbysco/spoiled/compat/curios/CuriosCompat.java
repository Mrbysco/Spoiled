package com.mrbysco.spoiled.compat.curios;

import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.handler.SpoilHandler;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
	public static void onCuriosTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !event.player.level.isClientSide &&
				event.player.level.getGameTime() % SpoiledConfigCache.spoilRate == 0 && !event.player.getAbilities().instabuild) {
			final Player player = event.player;
			final Level level = player.level;
			LazyOptional<IItemHandlerModifiable> optionalEquipped = CuriosApi.getCuriosHelper().getEquippedCurios(player);
			optionalEquipped.ifPresent(equipped -> {
				int slots = equipped.getSlots();
				for (int i = 0; i < slots; i++) {
					ItemStack stack = equipped.getStackInSlot(i);
					if (!stack.isEmpty()) {
						if (stack.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
							stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
								if (itemHandler.getSlots() > 0) {
									for (int j = 0; j < itemHandler.getSlots(); j++) {
										ItemStack nestedStack = itemHandler.getStackInSlot(j);
										if (nestedStack != null && !nestedStack.isEmpty()) {
											SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, nestedStack);
											if (recipe != null) {
												SpoilHandler.updateSpoilingStack(nestedStack, recipe);
												if (SpoilHandler.isSpoiled(nestedStack)) {
													SpoilHandler.spoilItemInHandler(itemHandler, j, nestedStack, recipe, level.registryAccess());
												}
											}
										}
									}
								}
							});
						} else {
							SpoilRecipe recipe = SpoilHelper.getSpoilRecipe(level, stack);
							if (recipe != null) {
								SpoilHandler.updateSpoilingStack(stack, recipe);
								if (SpoilHandler.isSpoiled(stack)) {
									SpoilHandler.spoilItemForPlayer(player, stack, recipe);
								}
							}
						}
					}
				}
			});
		}
	}
}