package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.Constants;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
	@Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Z", cancellable = true, remap = false)
	private static void spoiledCancelShouldCauseReequipAnimation(@NotNull ItemStack from, @NotNull ItemStack to, int slot, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (from.hasTag() && from.getTag() != null && to.hasTag() && to.getTag() != null
				&& from.getTag().getInt(Constants.SPOIL_TAG) != to.getTag().getInt(Constants.SPOIL_TAG)) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
}

