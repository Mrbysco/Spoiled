package com.mrbysco.spoiled.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SpoilTimer(int timer, int maxTime) {
	public static final Codec<SpoilTimer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.INT.fieldOf("timer").forGetter(SpoilTimer::timer),
					Codec.INT.fieldOf("maxTime").forGetter(SpoilTimer::maxTime))
			.apply(inst, SpoilTimer::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SpoilTimer> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			spoilTimer -> spoilTimer.timer,
			ByteBufCodecs.INT,
			spoilTimer -> spoilTimer.maxTime,
			SpoilTimer::new
	);

	public SpoilTimer(int maxTime) {
		this(0, maxTime);
	}
}
