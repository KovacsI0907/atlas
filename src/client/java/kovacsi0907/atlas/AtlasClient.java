package kovacsi0907.atlas;

import net.fabricmc.api.ClientModInitializer;

public class AtlasClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyBindings.init();
	}
}