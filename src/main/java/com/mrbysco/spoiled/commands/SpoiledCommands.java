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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SpoiledCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Reference.MOD_ID);
		root.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.literal("tileentity").then(Commands.literal("list").executes(SpoiledCommands::listTiles)));
		dispatcher.register(root);
	}

	private static int listTiles(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		Component text = new TranslatableComponent("spoiled:tileentity.list.message").withStyle(ChatFormatting.YELLOW);
		source.sendSuccess(text, true);

		List<ResourceLocation> keys = new ArrayList<>(ForgeRegistries.BLOCK_ENTITIES.getKeys());
		Spoiled.LOGGER.info("List of TileEntities requested by " + source.getTextName() + ":");
		keys.forEach(t -> Spoiled.LOGGER.info(t.toString()));

		return 0;
	}
}
