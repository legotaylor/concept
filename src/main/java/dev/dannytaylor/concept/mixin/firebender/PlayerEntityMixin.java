package dev.dannytaylor.concept.mixin.firebender;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void concept$addEffectIfNotAlready(CallbackInfo ci) {
		if (!this.hasStatusEffect(StatusEffectRegistry.fireBender)) this.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.fireBender, -1, 0, true, false));
	}
}