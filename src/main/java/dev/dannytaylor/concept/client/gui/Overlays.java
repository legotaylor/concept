package dev.dannytaylor.concept.client.gui;

import dev.dannytaylor.concept.common.data.Data;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

public class Overlays {
    public static void bootstrap() {
        HudElementRegistry.addFirst(Data.idOf("piss_and_drive"), PissAndDriveOverlay::render);
    }
}
