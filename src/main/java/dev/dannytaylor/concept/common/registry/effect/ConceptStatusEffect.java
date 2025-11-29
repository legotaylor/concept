package dev.dannytaylor.concept.common.registry.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleEffect;

public class ConceptStatusEffect extends StatusEffect {
	public ConceptStatusEffect(StatusEffectCategory category, int color) {
		super(category, color);
	}

	public ConceptStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
		super(category, color, particleEffect);
	}
}
