package com.mrbysco.spoiled.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.Spoiled;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SpoiledCommands {
	public static void initializeCommands (CommandDispatcher<CommandSource> dispatcher) {
		final LiteralArgumentBuilder<CommandSource> root = Commands.literal(Reference.MOD_ID);
		root.requires((commandSource) -> commandSource.hasPermissionLevel(2))
				.then(Commands.literal("tileentity").then(Commands.literal("list").executes(SpoiledCommands::listTiles)));
		dispatcher.register(root);
	}

	private static int listTiles(CommandContext<CommandSource> ctx) {
		CommandSource source = ctx.getSource();

		ITextComponent text = new TranslationTextComponent("spoiled:tileentity.list.message").mergeStyle(TextFormatting.YELLOW);
		source.sendFeedback(text, true);

		List<ResourceLocation> keys = new ArrayList<>(ForgeRegistries.TILE_ENTITIES.getKeys());
		Spoiled.LOGGER.info("List of TileEntities requested by " + source.getName() + ":");
		keys.forEach(t -> Spoiled.LOGGER.info(t.toString()));

		return 0;
	}
}
