package kovacsi0907.atlas.GUI.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.Atlas;
import kovacsi0907.atlas.ScreenHandlers.SmithingStationScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmithingStationScreen extends HandledScreen<SmithingStationScreenHandler> {
    final Identifier TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/screens/smithing_station_background.png");
    public SmithingStationScreen(SmithingStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
        renderProgressArrowAndFuel(matrices, i, j);
    }

    private void renderProgressArrowAndFuel(MatrixStack matrices, int x, int y) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        if(handler.isCrafting()) {
            int arrowSize = (int)(handler.getScaledProgress()*25);
            drawTexture(matrices, x + 78, y+27, 176, 14, arrowSize, 17);
        }
        int fuelProgress = 14-(int)(handler.getScaledFuel()*14);
        if(fuelProgress < 14)
            drawTexture(matrices, x+82, y+fuelProgress+44, 176, fuelProgress, 15, 14-fuelProgress);
    }
}
