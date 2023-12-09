package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.neoforged.neoforge.common.conditions.ICondition;

public class MergeRecipeCondition implements ICondition {
	public static final MergeRecipeCondition INSTANCE = new MergeRecipeCondition();

	public static final Codec<MergeRecipeCondition> CODEC = Codec.unit(INSTANCE).stable();

	private MergeRecipeCondition() {
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return SpoiledConfig.COMMON.mergeSpoilingFood.get();
	}
}