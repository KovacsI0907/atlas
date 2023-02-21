package kovacsi0907.atlas.GUI.DataTable;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;

public class DataTableBuilder {
    ItemRenderer itemRenderer;
    TextRenderer textRenderer;

    public DataTableBuilder(ItemRenderer itemRenderer, TextRenderer textRenderer) {
        this.itemRenderer = itemRenderer;
        this.textRenderer = textRenderer;
    }
    int textColor = 0xFFFFFFFF;
    public DataTableBuilder textColor(int color){
        this.textColor = textColor;
        return this;
    }
    int bgColor = 0xFF808080;
    public DataTableBuilder backgroundColor(int color){
        this.bgColor = color;
        return this;
    }
    int activeBgColor = 0xFF30B030;
    public DataTableBuilder activeColor(int color){
        this.activeBgColor = color;
        return this;
    }
    int gridColor = 0x0;
    public DataTableBuilder gridColor(int color){
        this.gridColor = color;
        return this;
    }
    boolean drawGrid = false;
    public DataTableBuilder drawGrid(boolean bool){
        this.drawGrid = bool;
        return this;
    }
    int height = 100;
    int width = 100;
    public DataTableBuilder size(int w, int h){
        this.width = w;
        this.height = h;
        return this;
    }

    int x;
    int y;
    public DataTableBuilder pos(int x, int y){
        this.x = x;
        this.y = y;
        return this;
    }

    public DataTable build(){
        return new DataTable(itemRenderer, textRenderer, textColor, bgColor, activeBgColor, gridColor, drawGrid, height, width, x,y);
    }
}
