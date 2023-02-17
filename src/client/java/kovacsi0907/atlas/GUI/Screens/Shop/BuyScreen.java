package kovacsi0907.atlas.GUI.Screens.Shop;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.AtlasClient;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.GUI.Rendering;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BuyScreen extends Screen {
    String vendorId;
    final float MARGIN = 0.1f;
    final int BORDER_WIDTH = 4;

    final int TEXT_COLOR = 0xFFFFFFFF;
    final int BG_COLOR = 0xFF808080;
    final int DARKER_BG_COLOR = 0xFF909090;
    final int BORDER_COLOR = 0xFFFFFFFF;
    final int ACTIVE_STACK_COLOR = 0xFF30B030;
    final int ICON_SIZE = 18;
    int currentPage = 1;
    int activeStack = -1;
    TextFieldWidget amountField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0,0,20,20,Text.literal(""));
    ButtonWidget buyButton = ButtonWidget.builder(Text.translatable("shop.buy_button"), (button -> {
        buyStack();
    })).size(100, 20).build();
    public BuyScreen(Text title, String vendorId) {
        super(title);
        this.vendorId = vendorId;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(buyButton);
        buyButton.setPos((int)(this.width*(1-MARGIN)-100-BORDER_WIDTH),getBuyPanelY()+5);
        this.addDrawableChild(amountField);
        amountField.setPos((int)(this.width*(1-MARGIN)-100-BORDER_WIDTH)-40,getBuyPanelY()+5);
        amountField.setText("1");
        ClientNetworkFunctions.requestGetWareStacksForVendorAndWait(vendorId);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        Rendering.rectangle(matrices,
                this.width*MARGIN,
                this.height*MARGIN,
                this.width*(1-MARGIN),
                this.height*(1-MARGIN),
                BORDER_WIDTH,
                BORDER_COLOR,
                BG_COLOR);

        float space = getSpaceForList();
        float startX = this.width*MARGIN + BORDER_WIDTH;
        float textStartX = startX + ICON_SIZE;
        float endX = startX+space;

        for(int i = getFirstStack(currentPage);i<AtlasClient.wareStacks.size() && i<getLastStack(currentPage);i++){
            WareStack ws = AtlasClient.wareStacks.get(i);
            float y = getStackY(i);
            float textY = y + 5;
            int color = i%2==0?BG_COLOR:DARKER_BG_COLOR;
            if(mouseX>startX && mouseX<endX && mouseY>y && mouseY<y+ICON_SIZE)
                color = DARKER_BG_COLOR + 0x00101010;
            if(i == activeStack)
                color = ACTIVE_STACK_COLOR;
            Rendering.rectangle(matrices, startX, y, endX, y+ICON_SIZE, 1, BORDER_COLOR, color);
            Rendering.text(matrices, ws.item.getName(), textStartX, textY, TEXT_COLOR, color);
            Rendering.text(matrices, Integer.toString(ws.count), textStartX + space*0.3f, textY, TEXT_COLOR, color);
            Rendering.text(matrices, Double.toString(ws.price), textStartX + space*0.4f, textY, TEXT_COLOR, color);
            Rendering.text(matrices, ws.price * (100 - ws.bulkDiscount) / 100 + "(" + ws.bulkDiscount + "%)", textStartX + space*0.5f, textY, TEXT_COLOR, color);
            Rendering.text(matrices, "player name", textStartX + space*0.7f, textY, TEXT_COLOR, color);
        }
        buyButton.active = checkInputAndMoney();
        super.render(matrices, mouseX, mouseY, delta);
        if(activeStack>=0)
            Rendering.text(matrices, "/" + AtlasClient.wareStacks.get(activeStack).count, this.width*(1-MARGIN)-100-BORDER_WIDTH-18, getBuyPanelY()+11, BORDER_COLOR, BG_COLOR);

        for(int i = getFirstStack(currentPage);i<AtlasClient.wareStacks.size() && i<getLastStack(currentPage);i++){
            float y = this.height * MARGIN + BORDER_WIDTH + i*ICON_SIZE;
            drawItem(new ItemStack(AtlasClient.wareStacks.get(i).item), startX, y);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float startX = this.width*MARGIN + BORDER_WIDTH;
        float endX = startX+getSpaceForList();
        for(int i = getFirstStack(currentPage);i<AtlasClient.wareStacks.size() && i<getLastStack(currentPage);i++){
            int y = (int)getStackY(i);
            if(mouseX>startX && mouseX<endX && mouseY>y && mouseY<y+ICON_SIZE){
                activeStack = i;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void buyStack() {
    }

    private void drawItem(ItemStack stack, float x, float y) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.translate(0.0F, 0.0F, 32.0F);
        RenderSystem.applyModelViewMatrix();
        this.setZOffset(200);
        this.itemRenderer.zOffset = 200.0F;
        this.itemRenderer.renderInGuiWithOverrides(stack, (int)x, (int)y);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, stack, (int)x, (int)y, Integer.toString(stack.getCount()));
        this.setZOffset(0);
        this.itemRenderer.zOffset = 0.0F;
    }

    int getFirstStack(int page){
        float spaceForStacks = this.height*(1-2*MARGIN) -2*BORDER_WIDTH-100;
        int stacksPerPage = (int) spaceForStacks/ICON_SIZE;
        return (page-1)*stacksPerPage;
    }

    int getLastStack(int page){
        float spaceForStacks = this.height*(1-2*MARGIN) -2*BORDER_WIDTH-100;
        int stacksPerPage = (int) spaceForStacks/ICON_SIZE;
        return page*stacksPerPage;
    }

    float getSpaceForList() {
        return this.width*(1-2*MARGIN) - 2*BORDER_WIDTH;
    }

    float getStackY(int index){
        return this.height * MARGIN + BORDER_WIDTH + index*ICON_SIZE;
    }

    int getBuyPanelY() {
        return (int)(this.height*(1-MARGIN)-30);
    }

    boolean checkInputAndMoney() {
        if(activeStack<0 || activeStack>=AtlasClient.wareStacks.size())
            return false;
        WareStack stack = AtlasClient.wareStacks.get(activeStack);
        try {
            int value = Integer.parseInt(amountField.getText());
            double price = value*(stack.price/stack.count);
            if(value>stack.count || value<=0)
                return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
