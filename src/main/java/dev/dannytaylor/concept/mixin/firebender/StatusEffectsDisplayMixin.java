package dev.dannytaylor.concept.mixin.firebender;

import dev.dannytaylor.concept.client.data.ClientData;
import dev.dannytaylor.concept.common.registry.effect.FirebendingStatusEffect;
import dev.dannytaylor.concept.common.registry.entity.FirebendingEntity;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectsDisplay.class)
public abstract class StatusEffectsDisplayMixin {
	@Inject(at = @At("RETURN"), method = "getStatusEffectDescription", cancellable = true)
	private void concept$getStatusEffectDescription(StatusEffectInstance statusEffect, CallbackInfoReturnable<Text> cir) {
		ClientPlayerEntity player = ClientData.getMinecraft().player;
		if (player != null) {
			int level = ((FirebendingEntity)player).concept$getFireballLevel();
			if (statusEffect.getEffectType().value() instanceof FirebendingStatusEffect && level != 0) {
				cir.setReturnValue(Text.translatable("concept.fire_bender.effect", cir.getReturnValue().copyContentOnly(), level));
			}
		}
	}
}