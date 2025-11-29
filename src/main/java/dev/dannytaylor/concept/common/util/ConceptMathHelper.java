package dev.dannytaylor.concept.common.util;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ConceptMathHelper {
    public static float sin(float value, Entity Entity) {
        return sin(value, hasPissEffect(Entity));
    }

    public static float cos(float value, Entity Entity) {
        return cos(value, hasPissEffect(Entity));
    }

    public static double cos(double value, Entity Entity) {
        return cos(value, hasPissEffect(Entity));
    }
    public static float sin(float value, boolean hasPissEffect) {
        return hasPissEffect ? MathHelper.cos(value) : MathHelper.sin(value);
    }

    public static float cos(float value, boolean hasPissEffect) {
        return hasPissEffect ? MathHelper.sin(value) : MathHelper.cos(value);
    }

    public static double cos(double value, boolean hasPissEffect) {
        return hasPissEffect ? Math.sin(value) : Math.cos(value);
    }

    public static float sin(float value) {
        return MathHelper.sin(value);
    }

    public static float cos(float value) {
        return MathHelper.cos(value);
    }

    public static Vec3d rotateX(Vec3d vec3d, float angle, Entity Entity) {
        return hasPissEffect(Entity) ? rotateX(vec3d, angle, true) : vec3d.rotateX(angle);
    }

    public static Vec3d rotateX(Vec3d vec3d, float angle, boolean hasPissEffect) {
        float f = cos(angle, hasPissEffect);
        float g = sin(angle, hasPissEffect);
        double d = vec3d.x;
        double e = vec3d.y * (double)f + vec3d.z * (double)g;
        double h = vec3d.z * (double)f - vec3d.y * (double)g;
        return new Vec3d(d, e, h);
    }

    public static Vec3d rotateY(Vec3d vec3d, float angle, Entity Entity) {
        return hasPissEffect(Entity) ? rotateY(vec3d, angle, true) : vec3d.rotateY(angle);
    }

    public static Vec3d rotateY(Vec3d vec3d, float angle, boolean hasPissEffect) {
        float f = cos(angle, hasPissEffect);
        float g = sin(angle, hasPissEffect);
        double d = vec3d.x * (double)f + vec3d.z * (double)g;
        double e = vec3d.y;
        double h = vec3d.z * (double)f - vec3d.x * (double)g;
        return new Vec3d(d, e, h);
    }

    public static Vec3d rotateZ(Vec3d vec3d, float angle, Entity Entity) {
        return hasPissEffect(Entity) ? rotateZ(vec3d, angle, true) : vec3d.rotateZ(angle);
    }

    public static Vec3d rotateZ(Vec3d vec3d, float angle, boolean hasPissEffect) {
        float f = cos(angle, hasPissEffect);
        float g = sin(angle, hasPissEffect);
        double d = vec3d.x * (double)f + vec3d.y * (double)g;
        double e = vec3d.y * (double)f - vec3d.x * (double)g;
        double h = vec3d.z;
        return new Vec3d(d, e, h);
    }

    public static boolean hasPissEffect(Entity entity) {
        return entity instanceof LivingEntity livingEntity && livingEntity.getStatusEffect(StatusEffectRegistry.piss) != null;
    }
}
