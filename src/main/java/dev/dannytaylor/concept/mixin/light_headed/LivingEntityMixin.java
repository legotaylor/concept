package dev.dannytaylor.concept.mixin.light_headed;

import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import dev.dannytaylor.concept.common.util.ShouldApply;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique private static final EntityAttributeModifier lightHeadedAttributeModifier;
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void concept$tick(CallbackInfo ci) {
		if (this.getBlockStateAtPos().isIn(TagKey.of(RegistryKeys.BLOCK, Data.idOf("light_headed")))) {
			this.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.lightHeaded, 1200));
		}
		this.concept$updateStatusEffects();
	}

	@Unique
	private void concept$updateStatusEffects() {
		this.concept$updateAttribute(EntityAttributes.GRAVITY, lightHeadedAttributeModifier, StatusEffectRegistry.lightHeaded);
	}

	@Unique
	public void concept$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, ShouldApply shouldApply) {
		EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(attribute);
		if (entityAttributeInstance != null) {
			entityAttributeInstance.removeModifier(modifier.id());
			if (shouldApply.get()) entityAttributeInstance.addTemporaryModifier(modifier);
		}
	}

	@Unique
	public void concept$updateAttribute(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, RegistryEntry<StatusEffect> statusEffect) {
		concept$updateAttribute(attribute, modifier, () -> this.hasStatusEffect(statusEffect));
	}

	static {
		lightHeadedAttributeModifier = new EntityAttributeModifier(Data.idOf("light_headed"), 0.5F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}
}