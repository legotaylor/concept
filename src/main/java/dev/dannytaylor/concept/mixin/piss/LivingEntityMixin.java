package dev.dannytaylor.concept.mixin.piss;

import dev.dannytaylor.concept.common.util.ConceptMathHelper;
import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"), method = "jump")
	private float concept$jumpSin(float value) {
		return ConceptMathHelper.sin(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;cos(F)F"), method = "jump")
	private float concept$jumpCos(float value) {
		return ConceptMathHelper.cos(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"), method = "calcGlidingVelocity")
	private float concept$calcGlidingVelocitySin(float value) {
		return ConceptMathHelper.sin(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Ljava/lang/Math;cos(D)D"), method = "calcGlidingVelocity")
	private double concept$calcGlidingVelocityCos(double value) {
		return ConceptMathHelper.cos(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"), method = "createItemEntity")
	private float concept$createItemEntitySin(float value) {
		return ConceptMathHelper.sin(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;cos(F)F"), method = "createItemEntity")
	private float concept$createItemEntityCos(float value) {
		return ConceptMathHelper.cos(value, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;rotateX(F)Lnet/minecraft/util/math/Vec3d;"), method = "spawnItemParticles")
	private Vec3d concept$spawnItemParticlesRotateX(Vec3d instance, float angle) {
		return ConceptMathHelper.rotateX(instance, angle, get());
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;rotateY(F)Lnet/minecraft/util/math/Vec3d;"), method = "spawnItemParticles")
	private Vec3d concept$spawnItemParticlesRotateY(Vec3d instance, float angle) {
		return ConceptMathHelper.rotateY(instance, angle, get());
	}

	@Inject(at = @At("RETURN"), method = "baseTick")
	private void concept$addEffectWhenInWater(CallbackInfo ci) {
		LivingEntity livingEntity = this.get();
		if (livingEntity.isTouchingWater()) {
			livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.piss, 1200, 0));
		}
	}

	@Unique
	private LivingEntity get() {
		return (LivingEntity) (Object) this;
	}
}