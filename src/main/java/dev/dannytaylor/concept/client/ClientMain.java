package dev.dannytaylor.concept.client;

import dev.dannytaylor.concept.client.gui.Overlays;
import dev.dannytaylor.concept.client.registry.ClientRegistry;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientRegistry.bootstrap();
		Overlays.bootstrap();
	}
}