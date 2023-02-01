package kovacsi0907.atlas.GUI;

import kovacsi0907.atlas.Skills.Skill;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkillButtonWidget extends TexturedButtonWidget {
    Skill skill;
    PressFunction pressFunction;
    public SkillButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, Skill skill, PressFunction pressFunction) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, (button -> {}));
        this.pressFunction = pressFunction;
        this.skill = skill;
    }

    @Override
    public void onPress() {
        pressFunction.onPress(this);
    }

    interface PressFunction {
        void onPress(SkillButtonWidget skillButtonWidget);
    }
}