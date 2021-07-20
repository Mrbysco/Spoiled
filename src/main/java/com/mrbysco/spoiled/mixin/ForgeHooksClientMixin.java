package com.mrbysco.spoiled.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    @Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)Z", cancellable = true, remap = false)
    private static void cancelShouldCauseReequipAnimation(@Nonnull ItemStack from, @Nonnull ItemStack to, int slot, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(from.hasTag() && from.getTag() != null && to.hasTag() && to.getTag() != null
                && from.getTag().getInt("spoiled:SpoilTimer") != to.getTag().getInt("spoiled:SpoilTimer")) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}

