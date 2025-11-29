package dev.dannytaylor.concept.mixin.piss;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	// All biomes are piss coloured (thanks dark...)

	@Inject(at = @At("HEAD"), method = "getWaterColor", cancellable = true)
	private static void concept$getWaterColor(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(0xffd33e);
	}

	@Inject(at = @At("HEAD"), method = "getWaterFogColor", cancellable = true)
	private static void concept$getWaterFogColor(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(0xffd33e);
	}
}