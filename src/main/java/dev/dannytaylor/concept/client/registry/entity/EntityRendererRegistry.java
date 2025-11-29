package dev.dannytaylor.concept.client.registry.entity;

import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.entity.EntityType;

public class EntityRendererRegistry {
	public static void bootstrap() {
		EntityRendererFactories.register(EntityType.SMALL_FIREBALL, FireChargeEntityRenderer::new);
		EntityRendererFactories.register(EntityType.FIREBALL, (context) -> new FireChargeEntityRenderer<>(context, 3.0F));
	}
}
