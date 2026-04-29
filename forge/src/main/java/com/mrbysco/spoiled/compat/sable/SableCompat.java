package com.mrbysco.spoiled.compat.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Mod support for the Sable mod
 */
public class SableCompat {


	/**
	 * Finds an item handler at the given position, searching through sublevels if necessary.
	 *
	 * @param level The level
	 * @param pos   The position
	 * @return The item handler, or null if none was found
	 */
	@Nullable
	public static IItemHandler findItemHandler(Level level, BlockPos pos) {
		final SubLevel containing = Sable.HELPER.getContaining(level, pos);

		return Sable.HELPER.runIncludingSubLevels(
				level,
				pos.getCenter(),
				true,
				containing,
				(subLevel, candidatePos) -> level.getCapability(Capabilities.ItemHandler.BLOCK, candidatePos, null)
		);
	}
}
