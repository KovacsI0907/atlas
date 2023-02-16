package kovacsi0907.atlas.GUI.Screens.Shop;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.AtlasClient;
import kovacsi0907.atlas.GUI.Rendering;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SellScreen extends Screen {
    String vendorId;
    PlayerInventory playerInventory;
    int activeSlot = -1;
    final Identifier GREEN_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/obtained_frame.png");
    TextFieldWidget amountField;
    TextFieldWidget priceField;
    TextFieldWidget discountField;

    final float MARGIN = 0.1f;
    final int BORDER_WIDTH = 4;
    final int COLUMNS = 9;
    final int BORDER_COLOR = 0xFFFFFFFF;
    final int BG_COLOR = 0xFF808080;
    public SellScreen(Text title, String vendorId, PlayerInventory playerInventory) {
        super(title);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.vendorId = vendorId;
        this.playerInventory = playerInventory;
        amountField = new TextFieldWidget(this.textRenderer, 0,0, 30, 15, Text.translatable("shop.amount_field"));
        priceField = new TextFieldWidget(this.textRenderer, 0,0, 30, 15, Text.translatable("shop.price_field"));
        discountField = new TextFieldWidget(this.textRenderer, 0,0, 30, 15, Text.translatable("shop.discount_field"));
    }


    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(amountField);
        this.addDrawableChild(priceField);
        this.addDrawableChild(discountField);
        amountField.setPos(getSplitPosX()+10, getSlotY(0));
        priceField.setPos(getSplitPosX()+10, getSlotY(0) + 30);
        discountField.setPos(getSplitPosX()+10, getSlotY(0) + 60);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("shop.sell_button"), (button -> {
            sendSellRequest();
        })).width((int)(this.width*(1-MARGIN)-getSplitPosX()-10)).position(getSplitPosX()+5,(int)(this.height*(1-MARGIN))-BORDER_WIDTH-20).build());
        ClientNetworkFunctions.requestGetWareStacksForVendorAndWait(vendorId);
        playerInventory.player.sendMessage(Text.literal("offers: " + AtlasClient.wareStacks.size()));
    }

    private void sendSellRequest() {
        int amount = Integer.parseInt(amountField.getText());
        int price = Integer.parseInt(priceField.getText());
        int discount = Integer.parseInt(discountField.getText());
        if(activeSlot<0 || activeSlot>playerInventory.main.size())
            return;
        int itemId = Item.getRawId(playerInventory.main.get(activeSlot).getItem());
        String response = ClientNetworkFunctions.requestSellItemsAndGetResponse(activeSlot, itemId, vendorId, amount, price, discount);
        playerInventory.removeStack(activeSlot, amount);
        activeSlot = -1;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int x1 = (int)(this.width*MARGIN);
        int x2 = (int)(this.width*(1-MARGIN));
        int y1 = (int)(this.height*MARGIN);
        int y2 = (int)(this.height*(1-MARGIN));
        DrawableHelper.fill(matrices, x1,y1,x2,y2, BG_COLOR);
        Rendering.horizontalLine(matrices, x1, x2, y1, BORDER_WIDTH, BORDER_COLOR);
        Rendering.horizontalLine(matrices, x1, x2, y2, BORDER_WIDTH, BORDER_COLOR);
        Rendering.verticalLine(matrices, x1, y1, y2, BORDER_WIDTH, BORDER_COLOR);
        Rendering.verticalLine(matrices, x2, y1, y2, BORDER_WIDTH, BORDER_COLOR);
        Rendering.verticalLine(matrices, getSplitPosX(), y1, y2, BORDER_WIDTH, BORDER_COLOR);

        super.render(matrices, mouseX, mouseY, delta);

        for(int i = 0;i<playerInventory.main.size();i++) {
            int x = getSlotX(i);
            int y = getSlotY(i);
            DrawableHelper.fill(matrices, x, y, x + 18, y + 18, 0x30000000);
        }

        for(int i = 0;i<playerInventory.main.size();i++){
            int x = getSlotX(i);
            int y = getSlotY(i);
            drawItem(playerInventory.main.get(i), x, y);
            if(i == activeSlot)
                Rendering.texture(matrices, x-1, y-1, 19,19, GREEN_SELECTOR_TEXTURE);
            checkFields();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int i = 0;i<playerInventory.main.size();i++){
            int x = getSlotX(i);
            int y = getSlotY(i);
            if(!playerInventory.main.get(i).isEmpty() && mouseX>x && mouseY>y && mouseX<x+18 && mouseY<y+18)
                activeSlot = i;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawItem(ItemStack stack, int x, int y) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.translate(0.0F, 0.0F, 32.0F);
        RenderSystem.applyModelViewMatrix();
        this.setZOffset(200);
        this.itemRenderer.zOffset = 200.0F;
        this.itemRenderer.renderInGuiWithOverrides(stack, x, y);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, stack, x, y, Integer.toString(stack.getCount()));
        this.setZOffset(0);
        this.itemRenderer.zOffset = 0.0F;
    }

    void checkFields() {
        if(!amountField.isFocused()) {
            try{
                int value = Integer.parseInt(amountField.getText());
                if(value < 1)
                    amountField.setText("1");
                if(activeSlot < 0 || activeSlot >= playerInventory.main.size() || value > playerInventory.main.get(activeSlot).getCount())
                    amountField.setText("1");
            }catch (Exception e){
                amountField.setText("1");
            }
        }

        if(!priceField.isFocused()){
            try{
                int value = Integer.parseInt(priceField.getText());
                if(value<=0)
                    priceField.setText("1");
            }catch (Exception e){
                priceField.setText("1");
            }
        }

        if(!discountField.isFocused()){
            try {
                int value = Integer.parseInt(discountField.getText());
                if(value < 0)
                    discountField.setText("0");
                if(value > 100)
                    discountField.setText("100");
            }catch (Exception e){
                discountField.setText("0");
            }
        }
    }

    int getSlotX(int index) {
        return (int)(this.width * MARGIN + BORDER_WIDTH + 2 + 20*(index%COLUMNS));
    }

    int getSlotY(int index){
        return (int)(this.height * MARGIN + BORDER_WIDTH + 2 + 20*(index/COLUMNS));
    }

    int getSplitPosX() {
        return (int)(this.width*MARGIN + BORDER_WIDTH + COLUMNS*20 + BORDER_WIDTH);
    }
}
