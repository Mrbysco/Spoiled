package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.spoiled.Constants;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SpoiledConditions {
	public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Constants.MOD_ID);

	public static final DeferredHolder<Codec<? extends ICondition>, Codec<InitializeSpoilingCondition>> INITIALIZE_SPOILING = CONDITION_CODECS.register("initialize_spoiling", () -> InitializeSpoilingCondition.CODEC);
	public static final DeferredHolder<Codec<? extends ICondition>, Codec<MergeRecipeCondition>> MERGE_FOOD = CONDITION_CODECS.register("merge_food", () -> MergeRecipeCondition.CODEC);
}
