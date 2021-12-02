package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.Reference;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    @Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Z", cancellable = true, remap = false)
    private static void spoiledCancelShouldCauseReequipAnimation(@Nonnull ItemStack from, @Nonnull ItemStack to, int slot, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(from.hasTag() && from.getTag() != null && to.hasTag() && to.getTag() != null
                && from.getTag().getInt(Reference.SPOIL_TAG) != to.getTag().getInt(Reference.SPOIL_TAG)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}

