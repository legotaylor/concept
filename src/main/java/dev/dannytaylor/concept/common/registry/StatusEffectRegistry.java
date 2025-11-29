package dev.dannytaylor.concept.common.registry;

import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.effect.ConceptStatusEffect;
import dev.dannytaylor.concept.common.registry.effect.FirebendingStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class StatusEffectRegistry {
	public static final RegistryEntry<StatusEffect> piss;
	public static final RegistryEntry<StatusEffect> lightHeaded;
	public static final RegistryEntry<StatusEffect> stare;
	public static final RegistryEntry<StatusEffect> fireBender;

	public static RegistryEntry<StatusEffect> register(Identifier id, StatusEffect statusEffect) {
		return Registry.registerReference(Registries.STATUS_EFFECT, id, statusEffect);
	}

	public static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
		return register(Data.idOf(id), statusEffect);
	}

	public static void bootstrap() {
	}

	static {
		piss = register("piss", new ConceptStatusEffect(StatusEffectCategory.HARMFUL, 0xffd33e));
		lightHeaded = register("light_headed", new ConceptStatusEffect(StatusEffectCategory.HARMFUL, 0x505089));
		stare = register("stare", new ConceptStatusEffect(StatusEffectCategory.HARMFUL, 0xFFFFFF));
		fireBender = register("fire_bender", new FirebendingStatusEffect(StatusEffectCategory.HARMFUL, 0xEEAC18));
	}
}
