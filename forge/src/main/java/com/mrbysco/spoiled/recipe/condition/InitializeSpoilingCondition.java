package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class InitializeSpoilingCondition implements ICondition {
	public static final InitializeSpoilingCondition INSTANCE = new InitializeSpoilingCondition();

	public static final Codec<InitializeSpoilingCondition> CODEC = Codec.unit(INSTANCE).stable();

	private InitializeSpoilingCondition() {
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return SpoiledConfig.COMMON.initializeSpoiling.get();
	}
}