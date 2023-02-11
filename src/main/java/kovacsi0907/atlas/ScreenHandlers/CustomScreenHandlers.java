package kovacsi0907.atlas.ScreenHandlers;

import kovacsi0907.atlas.Atlas;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class CustomScreenHandlers {
    public static ScreenHandlerType<SmithingStationScreenHandler> SMITHING_STATION_SCREEN_HANDLER;

    public static void regiterScreenHandlers(){
        SMITHING_STATION_SCREEN_HANDLER = new ScreenHandlerType<>(SmithingStationScreenHandler::new);
    }
}
