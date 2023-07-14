package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.handler.SpoilHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void spoiled_tick(CallbackInfo info) {
		SpoilHandler.onPlayerTick((Player) (Object) this);
	}
}