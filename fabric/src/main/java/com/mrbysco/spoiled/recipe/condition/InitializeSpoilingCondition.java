package com.mrbysco.spoiled.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.config.SpoiledConfig;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import org.jetbrains.annotations.Nullable;

public class InitializeSpoilingCondition implements ResourceCondition {
	public static final MapCodec<InitializeSpoilingCondition> CODEC = MapCodec.unit(InitializeSpoilingCondition::new);
	public static final Identifier ID = Constants.modLoc("initialize_spoiling");

	public static final ResourceConditionType<InitializeSpoilingCondition> PROVIDER = ResourceConditionType.create(ID, CODEC);

	@Override
	public ResourceConditionType<?> getType() {
		return PROVIDER;
	}

	@Override
	public boolean test(@Nullable RegistryOps.RegistryInfoLookup registryInfo) {
		return SpoiledConfig.COMMON.initializeSpoiling.get();
	}
}