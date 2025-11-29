package dev.dannytaylor.concept.mixin.piss;

import net.minecraft.client.render.item.tint.PotionTintSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PotionTintSource.class)
public abstract class PotionTintSourceMixin {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/tint/PotionTintSource;<init>(I)V"), method = "<init>()V")
	private static int concept$this(int par1) {
		return 0xffd33e;
	}
}