package kovacsi0907.atlas.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.Atlas;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PapyrusButton extends ButtonWidget {
    List<Integer> papyrusPieces = new ArrayList<>();
    int opened = 0;

    Identifier[] TEXTURES = new Identifier[]{
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_1.png"),
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_2.png"),
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_3.png")
    };
    Identifier[] GRAY_TEXTURES = new Identifier[]{
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_1_gray.png"),
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_2_gray.png"),
            new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_3_gray.png")
    };
    Identifier LEFT_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_left.png");
    Identifier LEFT_TEXTURE_GRAY = new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_left_gray.png");
    Identifier RIGHT_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_right.png");
    Identifier RIGHT_TEXTURE_GRAY = new Identifier(Atlas.MOD_ID, "textures/gui/paper_button_right_gray.png");

    public PapyrusButton(int centerX, int centerY, Text message, PressAction onPress) {
        super(centerX, centerY, 22, 20, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        this.hovered = mouseX > this.getX()-this.width/2
                && mouseX < this.getX()+this.width/2
                && mouseY > this.getY()-this.height/2
                && mouseY < this.getY()+this.height/2;

        if(this.hovered && this.opened < 5)
            this.opened++;
        if(!this.hovered && this.opened > 0)
            this.opened--;
        if(!this.active){
            opened = 0;
        }

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(this.getMessage());
        this.width = textWidth + 22 + opened*2;
        this.height = 20;
        int wholePiecesNeeded = (textWidth)/10+1;

        while(papyrusPieces.size()<wholePiecesNeeded+2){
            double rand = Math.random();
            if(rand<0.5)
                papyrusPieces.add(0);
            else if(rand<0.8)
                papyrusPieces.add(2);
            else
                papyrusPieces.add(1);
        }

        if(!this.visible)
            return;

        int x = this.getX()-this.width/2;
        int y = this.getY()-this.height/2;

        Rendering.texture(matrices, x, y-10, 11, 30, this.active?LEFT_TEXTURE:LEFT_TEXTURE_GRAY);
        x+=11;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, this.active?TEXTURES[papyrusPieces.get(wholePiecesNeeded)]:GRAY_TEXTURES[papyrusPieces.get(wholePiecesNeeded)]);
        DrawableHelper.drawTexture(matrices, x, y, 10-opened, 0, opened, 20, 10, 20);
        x+=opened;

        for(int i = 0;i<wholePiecesNeeded;i++){
            Rendering.texture(matrices, x, y, 10, 20, this.active?TEXTURES[papyrusPieces.get(i)]:GRAY_TEXTURES[papyrusPieces.get(i)]);
            x+=10;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, this.active?TEXTURES[papyrusPieces.get(wholePiecesNeeded+1)]:GRAY_TEXTURES[papyrusPieces.get(wholePiecesNeeded+1)]);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, opened, 20, 10, 20);
        x+=opened;

        Rendering.texture(matrices, x, y, 11, 30, this.active?RIGHT_TEXTURE:RIGHT_TEXTURE_GRAY);

        x = this.getX()-this.width/2 + (this.width-textWidth)/2 + 5;
        y = this.getY()-this.height/2 + (this.height-textRenderer.fontHeight)/2;
        Rendering.text(matrices, this.getMessage(), x,y, 0xFF000000, 0x0);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.opened = 7;
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return this.visible && this.active && this.hovered;
    }
}
