package kovacsi0907.atlas.GUI;

import kovacsi0907.atlas.AtlasClient;
import kovacsi0907.atlas.Experience;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import kovacsi0907.atlas.Skills.ExpType;
import kovacsi0907.atlas.Skills.Skill;
import kovacsi0907.atlas.Skills.Skills;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillTreeScreen extends Screen {

    final Identifier GOLD_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/gold_selector.png");
    final Identifier GREEN_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/green_selector.png");
    final int HALF_TEXTURE_SIZE = 16;
    final double SPACE_FOR_TREE = 0.7f;
    final int PIXELS_FOR_UNLOCK_BUTTON = 30;
    final double TEXT_BOX_MARGIN = 0.2f;
    private int columns = 1;
    public int rows = 1;
    final int ACTIVE_LINE_COLOR = 0xFF30B030;
    final int LINE_COLOR = 0xFF808080;
    final int TEXT_COLOR = 0xFFD0D0D0;
    final int BACKGROUND_COLOR = 0xA0000000;
    final int LINE_THICKNESS = 2;

    List<SkillButtonWidget> skillButtons = new ArrayList<>();
    SkillButtonWidget activeButton = null;
    List<Pos2> buttonPositions = new ArrayList<>();
    List<Pos2> connections = new ArrayList<>();

    ButtonWidget unlockButton = ButtonWidget.builder(Text.translatable("skills.unlock_button"), (button -> {
        if(activeButton != null && !hasSkill(activeButton.skill) && getXPNeeded(activeButton.skill.expType, activeButton.skill.xpRequired) == 0){
            ClientNetworkFunctions.requestGetSkill(activeButton.skill.id);
        }
    })).size(60, 20).build();
    protected SkillTreeScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ClientNetworkFunctions.syncSkills();
        ClientNetworkFunctions.syncXP();
        super.init();
        this.addDrawableChild(unlockButton);
        unlockButton.setPos((int)(this.width/2.0-30), (int)(this.height*SPACE_FOR_TREE + 5));
        createScreen();
    }

    void setGrid(int columns, int rows){
        this.columns = columns;
        this.rows = rows;
    }

    void addButtonToGrid(int x, int y, Skill skill, Identifier texture){
        SkillButtonWidget button = new SkillButtonWidget(
                gridX(x),
                gridY(y),
                2*HALF_TEXTURE_SIZE,
                2*HALF_TEXTURE_SIZE,
                0,
                0,
                0,
                texture,
                2*HALF_TEXTURE_SIZE,
                2*HALF_TEXTURE_SIZE,
                skill,
                (skillButtonWidget -> {
                    this.activeButton = skillButtonWidget;
                })
        );
        this.addDrawableChild(button);
        this.skillButtons.add(button);
        this.buttonPositions.add(new Pos2(gridX(x), gridY(y)));
    }

    void addConnectingLine(int buttonAIndex, int buttonBIndex){
        connections.add(new Pos2(buttonAIndex, buttonBIndex));
    }

    void createScreen(){
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        unlockButton.visible = activeButton!=null;
        if(activeButton != null)
            unlockButton.active = (!hasSkill(activeButton.skill))&&(getXPNeeded(activeButton.skill.expType ,activeButton.skill.xpRequired) == 0);
        fill(matrices, 0, 0, this.width, this.height, BACKGROUND_COLOR);
        drawConnectingLines(connections, matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawSelectors(matrices);
        if(this.activeButton != null){
            Skill skill = this.activeButton.skill;
            Rendering.textBox(
                    matrices,
                    new Text[]{
                            skill.name,
                            Text.translatable("skills.experience_needed", skill.xpRequired, getXPNeeded(activeButton.skill.expType ,skill.xpRequired)),
                            skill.description
                    },
                    (int)(this.width*TEXT_BOX_MARGIN),
                    (int)(this.height*SPACE_FOR_TREE+PIXELS_FOR_UNLOCK_BUTTON),
                    (int)(this.width*(1-2*TEXT_BOX_MARGIN)),
                    (int)(this.height*(1-SPACE_FOR_TREE)-PIXELS_FOR_UNLOCK_BUTTON),
                    TEXT_COLOR, BACKGROUND_COLOR, TEXT_COLOR);
        }
    }

    void drawConnectingLines(List<Pos2> connections, MatrixStack matrices){
        for(Pos2 connection : connections){
            Rendering.connectingLine(matrices, center(buttonPositions.get(connection.x)), center(buttonPositions.get(connection.y)), LINE_THICKNESS, hasSkill(skillButtons.get(connection.x).skill)?ACTIVE_LINE_COLOR:LINE_COLOR);
        }
    }

    void drawSelectors(MatrixStack matrices){
        //unlocked
        for(SkillButtonWidget button : skillButtons){
            if(hasSkill(button.skill))
                Rendering.texture(matrices, button.getX(), button.getY(), 2*HALF_TEXTURE_SIZE, 2*HALF_TEXTURE_SIZE, GREEN_SELECTOR_TEXTURE);
        }
        //selected
        if(activeButton != null)
            Rendering.texture(matrices, activeButton.getX(), activeButton.getY(), 2*HALF_TEXTURE_SIZE, 2*HALF_TEXTURE_SIZE, GOLD_SELECTOR_TEXTURE);
    }

    Pos2 center(Pos2 a){
        return new Pos2(a.x+HALF_TEXTURE_SIZE, a.y+HALF_TEXTURE_SIZE);
    }

    boolean hasSkill(Skill skill){
        return AtlasClient.skills.contains(skill.id);
    }

    SkillButtonWidget createButton(Pos2 pos, Identifier texture, Skill skill) {
        int size = 2 * HALF_TEXTURE_SIZE;
        return new SkillButtonWidget(pos.x, pos.y, size, size, 0,0, 0, texture, size, size, skill, (skillButtonWidget -> {
            this.activeButton = skillButtonWidget;
        }));
    }

    int gridX(int xPos){
        return (int)((this.width-HALF_TEXTURE_SIZE)/(columns+1.0)*xPos) - HALF_TEXTURE_SIZE;
    }

    int gridY(int yPos){
        double y = this.height * SPACE_FOR_TREE - HALF_TEXTURE_SIZE;
        y /= this.rows;
        y *= yPos;
        y -= HALF_TEXTURE_SIZE;
        return (int)y;
    }

    int getXPNeeded(ExpType type, int needed){
        for(Experience exp : AtlasClient.experienceList){
            if(exp.type == type)
                if(needed-exp.points>0)
                    return needed-exp.points;
                else return 0;
        }
        return needed;
    }


}
