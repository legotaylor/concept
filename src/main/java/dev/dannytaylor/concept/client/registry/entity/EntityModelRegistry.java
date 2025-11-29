package dev.dannytaylor.concept.client.registry.entity;

import dev.dannytaylor.concept.common.data.Data;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.WindChargeEntityModel;

public class EntityModelRegistry {
	public static final EntityModelLayer fireball;

	public static void bootstrap() {
		EntityModelLayerRegistry.registerModelLayer(fireball, WindChargeEntityModel::getTexturedModelData);
	}

	static {
		fireball = new EntityModelLayer(Data.idOf("fireball"), "main");
	}
}
