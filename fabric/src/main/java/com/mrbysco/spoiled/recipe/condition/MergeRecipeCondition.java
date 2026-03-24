package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class MergeRecipeCondition implements ResourceCondition {
	public static final MapCodec<MergeRecipeCondition> CODEC = MapCodec.unit(MergeRecipeCondition::new);
	public static final Identifier ID = Constants.modLoc("merge_food");

	public static final ResourceConditionType<MergeRecipeCondition> PROVIDER = ResourceConditionType.create(ID, CODEC);

	@Override
	public ResourceConditionType<?> getType() {
		return PROVIDER;
	}

	@Override
	public boolean test(@Nullable RegistryOps.RegistryInfoLookup registryInfo) {
		return SpoiledConfig.COMMON.mergeSpoilingFood.get();
	}
}