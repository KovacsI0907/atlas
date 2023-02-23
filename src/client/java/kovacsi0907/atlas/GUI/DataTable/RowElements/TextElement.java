package kovacsi0907.atlas.GUI.DataTable.RowElements;

import kovacsi0907.atlas.GUI.DataTable.DataTable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextElement extends RowElement {
    boolean verticallyCentered;
    boolean horizontallyCentered;
    boolean drawBackground;
    int width;
    Text text;
    public TextElement(Text text, boolean verticallyCentered, boolean drawBackground) {
        this.width = 0;
        this.verticallyCentered = verticallyCentered;
        this.horizontallyCentered = false;
        this.text = text;
        this.drawBackground = drawBackground;
    }

    public TextElement(String text, boolean verticallyCentered, boolean drawBackground) {
        this.width = 0;
        this.verticallyCentered = verticallyCentered;
        this.horizontallyCentered = false;
        this.text = Text.literal(text);
        this.drawBackground = drawBackground;
    }

    public TextElement(Text text, int width, boolean verticallyCentered, boolean horizontallyCentered) {
        this.width = width;
        this.verticallyCentered = verticallyCentered;
        this.horizontallyCentered = horizontallyCentered;
        this.text = text;
    }
    @Override
    public void render(DataTable table, MatrixStack matrices, int x, int y, int height, int rowIndex, boolean selected, boolean mouseOver) {
        int textX = x;
        int textY = y;
        int textWidth = table.textRenderer.getWidth(this.text);
        if(this.width == 0)
            this.width = textWidth;
        int alteredBgColor = table.bgColor;

        if(selected)
            alteredBgColor = table.activeBgColor;

        if(verticallyCentered)
            if(height>table.textRenderer.fontHeight)
                textY += (height - table.textRenderer.fontHeight)/2;

        if(horizontallyCentered){
            if(width>textWidth)
                textX += (width-textWidth)/2;
        }

        if(drawBackground) {
            DrawableHelper.fill(matrices, x, y, x + textWidth, y + height, alteredBgColor);
            if(rowIndex%2==0 && !selected)
                DrawableHelper.fill(matrices, x, y, x + textWidth, y + height, 0x30000000);
            if(mouseOver && !selected)
                DrawableHelper.fill(matrices, x, y, x + textWidth, y + height, 0x30000000);
        }

        table.textRenderer.draw(matrices, this.text, textX, textY, table.textColor);
    }

    @Override
    public int getWidth() {
        return this.width;
    }
}
