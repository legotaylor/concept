package dev.dannytaylor.concept.mixin.staring_mannequins;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import dev.dannytaylor.concept.common.registry.entity.mannequin.StaringEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow public abstract float getYaw(float tickProgress);
	@Shadow public abstract boolean canSee(Entity entity);
	@Shadow public abstract void setHeadYaw(float headYaw);
	@Shadow public abstract float getHeadYaw();
	@Shadow public abstract boolean isEntityLookingAtMe(LivingEntity entity, double d, boolean bl, boolean visualShape, double... checkedYs);
	@Shadow public abstract boolean teleport(double x, double y, double z, boolean particleEffects);

	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Inject(at = @At("RETURN"), method = "tick")
	private void concept$tick(CallbackInfo ci) {
		if (this.hasStatusEffect(StatusEffectRegistry.stare)) {
			if (this.isAlive()) {
				PlayerEntity playerTarget = null;
				double closestDistanceSq = Double.MAX_VALUE;

				for (PlayerEntity player : this.getEntityWorld().getPlayers()) {
					if (this.canSee(player) && !player.isInCreativeMode() && !player.isSpectator()) {
						double distanceSq = this.squaredDistanceTo(player);

						if (distanceSq < closestDistanceSq && distanceSq <= 32 * 32) {
							closestDistanceSq = distanceSq;
							playerTarget = player;
						}
					}
				}

				if (playerTarget != null) {
					Vec3d targetEyes = playerTarget.getEyePos();
					Vec3d selfEyes = this.getEyePos();
					double x = targetEyes.x - selfEyes.x;
					double y = targetEyes.y - selfEyes.y;
					double z = targetEyes.z - selfEyes.z;
					double distance = Math.sqrt(x*x + y*y + z*z);
					float minDelta = 0.125F;
					float maxDelta = 0.25F;
					float deltaFactor = 1.0F - MathHelper.clamp(minDelta + (float) (distance / 64.0F) * 0.25F, minDelta, maxDelta);
					this.setYaw(MathHelper.lerpAngleDegrees(deltaFactor, this.getYaw(), (float)(MathHelper.atan2(z, x) * (180.0F / Math.PI)) - 90.0F));
					this.setPitch(MathHelper.lerpAngleDegrees(deltaFactor, this.getPitch(), (float)(-(MathHelper.atan2(y, Math.sqrt(x*x + z*z)) * (180.0F / Math.PI)))));
					this.setHeadYaw(MathHelper.lerpAngleDegrees(deltaFactor, this.getHeadYaw(), this.getYaw()));
					if (Math.abs(MathHelper.wrapDegrees(this.getHeadYaw() - this.getBodyYaw())) > 45.0F) this.setBodyYaw(MathHelper.lerpAngleDegrees(deltaFactor, this.getBodyYaw(), this.getYaw()));

					if (this.isEntityLookingAtMe(playerTarget, 0.1, true, true, this.getEyeY()) || this.squaredDistanceTo(playerTarget) < 64 * 64) teleportRandomly();
				}
			}
		}
	}

	@Inject(method = "applyDamage", at = @At("RETURN"))
	private void concept$applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		if (this.hasStatusEffect(StatusEffectRegistry.stare)) teleportRandomly();
	}

	@Unique
	private void teleportRandomly() {
		this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20, 0, true, false));
		if (((LivingEntity) (Object) this) instanceof StaringEntity staringEntity) staringEntity.concept$randomize();
		if (!this.getEntityWorld().isClient() && this.isAlive()) {
			this.teleportTo(
					this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)64.0F,
					this.getY() + (double)(this.random.nextInt(64) - 32),
					this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)64.0F
			);
		}
	}

	@Unique
	private void teleportTo(double x, double y, double z) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
		while (mutable.getY() > this.getEntityWorld().getBottomY() && !this.getEntityWorld().getBlockState(mutable).blocksMovement()) mutable.move(Direction.DOWN);
		if (this.teleport(mutable.getX() + 0.5, mutable.getY(), mutable.getZ() + 0.5, false)) this.getEntityWorld().emitGameEvent(GameEvent.TELEPORT, this.getEntityPos(), GameEvent.Emitter.of(this));
	}
}