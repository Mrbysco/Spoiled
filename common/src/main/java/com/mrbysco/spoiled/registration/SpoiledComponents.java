package com.mrbysco.spoiled.registration;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.component.SpoilTimer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class SpoiledComponents {
	public static final RegistrationProvider<DataComponentType<?>> DATA_COMPONENTS = RegistrationProvider.get(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

	public static final RegistryObject<DataComponentType<SpoilTimer>> SPOIL_TIMER = DATA_COMPONENTS.register("spoil_timer", () ->
			DataComponentType.<SpoilTimer>builder()
					.persistent(SpoilTimer.CODEC)
					.networkSynchronized(SpoilTimer.STREAM_CODEC)
					.build());

	// Called in the mod initializer / constructor in order to make sure that items are registered
	public static void loadClass() {
	}
}
