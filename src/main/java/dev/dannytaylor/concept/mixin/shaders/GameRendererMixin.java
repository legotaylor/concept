package dev.dannytaylor.concept.mixin.shaders;

import dev.dannytaylor.concept.client.data.ClientData;
import dev.dannytaylor.concept.client.registry.pipeline.PostEffectRegistry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(at = @At("RETURN"), method = "close")
	private void dtaf2026$shaders_close(CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) postEffect.close();
	}

	@Inject(at = @At("RETURN"), method = "onResized")
	private void dtaf2026$shaders_onResized(int width, int height, CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) postEffect.onResized();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = At.Shift.AFTER), method = "render")
	private void dtaf2026$shaders_render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
		for (PostEffectRegistry.PostEffect postEffect : PostEffectRegistry.postEffects) postEffect.render(ClientData.getMinecraft());
	}
}
