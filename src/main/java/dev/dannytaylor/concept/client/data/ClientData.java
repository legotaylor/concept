package dev.dannytaylor.concept.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static MinecraftClient getMinecraft() {
		return MinecraftClient.getInstance();
	}
}
