package kovacsi0907.atlas;
import kovacsi0907.atlas.GUI.Rendering;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmithingSkillTreeScreen extends Screen {

    private final Identifier BRONZE_TEXTURE = new Identifier("atlas:textures/gui/skills/bronze_smithing.png");
    private final Identifier IRON_TEXTURE = new Identifier("atlas:textures/gui/skills/iron_smithing.png");
    private final Identifier SILVER_TEXTURE = new Identifier("atlas:textures/gui/skills/silver_smithing.png");
    private final Identifier GOLD_TEXTURE = new Identifier("atlas:textures/gui/skills/gold_smithing.png");
    private final Identifier STEEL_TEXTURE = new Identifier("atlas:textures/gui/skills/steel_smithing.png");
    private final Identifier NETHERITE_TEXTURE = new Identifier("atlas:textures/gui/skills/netherite_smithing.png");
    private final Identifier GOLD_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/gold_selector.png");
    private final Identifier GREEN_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/green_selector.png");
    private final int HALF_TEXTURE_SIZE = 16;
    private final int TEXTURE_SIZE = 2 * HALF_TEXTURE_SIZE;

    public SmithingSkillTreeScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        addSkillTreeButtons(0, 0, this.width,this.height/10f*75);
        //this.addDrawableChild(new TexturedButtonWidget(10, 10, 32, 32, 0, 0, 0, new Identifier("atlas:textures/gui/skills/smithing_bronze.png"), 32, 32, (button -> {})));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void addSkillTreeButtons(int x, int y, double w, double h) {
        //first row
        int yPos = (int)(h/7) * 2 - HALF_TEXTURE_SIZE;
        int xPos = (int)(w/5) - HALF_TEXTURE_SIZE;
        addSkillButton(xPos, yPos, BRONZE_TEXTURE, (button -> {}));
        xPos = (int)(w/5) * 2 - HALF_TEXTURE_SIZE;
        addSkillButton(xPos, yPos, IRON_TEXTURE, (button -> {}));
        xPos = (int)(w/5) * 3 - HALF_TEXTURE_SIZE;
        yPos = (int)(h/7) - HALF_TEXTURE_SIZE;
        addSkillButton(xPos, yPos, SILVER_TEXTURE, (button -> {}));
        yPos = (int)(h/7)*3 -HALF_TEXTURE_SIZE;
        addSkillButton(xPos, yPos, GOLD_TEXTURE, (button -> {}));
        yPos = (int)(h/7) * 2 - HALF_TEXTURE_SIZE;
        xPos = (int)(w/5)*4;
        addSkillButton(xPos, yPos, STEEL_TEXTURE, (button -> {}));
        xPos = (int)(w/5)*5;
        addSkillButton(xPos, yPos, NETHERITE_TEXTURE, (button -> {}));
    }

    private void addSkillButton(int x, int y, Identifier texture,  ButtonWidget.PressAction pressAction){
        this.addDrawableChild(new TexturedButtonWidget(
                x, y,
                TEXTURE_SIZE, TEXTURE_SIZE,
                0, 0,
                0,
                texture,
                TEXTURE_SIZE, TEXTURE_SIZE,
                pressAction));
    }
}
