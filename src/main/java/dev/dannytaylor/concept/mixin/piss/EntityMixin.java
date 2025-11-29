package dev.dannytaylor.concept.mixin.piss;

import dev.dannytaylor.concept.common.util.ConceptMathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"), method = "updateVelocity")
	private Vec3d concept$movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
		return isLivingEntity() ? concept$movementInputToVelocity(get(), movementInput, speed, yaw) : Entity.movementInputToVelocity(movementInput, speed, yaw); // accesswidened to allow for mod compat.
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"), method = "getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;")
	private float concept$getRotationVectorSin(float value) {
		return isLivingEntity() ? ConceptMathHelper.sin(value, get()) : value;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;cos(F)F"), method = "getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;")
	private float concept$getRotationVectorCos(float value) {
		return isLivingEntity() ? ConceptMathHelper.cos(value, get()) : value;
	}

	@Unique
	private static Vec3d concept$movementInputToVelocity(LivingEntity entity, Vec3d movementInput, float speed, float yaw) {
		double d = movementInput.lengthSquared();
		if (d < 1.0E-7) {
			return Vec3d.ZERO;
		} else {
			Vec3d vec3d = (d > (double)1.0F ? movementInput.normalize() : movementInput).multiply(speed);
			float f = ConceptMathHelper.sin(yaw * ((float)Math.PI / 180F), entity);
			float g = ConceptMathHelper.cos(yaw * ((float)Math.PI / 180F), entity);
			return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
		}
	}

	@Unique
	private boolean isLivingEntity() {
		return ((Entity) (Object) this) instanceof LivingEntity;
	}

	@Unique
	private LivingEntity get() {
		return ((Entity) (Object) this) instanceof LivingEntity livingEntity ? livingEntity : null;
	}
}