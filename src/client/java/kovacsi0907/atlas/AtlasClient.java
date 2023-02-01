package kovacsi0907.atlas;

import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import kovacsi0907.atlas.Network.ClientNetworkReciever;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import java.util.ArrayList;
import java.util.List;

public class AtlasClient implements ClientModInitializer {

	public static List<String> skills = new ArrayList<>();
	public static List<Experience> experienceList = new ArrayList<>();
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyBindings.init();
		ClientNetworkReciever.registerListeners();

		ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
			ClientNetworkFunctions.syncSkills();
			ClientNetworkFunctions.syncXP();
		}));
	}
}