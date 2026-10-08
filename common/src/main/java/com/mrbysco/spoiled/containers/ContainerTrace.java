package com.mrbysco.spoiled.containers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class ContainerTrace {

	private final HashSet<Integer> visited = new HashSet<>();

	public final ServerLevel level;
	@Nullable
	public final ServerPlayer player;

	public ContainerTrace(ServerLevel level) {
		this.level = level;
		this.player = null;
		visited.add(-1);
	}

	public ContainerTrace(ServerPlayer player) {
		this.player = player;
		this.level = player.serverLevel();
		visited.add(-1);
	}

	public boolean push(int sig) {
		if (visited.contains(sig)) {
			return false;
		}
		visited.add(sig);
		return true;
	}
}