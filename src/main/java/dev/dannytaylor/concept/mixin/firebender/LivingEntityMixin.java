package dev.dannytaylor.concept.mixin.firebender;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import dev.dannytaylor.concept.common.registry.entity.FirebendingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements FirebendingEntity {
	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow @Nullable private LivingEntity attacking;
	@Unique private LivingEntity preventAttackFireball;
	@Unique private static final TrackedData<Integer> fireballLevel;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void concept$tick(CallbackInfo ci) {
		if (this.attacking != null) {
			if (this.preventAttackFireball != this.attacking) {
				tryFireball();
				this.preventAttackFireball = this.attacking;
			}
		} else if (this.preventAttackFireball != null) {
			this.preventAttackFireball = null;
		}
	}

	@Inject(at = @At("RETURN"), method = "initDataTracker")
	private void concept$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(fireballLevel, 0);
	}

	@Inject(at = @At("RETURN"), method = "writeCustomData")
	private void concept$writeCustomData(WriteView view, CallbackInfo ci) {
		view.putFloat("ConceptFireballLevel", this.concept$getFireballLevel());
	}

	@Inject(at = @At("RETURN"), method = "readCustomData")
	private void concept$readCustomData(ReadView view, CallbackInfo ci) {
		this.concept$setFireballLevel(view.getInt("ConceptFireballLevel", 0));
	}

	@Unique
	private void tryFireball(LivingEntity target, int chance) {
		if (concept$shouldFireball(chance)) {
			this.concept$tryDecreaseFireball();
			Vec3d velocity = concept$getVelocity(target);
			World world = this.getEntityWorld();
			FireballEntity fireChargeEntity = new FireballEntity(world, (LivingEntity) (Object) this, velocity, 1);
			fireChargeEntity.setVelocity(velocity);
			world.spawnEntity(fireChargeEntity);
		}
		else this.concept$tryIncreaseFireball();
	}

	@Unique
	private void tryFireball() {
		tryFireball(this.attacking, 256);
	}

	@Unique
	private void concept$tryIncreaseFireball() {
		this.concept$setFireballLevel(this.concept$getFireballLevel() + 1);
	}

	@Unique
	private void concept$tryDecreaseFireball() {
		this.concept$setFireballLevel(this.concept$getFireballLevel() - 1);
	}

	@Unique
	private boolean concept$shouldFireball(int chance) {
		if (this.hasStatusEffect(StatusEffectRegistry.fireBender)) {
			if (this.hasStatusEffect(StatusEffectRegistry.fireBender)) {
				int bound = Math.max(chance / (this.concept$getFireballLevel() + 1), 0);
				return bound == 0 || this.random.nextInt(bound) == 0;
			}
		}
		return false;
	}

	@Unique
	private Vec3d concept$getVelocity(LivingEntity livingEntity) {
		if (livingEntity != null) {
			return new Vec3d(
					livingEntity.getX() - this.getX(),
					livingEntity.getBodyY(0.5F) - this.getBodyY(0.5F),
					livingEntity.getZ() - this.getZ()
			);
		} else {
			Direction direction = this.getFacing();
			return new Vec3d(
					this.random.nextTriangular(direction.getOffsetX(), 0.11485000000000001),
					this.random.nextTriangular(direction.getOffsetY(), 0.11485000000000001),
					this.random.nextTriangular(direction.getOffsetZ(), 0.11485000000000001)
			);
		}
	}

	public int concept$getFireballLevel() {
		return Math.clamp(this.dataTracker.get(fireballLevel), 0, 255);
	}

	public void concept$setFireballLevel(int level) {
		this.dataTracker.set(fireballLevel, Math.clamp(level, 0, 255));
	}

	static {
		fireballLevel = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
	}
}