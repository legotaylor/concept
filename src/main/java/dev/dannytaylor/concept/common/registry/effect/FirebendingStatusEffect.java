package dev.dannytaylor.concept.common.registry.effect;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;

public class FirebendingStatusEffect extends ConceptStatusEffect {
	public FirebendingStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	public FirebendingStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
		super(category, color, particleEffect);
	}
}
