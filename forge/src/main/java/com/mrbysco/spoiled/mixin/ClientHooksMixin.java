package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.registration.SpoiledComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {
	@Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Z", cancellable = true, remap = false)
	private static void spoiledCancelShouldCauseReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, int slot, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (from.has(SpoiledComponents.SPOIL_TIMER) && from.has(SpoiledComponents.SPOIL_TIMER) &&
				from.get(SpoiledComponents.SPOIL_TIMER) != to.get(SpoiledComponents.SPOIL_TIMER)) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
}

