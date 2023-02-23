package kovacsi0907.atlas.GUI;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextureButton extends ButtonWidget {
    Identifier texture;
    public TextureButton(int x, int y, Identifier texture, int textureWidth, int textureHeight, PressAction onPress) {
        super(x, y, textureWidth, textureHeight, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(this.hovered && this.active)
            DrawableHelper.fill(matrices, this.getX(), this.getY(), this.getX()+this.width, this.getY()+this.height, 0x30FFFFFF);
        Rendering.texture(matrices, this.getX(), this.getY(), this.width, this.height, this.texture);
    }
}
