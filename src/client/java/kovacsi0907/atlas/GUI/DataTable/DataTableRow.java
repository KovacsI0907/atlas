package kovacsi0907.atlas.GUI.DataTable;

import kovacsi0907.atlas.GUI.DataTable.RowElements.RowElement;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class DataTableRow {
    List<RowElement> elements;
    List<Float> spacings;
    int height;

    public DataTableRow(int height) {
        this.height = height;
        elements = new ArrayList<>();
        spacings = new ArrayList<>();
    }

    public void add(RowElement element, float spacing){
        elements.add(element);
        spacings.add(spacing);
    }

    public boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width){
        return mouseX>x && mouseX<x+width && mouseY>y && mouseY<y+height;
    }

    public void render(DataTable table, MatrixStack matrices,int y, int rowIndex, int mouseX, int mouseY, boolean selected) {
        int bgColor = table.bgColor;
        if(rowIndex%2 == 0)
            bgColor -= 0x00101010;
        if(isMouseOver(mouseX, mouseY, table.x, y, table.width))
            bgColor -= 0x00101010;
        if(selected)
            bgColor = table.activeBgColor;
        DrawableHelper.fill(matrices, table.x,y,table.x+table.width, y+height, bgColor);
        for(int  i = 0;i<elements.size();i++){
            elements.get(i).render(
                    table,
                    matrices,
                    (int)(table.x + spacings.get(i) * table.width),
                    y,
                    this.height,
                    rowIndex,
                    selected,
                    isMouseOver(mouseX, mouseY, table.x, y, table.width)
            );
        }
    }
}
