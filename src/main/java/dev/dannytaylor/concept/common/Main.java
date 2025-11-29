package dev.dannytaylor.concept.common;

import dev.dannytaylor.concept.common.registry.Registry;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		Registry.bootstrap();
	}
}