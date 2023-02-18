package kovacsi0907.atlas.GUI.Screens.Shop;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.ClientData;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.GUI.Rendering;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.UUID;

public class BuyScreen extends Screen {
    String vendorId;
    PlayerInventory playerInventory;
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
    public BuyScreen(Text title, String vendorId, PlayerInventory playerInventory) {
        super(title);
        this.vendorId = vendorId;
        this.playerInventory = playerInventory;
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

        Rendering.text(matrices, Text.translatable("shop.buy.item_name"), textStartX, getStackY(0)+5, TEXT_COLOR, BG_COLOR);
        Rendering.text(matrices, Text.translatable("shop.buy.amount"), textStartX + space*0.3f, getStackY(0)+5, TEXT_COLOR, BG_COLOR);
        Rendering.text(matrices, Text.translatable("shop.buy.price"), textStartX + space*0.4f, getStackY(0)+5, TEXT_COLOR, BG_COLOR);
        Rendering.text(matrices, Text.translatable("shop.buy.discount"), textStartX + space*0.5f-5, getStackY(0)+5, TEXT_COLOR, BG_COLOR);
        Rendering.text(matrices, Text.translatable("shop.buy.discount_volume"), textStartX+space*0.6f-10, getStackY(0)+5, TEXT_COLOR, BG_COLOR);
        Rendering.text(matrices, Text.translatable("shop.buy.player"), textStartX + space*0.7f+10, getStackY(0)+5, TEXT_COLOR, BG_COLOR);

        for(int i = getFirstStack(currentPage); i< ClientData.wareStacks.size() && i<getLastStack(currentPage); i++){
            WareStack ws = ClientData.wareStacks.get(i);
            float y = getStackY(i+1-getFirstStack(currentPage));
            int color = i%2==0?BG_COLOR:DARKER_BG_COLOR;
            if(mouseX>startX && mouseX<endX && mouseY>y && mouseY<y+ICON_SIZE)
                color = DARKER_BG_COLOR + 0x00101010;
            if(i == activeStack)
                color = ACTIVE_STACK_COLOR;
            drawRow(matrices, startX, textStartX, endX, space, y, color,
                    ws.item.getName(),
                    Integer.toString(ws.count),
                    Double.toString(ws.price),
                    ws.bulkDiscount + "%",
                    Integer.toString(ws.discountVolume),
                    MinecraftClient.getInstance().world.getPlayerByUuid(UUID.fromString(ws.playerUuid)).getDisplayName()
                    );
        }
        buyButton.active = checkInputAndMoney();
        super.render(matrices, mouseX, mouseY, delta);
        if(activeStack>=0) {
            WareStack stack = ClientData.wareStacks.get(activeStack);
            int amount;
            try{
                amount = Integer.parseInt(amountField.getText());
            }catch (Exception e){
                amount = 1;
            }
            double price = amount*(stack.price/stack.count);
            if(amount>=stack.discountVolume)
                price = price * (100-stack.bulkDiscount)/100;
            Rendering.text(matrices, "/" + stack.count, this.width * (1 - MARGIN) - 100 - BORDER_WIDTH - 18, getBuyPanelY() + 11, BORDER_COLOR, BG_COLOR);
            Rendering.text(matrices, "Buy " + amount + " for " + price + " Gold", this.width*MARGIN+BORDER_WIDTH, getBuyPanelY() + BORDER_WIDTH + 10, BORDER_COLOR, BG_COLOR);
        }

        for(int i = getFirstStack(currentPage);i<ClientData.wareStacks.size() && i<getLastStack(currentPage);i++){
            //float y = this.height * MARGIN + BORDER_WIDTH + i*ICON_SIZE;
            float y = getStackY(i+1-getFirstStack(currentPage));
            drawItem(new ItemStack(ClientData.wareStacks.get(i).item), startX, y);
        }
    }


    void drawRow(MatrixStack matrices, float startX, float textStartX, float endX, float space, float y, int color, Text itemName, String count, String price, String bulkDiscount, String discountVolume, Text playerName) {
        float textY = y + 5;
        Rendering.rectangle(matrices, startX, y, endX, y+ICON_SIZE, 1, BORDER_COLOR, color);
        Rendering.text(matrices, itemName, textStartX, textY, TEXT_COLOR, color);
        Rendering.text(matrices, count, textStartX + space*0.3f, textY, TEXT_COLOR, color);
        Rendering.text(matrices, price, textStartX + space*0.4f, textY, TEXT_COLOR, color);
        Rendering.text(matrices, bulkDiscount, textStartX + space*0.5f, textY, TEXT_COLOR, color);
        Rendering.text(matrices, discountVolume, textStartX+space*0.6f, textY, TEXT_COLOR, color);
        Rendering.text(matrices, playerName, textStartX + space*0.7f, textY, TEXT_COLOR, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float startX = this.width*MARGIN + BORDER_WIDTH;
        float endX = startX+getSpaceForList();
        for(int i = getFirstStack(currentPage);i<ClientData.wareStacks.size() && i<getLastStack(currentPage);i++){
            float y = getStackY(i+1-getFirstStack(currentPage));
            if(mouseX>startX && mouseX<endX && mouseY>y && mouseY<y+ICON_SIZE){
                activeStack = i;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void buyStack() {
        if(ClientData.wareStacks.size()>activeStack && activeStack>=0) {
            WareStack stack = ClientData.wareStacks.get(activeStack);
            int amount = Integer.parseInt(amountField.getText());
            String response = ClientNetworkFunctions.buyStackAndWait(stack.stackUuid, amount, vendorId);
            if(response.equals("success")){
                playerInventory.insertStack(new ItemStack(stack.item, amount));
            }
            playerInventory.player.sendMessage(Text.literal(response));
        }
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
        float spaceForStacks = this.height*(1-2*MARGIN) -2*BORDER_WIDTH-50;
        int stacksPerPage = (int) spaceForStacks/ICON_SIZE;
        return (page-1)*stacksPerPage;
    }

    int getLastStack(int page){
        float spaceForStacks = this.height*(1-2*MARGIN) -2*BORDER_WIDTH-50;
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
        if(activeStack<0 || activeStack>=ClientData.wareStacks.size())
            return false;
        WareStack stack = ClientData.wareStacks.get(activeStack);
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
