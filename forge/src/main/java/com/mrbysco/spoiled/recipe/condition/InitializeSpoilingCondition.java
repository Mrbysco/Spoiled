package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class InitializeSpoilingCondition implements ICondition {
	public static final InitializeSpoilingCondition INSTANCE = new InitializeSpoilingCondition();

	public static final MapCodec<InitializeSpoilingCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	private InitializeSpoilingCondition() {
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return SpoiledConfig.COMMON.initializeSpoiling.get();
	}
}