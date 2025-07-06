package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.SpoiledFabric;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {


	@Shadow @Final private MinecraftServer server;

	@Inject(at = @At("TAIL"), method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V")
	private void spoiled_tick(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo info) {
		SpoiledFabric.sendRecipes(serverPlayer, this.server.getRecipeManager().getRecipes());
	}
}
