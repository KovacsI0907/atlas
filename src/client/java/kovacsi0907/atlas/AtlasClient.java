package kovacsi0907.atlas;

import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.GUI.Screens.CustomScreens;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import kovacsi0907.atlas.Network.ClientNetworkReceiver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.item.Items;

public class AtlasClient implements ClientModInitializer {

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyBindings.init();
		ClientNetworkReceiver.registerListeners();
		CustomScreens.registerHandledScreens();
		ClientData.wareStacks.add(new WareStack("sada", "test", Items.ACACIA_BUTTON, 10, 10, 10, 10, null));

		ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
			ClientNetworkFunctions.syncSkills();
			ClientNetworkFunctions.syncXP();
		}));
	}
}