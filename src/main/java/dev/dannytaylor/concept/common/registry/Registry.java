package dev.dannytaylor.concept.common.registry;

import dev.dannytaylor.concept.common.registry.entity.mannequin.MannequinProfiles;

public class Registry {
	public static void bootstrap() {
		StatusEffectRegistry.bootstrap();
		MannequinProfiles.bootstrap();
		CommandRegistry.bootstrap();
	}
}
