package dev.dannytaylor.concept.client.registry.pipeline;

import dev.dannytaylor.concept.client.gui.PissAndDriveOverlay;
import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.util.Pool;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PostEffectRegistry {
	public static List<PostEffect> postEffects = new ArrayList<>();

	public static void bootstrap() {
		postEffects.add(new PostEffect(Data.idOf("piss_and_drive"), PissAndDriveOverlay::isPissAndDriveOverlayActive));
		postEffects.add(new PostEffect(Data.idOf("light_headed"), (client) -> client.player != null && client.player.hasStatusEffect(StatusEffectRegistry.lightHeaded)));
	}

	public static class PostEffect {
		private final Pool pool;
		private final Identifier id;
		private final ShouldRender shouldRender;

		public PostEffect(Identifier id, ShouldRender shouldRender) {
			this.pool = new Pool(3);
			this.id = id;
			this.shouldRender = shouldRender;
		}

		@SuppressWarnings("deprecation")
		public void render(MinecraftClient client) {
			try {
				if (this.shouldRender.shouldRender(client)) {
					PostEffectProcessor processor = client.getShaderLoader().loadPostEffect(this.id, DefaultFramebufferSet.MAIN_ONLY);
					if (processor != null) processor.render(client.getFramebuffer(), this.pool);
				}
			} catch (Exception error) {
				throw new RuntimeException(error);
			}
		}

		public void close() {
			this.pool.close();
		}

		public void onResized() {
			this.pool.clear();
		}

		public interface ShouldRender {
			boolean shouldRender(MinecraftClient client);
		}
	}
}
