package kovacsi0907.atlas;

import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import kovacsi0907.atlas.Network.ClientNetworkReceiver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import java.util.ArrayList;
import java.util.List;

public class AtlasClient implements ClientModInitializer {

	public static List<String> skills = new ArrayList<>();
	public static List<Experience> experienceList = new ArrayList<>();
	public static List<Experience> overallExperienceList = new ArrayList<>();
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyBindings.init();
		ClientNetworkReceiver.registerListeners();

		ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
			ClientNetworkFunctions.syncSkills();
			ClientNetworkFunctions.syncXP();
		}));
	}
}