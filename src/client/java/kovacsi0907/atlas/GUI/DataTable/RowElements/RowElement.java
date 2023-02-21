package kovacsi0907.atlas.GUI.DataTable.RowElements;

import kovacsi0907.atlas.GUI.DataTable.DataTable;
import net.minecraft.client.util.math.MatrixStack;

public abstract class RowElement {
    public RowElement(){
    }
    public void render(DataTable table, MatrixStack matrices, int x, int y, int height, int rowIndex, boolean selected, boolean mouseOver){
    }

    public int getWidth() {
        return 0;
    }
}
