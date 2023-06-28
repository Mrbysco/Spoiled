package com.mrbysco.spoiled.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.Spoiled;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpoiledCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Reference.MOD_ID);
		root.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.literal("list")
						.then(Commands.literal("blockentity").executes(SpoiledCommands::listBlockEntities))
						.then(Commands.literal("food").executes(SpoiledCommands::listFood)));
		dispatcher.register(root);
	}

	private static int listBlockEntities(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		source.sendSuccess(() -> Component.translatable("spoiled.command.blockentity_list.message").withStyle(ChatFormatting.YELLOW), true);

		List<ResourceLocation> keys = new ArrayList<>(ForgeRegistries.BLOCK_ENTITY_TYPES.getKeys());
		Spoiled.LOGGER.info("List of Block Entities requested by " + source.getTextName() + ":");
		keys.forEach(t -> Spoiled.LOGGER.info(t.toString()));

		return 0;
	}

	private static int listFood(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		source.sendSuccess(() -> Component.translatable("spoiled.command.food_list.message").withStyle(ChatFormatting.YELLOW), true);

		List<Item> foodItems = new ArrayList<>(ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.isEdible()).collect(Collectors.toList()));
		List<ResourceLocation> keys = foodItems.stream().map(item -> ForgeRegistries.ITEMS.getKey(item)).collect(Collectors.toList());
		Spoiled.LOGGER.info("List of Foods requested by " + source.getTextName() + ":");
		keys.forEach(t -> Spoiled.LOGGER.info(t.toString()));

		return 0;
	}
}
