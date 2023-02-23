package kovacsi0907.atlas.GUI.Screens.Shop;

import kovacsi0907.atlas.GUI.PapyrusButton;
import kovacsi0907.atlas.GUI.Pos2;
import kovacsi0907.atlas.GUI.Rendering;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SellScreen extends Screen {
    PlayerInventory playerInventory;
    private static final int MAIN_WIDTH = 200;
    private static final int TEXT_FIELD_WIDTH = 40;
    private static final int TEXT_FIELD_HEIGHT = 10;
    final Identifier GREEN_SELECTOR_TEXTURE = new Identifier("atlas:textures/gui/skills/obtained_frame.png");

    TextFieldWidget amountField;
    TextFieldWidget priceField;
    TextFieldWidget discountField;
    TextFieldWidget discountVolumeField;
    double MARGIN = 0.075;
    Pos2 mainTopLeft = new Pos2();
    Pos2 mainLowerRight = new Pos2();
    Pos2 invTopLeft = new Pos2();
    Pos2 invLowerRight = new Pos2();
    Pos2 inputsTopLeft = new Pos2();
    Pos2 inputsLowerRight = new Pos2();
    int BG_COLOR = 0xFF733D00;
    int TEXT_COLOR = 0xFFFFFFFF;
    int activeSlot = -1;
    PapyrusButton sellButton = new PapyrusButton(0, 0, Text.translatable("shop.sell_button"), (button -> {
        checkFields();
        sendSellRequest();
    }));

    String vendorId;

    public SellScreen(Text title, String vendorId, PlayerInventory playerInventory) {
        super(title);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.playerInventory = playerInventory;
        this.vendorId = vendorId;
        amountField = new TextFieldWidget(this.textRenderer, 0,0, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Text.literal(""));
        priceField = new TextFieldWidget(this.textRenderer, 0,0, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Text.literal(""));
        discountField = new TextFieldWidget(this.textRenderer, 0,0, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Text.literal(""));
        discountVolumeField = new TextFieldWidget(this.textRenderer, 0,0, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Text.literal(""));
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(amountField);
        this.addDrawableChild(priceField);
        this.addDrawableChild(discountField);
        this.addDrawableChild(discountVolumeField);
        this.addDrawableChild(sellButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        Rendering.fancyBorderAround(matrices, mainTopLeft.x, mainTopLeft.y, mainLowerRight.x, mainLowerRight.y, BG_COLOR);
        calculatePositions();
        if(activeSlot >= 0){
            Pos2 pos = getSlotPos(activeSlot);
            Rendering.texture(matrices, pos.x-1, pos.y-1, 17,17, GREEN_SELECTOR_TEXTURE);
        }
        renderInventory(matrices);
        renderInputs(matrices);
        checkFields();
        sellButton.active = activeSlot>=0;
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(int i = 0;i<playerInventory.main.size();i++){
            Pos2 pos = getSlotPos(i);
            if(!playerInventory.main.get(i).isEmpty() && mouseX>pos.x && mouseY>pos.y && mouseX<pos.x+18 && mouseY<pos.y+18) {
                activeSlot = i;
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void sendSellRequest() {
        int amount = Integer.parseInt(amountField.getText());
        int price = Integer.parseInt(priceField.getText());
        int discount = Integer.parseInt(discountField.getText());
        int discountVolume = Integer.parseInt(discountVolumeField.getText());
        if(activeSlot<0 || activeSlot>playerInventory.main.size())
            return;
        int itemId = Item.getRawId(playerInventory.main.get(activeSlot).getItem());
        String response = ClientNetworkFunctions.requestSellItemsAndGetResponse(activeSlot, itemId, vendorId, amount, price, discount, discountVolume);
        playerInventory.removeStack(activeSlot, amount);
        activeSlot = -1;
    }

    void renderInventory(MatrixStack matrices) {
        for(int i = 0;i<playerInventory.main.size();i++) {
            Pos2 slotPos = getSlotPos(i);
            DrawableHelper.fill(matrices, slotPos.x, slotPos.y, slotPos.x+16, slotPos.y+16, 0x30303030);
            Rendering.drawItemStack(this.itemRenderer,
                    MinecraftClient.getInstance().textRenderer,
                    playerInventory.main.get(i),
                    slotPos.x,
                    slotPos.y,
                    true);
        }
    }

    void renderInputs(MatrixStack matrices) {
        int inputX = invLowerRight.x - TEXT_FIELD_WIDTH;
        int inputY = inputsTopLeft.y;
        Rendering.text(matrices, Text.translatable("shop.amount_field"), inputsTopLeft.x, inputY, TEXT_COLOR, BG_COLOR);
        amountField.setPos(inputX, inputY);
        inputY += TEXT_FIELD_HEIGHT + 5;
        Rendering.text(matrices, Text.translatable("shop.price_field"), inputsTopLeft.x, inputY, TEXT_COLOR, BG_COLOR);
        priceField.setPos(inputX, inputY);
        inputY += TEXT_FIELD_HEIGHT + 5;
        Rendering.text(matrices, Text.translatable("shop.discount_field"), inputsTopLeft.x, inputY, TEXT_COLOR, BG_COLOR);
        discountField.setPos(inputX, inputY);
        inputY += TEXT_FIELD_HEIGHT + 5;
        Rendering.text(matrices, Text.translatable("shop.discount_volume_field"), inputsTopLeft.x, inputY, TEXT_COLOR, BG_COLOR);
        discountVolumeField.setPos(inputX, inputY);
    }

    Pos2 getSlotPos(int index) {
        int x = index%9;
        int y = index/9;
        int xPos = x*20+2+invTopLeft.x;
        int yPos = y*20+2+invTopLeft.y;
        return new Pos2(xPos, yPos);
    }
    void calculatePositions() {
        mainTopLeft.x = (this.width-MAIN_WIDTH)/2;
        mainLowerRight.x = mainTopLeft.x + MAIN_WIDTH;
        mainTopLeft.y = (int)(this.height*MARGIN);
        invTopLeft.x = mainTopLeft.x + (MAIN_WIDTH - 9*20)/2;
        invTopLeft.y = mainTopLeft.y+5;
        invLowerRight.x = invTopLeft.x + 9*20;
        invLowerRight.y = invTopLeft.y + 4*20;
        inputsTopLeft.x = getSlotPos(0).x;
        inputsTopLeft.y = invLowerRight.y + 10;
        inputsLowerRight.x = invLowerRight.x;
        inputsLowerRight.y = inputsTopLeft.y + 4*(TEXT_FIELD_HEIGHT + 5);
        sellButton.setPos(mainTopLeft.x + MAIN_WIDTH/2, inputsLowerRight.y + 20);
        mainLowerRight.y = sellButton.getY()+25;
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

        if(!discountVolumeField.isFocused()) {
            try{
                int value = Integer.parseInt(discountVolumeField.getText());
                if(value < 1)
                    discountVolumeField.setText("1");
                if(activeSlot < 0 || activeSlot >= playerInventory.main.size() || value > playerInventory.main.get(activeSlot).getCount())
                    discountVolumeField.setText("1");
            }catch (Exception e){
                discountVolumeField.setText("1");
            }
        }
    }
}
