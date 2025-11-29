package dev.dannytaylor.concept.common.registry.entity.mannequin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dannytaylor.concept.common.data.Data;
import dev.dannytaylor.concept.common.registry.JsonResourceLoader;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.*;

public class MannequinProfiles extends JsonResourceLoader {
	public static List<String> registry;

	public MannequinProfiles() {
		super(new Gson(), "mannequin_profiles");
	}

	public static void bootstrap() {
		ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(Data.idOf("mannequin_profiles"), new MannequinProfiles());
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		registry.clear();
		prepared.forEach((identifier, jsonElement) -> {
			try {
				JsonObject reader = jsonElement.getAsJsonObject();
				if (JsonHelper.hasString(reader, "username")) {
					String username = JsonHelper.getString(reader, "username");
					Data.getLogger().info("Registering: {}", username);
					registry.add(username);
				}
			} catch (Exception error) {
                Data.getLogger().error("Failed to load mannequin profile: {}", error.getLocalizedMessage());
			}
		});
	}

	static {
		registry = new ArrayList<>();
	}
}
