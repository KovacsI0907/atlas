package kovacsi0907.atlas.GUI.DataTable;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class DataTable {
    public List<DataTableRow> rows;
    public final ItemRenderer itemRenderer;
    public final TextRenderer textRenderer;

    public int textColor;
    public int bgColor;
    public int activeBgColor;
    public int gridColor;
    public boolean drawGrid;

    int height;
    int width;
    int x;
    int y;
    int selectedRow;

    public DataTable(ItemRenderer itemRenderer, TextRenderer textRenderer, int textColor, int bgColor, int activeBgColor, int gridColor, boolean drawGrid, int height, int width, int x, int y) {
        this.itemRenderer = itemRenderer;
        this.textRenderer = textRenderer;
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.activeBgColor = activeBgColor;
        this.gridColor = gridColor;
        this.drawGrid = drawGrid;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;

        this.rows = new ArrayList<>();
        this.selectedRow = -1;
    }

    public void addRow(DataTableRow row){
        this.rows.add(row);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY){
        int currentY = this.y;
        for(int i = 0;i<this.rows.size();i++){
            this.rows.get(i).render(this, matrices, currentY, i, mouseX, mouseY, i == selectedRow);
            currentY += this.rows.get(i).height;
        }
    }

    public void onClick(int mouseX, int mouseY){
        int currentY = this.y;
        for(int i = 0;i<this.rows.size();i++){
            if(this.rows.get(i).isMouseOver(mouseX,mouseY, this.x, currentY, this.width)){
                selectedRow = i;
                return;
            }
            currentY += this.rows.get(i).height;
        }
    }
}
