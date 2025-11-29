package dev.dannytaylor.concept.mixin.staring_mannequins;

import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import dev.dannytaylor.concept.common.registry.entity.mannequin.MannequinProfiles;
import dev.dannytaylor.concept.common.registry.entity.mannequin.StaringEntity;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MannequinEntity.class)
public abstract class MannequinEntityMixin extends LivingEntity implements StaringEntity {
	@Shadow protected abstract void setMannequinProfile(ProfileComponent profile);
	@Shadow protected abstract void setHideDescription(boolean hideDescription);

	protected MannequinEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.concept$randomize();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.hasStatusEffect(StatusEffectRegistry.stare)) this.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.stare, -1, 0, true, false));
	}

	public void concept$randomize() {
		if (!MannequinProfiles.registry.isEmpty()) {
			String profile = MannequinProfiles.registry.get((int) (Math.abs(this.getUuid().getLeastSignificantBits()) % MannequinProfiles.registry.size()));
			this.setMannequinProfile(ProfileComponent.ofDynamic(profile));
			this.setCustomName(Text.literal(profile));
			this.setCustomNameVisible(true);
			this.setHideDescription(true);
		}
	}
}