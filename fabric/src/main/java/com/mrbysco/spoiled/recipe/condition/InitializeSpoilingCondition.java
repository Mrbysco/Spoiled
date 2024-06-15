package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.SpoiledFabric;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class InitializeSpoilingCondition implements ResourceCondition {
	public static final MapCodec<InitializeSpoilingCondition> CODEC = MapCodec.unit(InitializeSpoilingCondition::new);
	public static final ResourceLocation ID = Constants.modLoc("initialize_spoiling");

	public static final ResourceConditionType<InitializeSpoilingCondition> PROVIDER = ResourceConditionType.create(ID, CODEC);

	@Override
	public ResourceConditionType<?> getType() {
		return PROVIDER;
	}

	@Override
	public boolean test(@Nullable HolderLookup.Provider registryLookup) {
		return SpoiledFabric.config.get().general.initializeSpoiling;
	}
}