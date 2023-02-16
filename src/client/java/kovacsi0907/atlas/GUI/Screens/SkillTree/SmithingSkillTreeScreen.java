package kovacsi0907.atlas.GUI.Screens.SkillTree;

import kovacsi0907.atlas.Skills.Skills;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmithingSkillTreeScreen extends SkillTreeScreen {

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

        addButtonToGrid(1, 2, Skills.BRONZE_WEAPON_SMITHING, BRONZE_TEXTURE);
        addButtonToGrid(2, 1, Skills.SILVER_WEAPON_SMITHING, SILVER_TEXTURE);
        addButtonToGrid(2, 3, Skills.GOLD_WEAPON_SMITHING, GOLD_TEXTURE);
        addButtonToGrid(3, 2, Skills.IRON_WEAPON_SMITHING, IRON_TEXTURE);
        addButtonToGrid(4, 2, Skills.STEEL_WEAPON_SMITHING, STEEL_TEXTURE);
        addButtonToGrid(5, 2, Skills.NETHERITE_WEAPON_SMITHING, NETHERITE_TEXTURE);

        addConnectingLine(0,1);
        addConnectingLine(0,2);
        addConnectingLine(1,3);
        addConnectingLine(2,3);
        addConnectingLine(3,4);
        addConnectingLine(4,5);

        addButtonToGrid(1, 6, Skills.BRONZE_ARMOR_SMITHING, BRONZE_TEXTURE);
        addButtonToGrid(2, 5, Skills.SILVER_ARMOR_SMITHING, SILVER_TEXTURE);
        addButtonToGrid(2, 7, Skills.GOLD_ARMOR_SMITHING, GOLD_TEXTURE);
        addButtonToGrid(3, 6, Skills.IRON_ARMOR_SMITHING, IRON_TEXTURE);
        addButtonToGrid(4, 6, Skills.STEEL_ARMOR_SMITHING, STEEL_TEXTURE);
        addButtonToGrid(5, 6, Skills.NETHERITE_ARMOR_SMITHING, NETHERITE_TEXTURE);

        addConnectingLine(6,7);
        addConnectingLine(6,8);
        addConnectingLine(7,9);
        addConnectingLine(8,9);
        addConnectingLine(9,10);
        addConnectingLine(10,11);
    }
}
