package dev.dannytaylor.concept.mixin.piss;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

// Default potions give the piss effect as water is now piss (this is the weridest mod i've ever made................)

@Mixin(Potions.class)
public abstract class PotionsMixin {
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void concept$replaceWaterPotion(String name, Potion potion, CallbackInfoReturnable<RegistryEntry<Potion>> cir) {
		if (name.equals("water") || name.equals("mundane") || name.equals("thick") || name.equals("awkward")) {
			cir.setReturnValue(Registry.registerReference(Registries.POTION, Identifier.ofVanilla(name), new Potion(name, concept$getPissEffect())));
		}
	}

	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potions;register(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Lnet/minecraft/registry/entry/RegistryEntry;"))
	private static RegistryEntry<Potion> concept$replaceWaterPotion(String name, Potion potion) {
		List<StatusEffectInstance> effects = new ArrayList<>(potion.getEffects());
		effects.add(concept$getPissEffect());
		return concept$register(name, new Potion(potion.getBaseName(), effects.toArray(new StatusEffectInstance[0])));
	}

	@Unique
	private static RegistryEntry<Potion> concept$register(String name, Potion potion) {
		return Registry.registerReference(Registries.POTION, Identifier.ofVanilla(name), potion);
	}

	@Unique
	private static StatusEffectInstance concept$getPissEffect() {
		return new StatusEffectInstance(StatusEffectRegistry.piss, 200, 0);
	}
}