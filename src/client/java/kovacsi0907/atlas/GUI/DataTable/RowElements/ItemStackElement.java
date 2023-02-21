package kovacsi0907.atlas.GUI.DataTable.RowElements;

import kovacsi0907.atlas.GUI.DataTable.DataTable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ItemStackElement extends RowElement {
    ItemStack itemStack;
    boolean drawCount;

    public ItemStackElement(ItemStack itemStack, boolean drawCount) {
        this.itemStack = itemStack;
        this.drawCount = drawCount;
    }

    @Override
    public void render(DataTable table, MatrixStack matrices, int x, int y, int height, int rowIndex, boolean selected, boolean mouseOver) {
        table.itemRenderer.renderGuiItemIcon(itemStack, x,y);
        if(drawCount)
            table.itemRenderer.renderGuiItemOverlay(table.textRenderer, itemStack, x,y);
    }

    @Override
    public int getWidth() {
        return 16;
    }
}
