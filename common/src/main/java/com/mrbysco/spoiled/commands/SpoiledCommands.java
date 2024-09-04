package com.mrbysco.spoiled.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.spoiled.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SpoiledCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Constants.MOD_ID);
		root.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.literal("list")
						.then(Commands.literal("blockentity").executes(SpoiledCommands::listBlockEntities))
						.then(Commands.literal("food").executes(SpoiledCommands::listFood)));
		dispatcher.register(root);
	}

	private static int listBlockEntities(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		source.sendSuccess(() -> Component.translatable("spoiled.command.blockentity_list.message").withStyle(ChatFormatting.YELLOW), true);

		List<ResourceLocation> keys = new ArrayList<>(BuiltInRegistries.BLOCK_ENTITY_TYPE.keySet());
		Constants.LOGGER.info("List of Block Entities requested by {}", source.getTextName());
		keys.forEach(t -> Constants.LOGGER.info(t.toString()));

		return 0;
	}

	private static int listFood(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		source.sendSuccess(() -> Component.translatable("spoiled.command.food_list.message").withStyle(ChatFormatting.YELLOW), true);

		List<Item> foodItems = BuiltInRegistries.ITEM.stream().filter(item -> item.getDefaultInstance().has(DataComponents.FOOD)).toList();
		List<ResourceLocation> keys = foodItems.stream().map(BuiltInRegistries.ITEM::getKey).toList();
		Constants.LOGGER.info("List of Foods requested by {}:", source.getTextName());
		keys.forEach(t -> Constants.LOGGER.info(t.toString()));

		return 0;
	}
}
