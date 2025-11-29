package dev.dannytaylor.concept.client.registry;

import dev.dannytaylor.concept.client.registry.entity.ClientEntityRegistry;
import dev.dannytaylor.concept.client.registry.pipeline.PostEffectRegistry;

public class ClientRegistry {
	public static void bootstrap() {
		ClientEntityRegistry.bootstrap();
		PostEffectRegistry.bootstrap();
	}
}
