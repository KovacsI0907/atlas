package kovacsi0907.atlas.GUI.Screens;

import kovacsi0907.atlas.ScreenHandlers.CustomScreenHandlers;
import kovacsi0907.atlas.ScreenHandlers.SmithingStationScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CustomScreens {
    public static void registerHandledScreens() {
        HandledScreens.register(CustomScreenHandlers.SMITHING_STATION_SCREEN_HANDLER, SmithingStationScreen::new);
    }
}
