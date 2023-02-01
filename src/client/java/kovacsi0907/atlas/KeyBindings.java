package kovacsi0907.atlas;

import kovacsi0907.atlas.GUI.SmithingSkillTreeScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

final class KeyBindings {
    private static final KeyBinding TESTKEY_H = new KeyBinding("key.atlas.test_h", GLFW.GLFW_KEY_H, "key.categories.misc");
    private static void registerTestKeyH(){
        KeyBindingHelper.registerKeyBinding(TESTKEY_H);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(TESTKEY_H.wasPressed()){
                client.setScreen(new SmithingSkillTreeScreen(Text.of("Smithing")));
            }
        });
    }

    static void init() {
        registerTestKeyH();
    }
}
