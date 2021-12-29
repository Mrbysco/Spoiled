package com.mrbysco.spoiled.util;

import com.mrbysco.spoiled.mixin.ChunkMapAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkHelper {

	public static List<BlockPos> getBlockEntityPositions(Level level) {
		ChunkSource source = level.getChunkSource();
		List<BlockPos> positions = new ArrayList<>();

		if (source instanceof ServerChunkCache cache) {
			ChunkMap chunkMap = cache.chunkMap;
			Iterable<ChunkHolder> chunks = ((ChunkMapAccessor) chunkMap).spoiledCallGetChunks();
			for (ChunkHolder chunk : chunks) {
				LevelChunk levelChunk = chunk.getTickingChunk();

				if (levelChunk != null) {
					ChunkAccess access = chunk.getLastAvailable();

					if (access != null) {
						positions.addAll(access.getBlockEntitiesPos());
					}
				}
			}
		}
		return positions;
	}
}
