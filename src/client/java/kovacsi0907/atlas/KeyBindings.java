package kovacsi0907.atlas;

import kovacsi0907.atlas.GUI.Screens.Shop.BuyScreen;
import kovacsi0907.atlas.GUI.Screens.Shop.SellScreen;
import kovacsi0907.atlas.GUI.Screens.SkillTree.SmithingSkillTreeScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

final class KeyBindings {
    private static final KeyBinding TESTKEY_H = new KeyBinding("key.atlas.test_h", GLFW.GLFW_KEY_H, "key.categories.misc");
    private static final KeyBinding TESTKEY_J = new KeyBinding("key.atlas.test_j", GLFW.GLFW_KEY_J, "key.categories.misc");
    private static final KeyBinding TESTKEY_K = new KeyBinding("key.atlas.test_k", GLFW.GLFW_KEY_K, "key.categories.misc");
    private static void registerTestKeyH(){
        KeyBindingHelper.registerKeyBinding(TESTKEY_H);
        KeyBindingHelper.registerKeyBinding(TESTKEY_J);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(TESTKEY_H.wasPressed()){
                client.setScreen(new SmithingSkillTreeScreen(Text.of("Smithing")));
            }
            while (TESTKEY_J.wasPressed()){
                client.setScreen(new SellScreen(Text.literal("Shop"), "test_vendor", MinecraftClient.getInstance().player.getInventory()));
            }
            while(TESTKEY_K.wasPressed()){
                client.setScreen(new BuyScreen(Text.literal("Shop"), "test_vendor", MinecraftClient.getInstance().player.getInventory()));
            }
        });
    }

    static void init() {
        registerTestKeyH();
    }
}
