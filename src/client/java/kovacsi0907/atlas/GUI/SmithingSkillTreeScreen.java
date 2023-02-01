package kovacsi0907.atlas.GUI;

import kovacsi0907.atlas.Skills.Skills;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmithingSkillTreeScreen extends SkillTreeScreen{

    final Identifier BRONZE_TEXTURE = new Identifier("atlas:textures/gui/skills/bronze_smithing.png");
    final Identifier IRON_TEXTURE = new Identifier("atlas:textures/gui/skills/iron_smithing.png");
    final Identifier SILVER_TEXTURE = new Identifier("atlas:textures/gui/skills/silver_smithing.png");
    final Identifier GOLD_TEXTURE = new Identifier("atlas:textures/gui/skills/gold_smithing.png");
    final Identifier STEEL_TEXTURE = new Identifier("atlas:textures/gui/skills/steel_smithing.png");
    final Identifier NETHERITE_TEXTURE = new Identifier("atlas:textures/gui/skills/netherite_smithing.png");
    final Identifier GOLD_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/gold_selector.png");
    final Identifier GREEN_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/green_selector.png");

    public SmithingSkillTreeScreen(Text title) {
        super(title);
    }

    @Override
    void createScreen() {
        setGrid(5, 7);
        addButtonToGrid(1,2, Skills.BRONZE_WEAPON_SMITHING, BRONZE_TEXTURE);
        addButtonToGrid(2,2, Skills.IRON_WEAPON_SMITHING, IRON_TEXTURE);
        addButtonToGrid(3,1, Skills.SILVER_WEAPON_SMITHING, SILVER_TEXTURE);
        addButtonToGrid(3,3, Skills.GOLD_WEAPON_SMITHING, GOLD_TEXTURE);
        addConnectingLine(0,1);
        addConnectingLine(1,2);
        addConnectingLine(1,3);
    }
}
