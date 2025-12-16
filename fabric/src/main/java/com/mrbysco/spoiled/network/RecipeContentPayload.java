package com.mrbysco.spoiled.network;

import com.mrbysco.spoiled.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record RecipeContentPayload(
		Set<RecipeType<?>> recipeTypes,
		List<RecipeHolder<?>> recipes) implements CustomPacketPayload {
	public static final Type<RecipeContentPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "sync_recipes"));

	public static final StreamCodec<RegistryFriendlyByteBuf, RecipeContentPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.RECIPE_TYPE).apply(ByteBufCodecs.collection(HashSet::new)), RecipeContentPayload::recipeTypes,
			RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs.list()), RecipeContentPayload::recipes,
			RecipeContentPayload::new);

	public static RecipeContentPayload create(Collection<RecipeType<?>> recipeTypes, Collection<RecipeHolder<?>> recipes) {
		var recipeTypeSet = Set.copyOf(recipeTypes);
		// Fast-path for empty recipe type set (if no mod wants to sync anything)
		if (recipeTypeSet.isEmpty()) {
			return new RecipeContentPayload(recipeTypeSet, List.of());
		} else {
			var recipeSubset = recipes.stream().filter(h -> recipeTypeSet.contains(h.value().getType())).toList();
			return new RecipeContentPayload(recipeTypeSet, recipeSubset);
		}
	}

	@Override
	public Type<RecipeContentPayload> type() {
		return TYPE;
	}
}
