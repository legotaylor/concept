package dev.dannytaylor.concept.common.data;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Data {
	private static final String modId;
	private static final Logger logger;

	public static Identifier idOf(String path) {
		return Identifier.of(getModId(), path);
	}

	public static String getModId() {
		return modId;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static boolean isModInstalled(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	static {
		modId = "concept";
		logger = LoggerFactory.getLogger(getModId());
	}
}
