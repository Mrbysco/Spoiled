package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class MergeRecipeCondition implements ICondition {
	public static final MergeRecipeCondition INSTANCE = new MergeRecipeCondition();

	public static final MapCodec<MergeRecipeCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	private MergeRecipeCondition() {
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return SpoiledConfig.COMMON.mergeSpoilingFood.get();
	}
}