package dev.dannytaylor.concept.mixin.reversed_boats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBoatEntity.class)
public abstract class BoatEntityMixin extends Entity {
    @Shadow private BoatEntity.Location location;
    @Shadow private BoatEntity.Location lastLocation;
    @Shadow private double waterLevel;
    @Shadow private float yawVelocity;
    @Shadow private double fallVelocity;
    @Shadow private float nearbySlipperiness;
    @Shadow public abstract float getWaterHeightBelow();

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void updateVelocity(CallbackInfo ci) {
        double d = -this.getFinalGravity();
        double e = 0.0;
        float f = 0.05F;
        if (this.lastLocation == BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.ON_LAND) {
            this.waterLevel = this.getBodyY(1.0);
            double g = (double)(this.getWaterHeightBelow() - this.getHeight()) + 0.101;
            if (this.getEntityWorld().isSpaceEmpty(this, this.getBoundingBox().offset(0.0, g - this.getY(), 0.0))) {
                this.setPosition(this.getX(), g, this.getZ());
                this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
                this.fallVelocity = 0.0;
            }
            this.location = BoatEntity.Location.IN_WATER;
        } else {
            if (this.location == BoatEntity.Location.IN_WATER) {
                e = (this.waterLevel - this.getY()) / (double)this.getHeight();
                f = 0.6F;
            } else if (this.location == BoatEntity.Location.UNDER_FLOWING_WATER) {
                d = -7.0E-4;
                f = 0.6F;
            } else if (this.location == BoatEntity.Location.UNDER_WATER) {
                e = 0.009999999776482582;
                f = 0.45F;
            } else if (this.location == BoatEntity.Location.IN_AIR) {
                f = 0.9F;
            } else if (this.location == BoatEntity.Location.ON_LAND) {
                f = Math.max(this.nearbySlipperiness, 0.9F);
                if (this.getControllingPassenger() instanceof PlayerEntity) {
                    this.nearbySlipperiness /= 2.0F;
                }
            }

            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x * (double)f, vec3d.y + d, vec3d.z * (double)f);
            this.yawVelocity *= f;
            if (e > 0.0) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, (vec3d2.y + e * (this.getGravity() / 0.65)) * 0.75, vec3d2.z);
            }
        }
        ci.cancel();
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    @Inject(method = "getMaxPassengers", at = @At("HEAD"), cancellable = true)
    private void maxPassengers(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(3);
    }
}