package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.Constants;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SpoiledConditions {
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Constants.MOD_ID);

	public static final Supplier<MapCodec<InitializeSpoilingCondition>> INITIALIZE_SPOILING = CONDITION_CODECS.register("initialize_spoiling", () -> InitializeSpoilingCondition.CODEC);
	public static final Supplier<MapCodec<MergeRecipeCondition>> MERGE_FOOD = CONDITION_CODECS.register("merge_food", () -> MergeRecipeCondition.CODEC);
}
