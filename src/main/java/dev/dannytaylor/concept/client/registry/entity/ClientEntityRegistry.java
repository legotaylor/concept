package dev.dannytaylor.concept.client.registry.entity;

public class ClientEntityRegistry {
	public static void bootstrap() {
		EntityModelRegistry.bootstrap();
		EntityRendererRegistry.bootstrap();
	}
}
