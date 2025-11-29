package dev.dannytaylor.concept.client.gui;

import dev.dannytaylor.concept.client.data.ClientData;
import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.StatusEffectRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class PissAndDriveOverlay {
    private static final Identifier texture = Data.idOf("textures/gui/piss_and_drive.png");

    public static void render(DrawContext context, RenderTickCounter renderTickCounter) {
        if (isPissAndDriveOverlayActive(ClientData.getMinecraft())) context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, (context.getScaledWindowWidth() - 248) / 2, (context.getScaledWindowHeight() - 64) / 2, 0.0F, 0.0F, 248, 64, 256, 256);
    }

    public static boolean isPissAndDriveOverlayActive(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        return player != null && player.getControllingVehicle() != null && player.hasStatusEffect(StatusEffectRegistry.piss);
    }
}
