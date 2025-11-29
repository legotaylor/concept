package dev.dannytaylor.concept.mixin.staring_mannequins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
	@Unique private int concept$cooldown = 100;

	public PlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void concept$tick(CallbackInfo ci) {
		if (concept$cooldown > 0) concept$cooldown--;
		else {
			World world = getEntityWorld();
			if (world.getEntitiesByType(EntityType.MANNEQUIN, this.getBoundingBox().expand(128), e -> true).isEmpty()) {
				double x = this.getX() + (world.random.nextDouble() * 64 - 32);
				double z = this.getZ() + (world.random.nextDouble() * 64 - 32);
				double y = world.getTopY(Heightmap.Type.WORLD_SURFACE, (int)x, (int)z);
				MannequinEntity mannequinEntity = EntityType.MANNEQUIN.create(world, SpawnReason.TRIGGERED);
				if (mannequinEntity != null) {
					mannequinEntity.refreshPositionAfterTeleport(x, y, z);
					world.spawnEntity(mannequinEntity);
				}
			}
			concept$cooldown = 100;
		}
	}
}