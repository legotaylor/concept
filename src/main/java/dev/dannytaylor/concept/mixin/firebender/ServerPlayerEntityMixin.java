package dev.dannytaylor.concept.mixin.firebender;

import dev.dannytaylor.concept.common.registry.entity.FirebendingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Inject(at = @At("RETURN"), method = "copyFrom")
	private void concept$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		((FirebendingEntity)this).concept$setFireballLevel(((FirebendingEntity)oldPlayer).concept$getFireballLevel());
	}
}