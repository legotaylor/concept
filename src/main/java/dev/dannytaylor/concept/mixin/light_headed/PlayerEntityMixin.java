package dev.dannytaylor.concept.mixin.light_headed;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

	@Inject(at = @At("HEAD"), method = "updatePose", cancellable = true)
	private void concept$updatePose(CallbackInfo ci) {
		if (this.hasStatusEffect(StatusEffectRegistry.lightHeaded)) {
			this.setPose(EntityPose.SWIMMING);
			ci.cancel();
		}
	}
}