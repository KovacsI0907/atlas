package kovacsi0907.atlas.GUI.Screens.Shop;

import kovacsi0907.atlas.Atlas;
import kovacsi0907.atlas.ClientData;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.GUI.DataTable.DataTable;
import kovacsi0907.atlas.GUI.DataTable.DataTableBuilder;
import kovacsi0907.atlas.GUI.DataTable.DataTableRow;
import kovacsi0907.atlas.GUI.DataTable.RowElements.ItemStackElement;
import kovacsi0907.atlas.GUI.DataTable.RowElements.TextElement;
import kovacsi0907.atlas.GUI.PapyrusButton;
import kovacsi0907.atlas.GUI.Pos2;
import kovacsi0907.atlas.GUI.Rendering;
import kovacsi0907.atlas.GUI.TextureButton;
import kovacsi0907.atlas.Network.ClientNetworkFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.UUID;

public class BuyScreen extends Screen {
    private static final int MONEY_ICON_SIZE = 16;
    private static final float MARGIN = 0.1f;
    private static final int TEXT_FIELD_WIDTH = 20;
    private static final int TEXT_FIELD_HEIGHT = 10;

    private static final Identifier MONEY_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/money_pouch.png");
    private static final Identifier LEFT_ARROW_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/left_arrow.png");
    private static final Identifier RIGHT_ARROW_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/right_arrow.png");
    Pos2 mainUpperLeft = new Pos2(0,0);
    Pos2 mainLowerRight = new Pos2(0,0);
    Pos2 tableUpperLeft = new Pos2(0,0);
    Pos2 tableLowerRight = new Pos2(0,0);
    Pos2 panelUpperLeft = new Pos2(0,0);
    Pos2 panelLowerRight = new Pos2(0,0);
    Pos2 allSpace = new Pos2(0,0);
    Pos2 buttonCenter = new Pos2(0,0);
    Pos2 textFieldPos = new Pos2(0,0);
    Pos2 maxAmountPos = new Pos2(0,0);
    Pos2 moneyIconPos = new Pos2(0,0);
    Pos2 moneyTextPos = new Pos2(0,0);
    Pos2 responseTextPos = new Pos2(0,0);
    Pos2 leftArrowPos = new Pos2(0,0);
    Pos2 rightArrowPos = new Pos2(0,0);
    int BG_COLOR = 0xFF733D00;
    int TEXT_COLOR = 0xFFFFFFFF;
    DataTable table;
    boolean mouseClicked = false;
    String vendorId;

    int pageNo = 0;
    int stacksPerPage = 0;

    PapyrusButton buyButton = new PapyrusButton(0,0, Text.translatable("shop.buy_button"), (button -> {
        buy();
    }));

    TextureButton leftArrow = new TextureButton(0, 0, LEFT_ARROW_TEXTURE, 8, 8, (button -> {
        pageNo--;
        updateStacks();
    }));
    TextureButton rightArrow = new TextureButton(0,0, RIGHT_ARROW_TEXTURE, 8, 8, (button -> {
        pageNo++;
        updateStacks();
    }));
    TextFieldWidget amountField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Text.literal(""));
    public BuyScreen(Text title, String vendorId) {
        super(title);
        this.vendorId = vendorId;
    }
    Text buyResponse = Text.empty();
    @Override
    protected void init() {
        super.init();
        ClientNetworkFunctions.requestGetMoneyAndWait();
        this.addDrawableChild(buyButton);
        this.addDrawableChild(amountField);
        this.addDrawableChild(leftArrow);
        this.addDrawableChild(rightArrow);
        table = new DataTableBuilder(this.itemRenderer, MinecraftClient.getInstance().textRenderer)
                .backgroundColor(BG_COLOR)
                .textColor(TEXT_COLOR)
                .size(allSpace.x, allSpace.y)
                .build();
        DataTableRow headerRow = new DataTableRow(12);
        headerRow.add(new TextElement(Text.translatable("shop.buy.item_name"), true, true), 0f);
        headerRow.add(new TextElement(Text.translatable("shop.buy.amount"), true, true), 0.4f);
        headerRow.add(new TextElement(Text.translatable("shop.buy.price"), true, true), 0.5f);
        headerRow.add(new TextElement(Text.translatable("shop.buy.discount"), true, true), 0.6f);
        headerRow.add(new TextElement(Text.translatable("shop.buy.player_name"), true, true), 0.8f);
        table.headerRow = headerRow;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        buyButton.active = true;
        calculatePositions();
        int newStacksPerPage = (tableLowerRight.y - tableUpperLeft.y)/16;
        if(stacksPerPage != newStacksPerPage){
            stacksPerPage = newStacksPerPage;
            updateStacks();
        }

        Rendering.fancyBorderAround(matrices, mainUpperLeft.x, mainUpperLeft.y, mainLowerRight.x, mainLowerRight.y, BG_COLOR);
        buyButton.setPos(buttonCenter.x, buttonCenter.y);
        amountField.setPos(textFieldPos.x, textFieldPos.y);
        leftArrow.setPos(leftArrowPos.x, leftArrowPos.y);
        leftArrow.active = pageNo>0;
        rightArrow.active = ClientData.wareStacks.size()==stacksPerPage;
        rightArrow.setPos(rightArrowPos.x, rightArrowPos.y);
        Rendering.texture(matrices, moneyIconPos.x, moneyIconPos.y, 16, 16, MONEY_TEXTURE);
        if(table.selectedRow >= 0) {
            WareStack stack = ClientData.wareStacks.get(table.selectedRow);
            Rendering.text(matrices, Text.literal("/" + stack.count), maxAmountPos.x, maxAmountPos.y, TEXT_COLOR, BG_COLOR);
            Rendering.text(matrices, Text.literal(ClientData.money + "-" + getPriceString()), moneyTextPos.x, moneyTextPos.y, TEXT_COLOR, BG_COLOR);
            if(getPrice()>ClientData.money)
                buyButton.active = false;
        }else {
            Rendering.text(matrices, Text.literal("/0"), maxAmountPos.x, maxAmountPos.y, TEXT_COLOR, BG_COLOR);
            Rendering.text(matrices, Text.literal(Double.toString(ClientData.money)), moneyTextPos.x, moneyTextPos.y, TEXT_COLOR, BG_COLOR);
        }
        if(!Objects.equals(buyResponse, Text.empty()))
            Rendering.text(matrices, buyResponse, responseTextPos.x, responseTextPos.y, 0xFFB03030, BG_COLOR);

        if(mouseClicked)
            table.onClick(mouseX, mouseY);
        prepareTable();
        table.render(matrices, mouseX, mouseY);

        mouseClicked = false;

        checkInput();
        if(getAmount()<=0)
            buyButton.active = false;
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void checkInput() {
        if(table.selectedRow < 0) {
            amountField.setText("");
            return;
        }

        int amount = getAmount();
        if(amountField.getText().equals(""))
            return;
        if(amount == -1) {
            amountField.setText("1");
            return;
        }
        WareStack stack = ClientData.wareStacks.get(table.selectedRow);
        if(amount>stack.count)
            amountField.setText(Integer.toString(stack.count));
    }

    void updateStacks(){
        int firstStack = pageNo*stacksPerPage;
        ClientNetworkFunctions.requestGetWareStacksForVendorAndWait(vendorId, firstStack, stacksPerPage);
    }

    void buy(){
        if(table.selectedRow<0)
            return;
        if(getAmount() < 1)
            return;
        ClientNetworkFunctions.requestGetMoneyAndWait();
        WareStack stack = ClientData.wareStacks.get(table.selectedRow);
        if(ClientNetworkFunctions.buyStackAndWait(stack.stackUuid, getAmount(), this.vendorId).equals("success"))
            buyResponse = Text.empty();
        else
            buyResponse = Text.translatable("shop.error_occurred");
        table.selectedRow = -1;
        updateStacks();
        ClientNetworkFunctions.requestGetMoneyAndWait();
    }

    int getAmount() {
        if(amountField.getText().equals(""))
            return 0;
        int amount;
        try{
            amount = Integer.parseInt(amountField.getText());
        }catch (Exception e){
            amount = -1;
        }

        return amount;
    }

    double getPrice() {
        if(table.selectedRow<0)
            return 0;
        WareStack stack = ClientData.wareStacks.get(table.selectedRow);
        double price = getAmount()/(double)stack.count*stack.price;
        if(getAmount()>=stack.discountVolume)
            price = price*(100-stack.bulkDiscount)/100;
        return price;
    }

    String getPriceString() {
        double pr = Math.round(getPrice()*100.0)/100.0;
        return Double.toString(pr);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseClicked = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void calculatePositions() {
        mainUpperLeft.x = (int)(this.width*MARGIN);
        mainUpperLeft.y = (int)(this.height*MARGIN);
        allSpace.x = (int)(this.width*(1-2*MARGIN));
        allSpace.y = (int)(this.height*(1-2*MARGIN));
        mainLowerRight.x = mainUpperLeft.x+allSpace.x;
        mainLowerRight.y = mainUpperLeft.y+allSpace.y;
        tableUpperLeft.x = mainUpperLeft.x+5;
        tableUpperLeft.y = mainUpperLeft.y+5;
        tableLowerRight.x = mainLowerRight.x-5;
        tableLowerRight.y = mainLowerRight.y-5-40-16;
        panelUpperLeft.x = tableUpperLeft.x;
        panelUpperLeft.y = tableLowerRight.y + 5;
        panelLowerRight.x = tableLowerRight.x;
        panelLowerRight.y = mainLowerRight.y-5;
        buttonCenter.x = panelUpperLeft.x + (panelLowerRight.x-panelUpperLeft.x)/2/2;
        buttonCenter.y = panelUpperLeft.y + (panelLowerRight.y-panelUpperLeft.y)/2;
        textFieldPos.x = panelUpperLeft.x + (panelLowerRight.x-panelUpperLeft.x)/2 + 5;
        textFieldPos.y = panelUpperLeft.y + 20;
        maxAmountPos.x = textFieldPos.x + TEXT_FIELD_WIDTH + 2;
        maxAmountPos.y = textFieldPos.y + 2;
        moneyIconPos.x = maxAmountPos.x + 25;
        moneyIconPos.y = textFieldPos.y-3;
        moneyTextPos.x = moneyIconPos.x + MONEY_ICON_SIZE +  2;
        moneyTextPos.y = maxAmountPos.y;
        responseTextPos.x = textFieldPos.x;
        responseTextPos.y = textFieldPos.y + TEXT_FIELD_HEIGHT + 10;
        leftArrowPos.x = mainUpperLeft.x + (mainLowerRight.x-mainUpperLeft.x)/2-20;
        leftArrowPos.y = tableLowerRight.y;
        rightArrowPos.x = mainUpperLeft.x + (mainLowerRight.x-mainUpperLeft.x)/2+4;
        rightArrowPos.y = tableLowerRight.y;
    }

    void prepareTable() {
        table.x = tableUpperLeft.x;
        table.y = tableUpperLeft.y;
        table.width = tableLowerRight.x - table.x;
        table.height = tableLowerRight.y -table.y;
        table.rows.clear();
        for(int i = 0; i< ClientData.wareStacks.size(); i++){
            WareStack wareStack = ClientData.wareStacks.get(i);
            DataTableRow row = new DataTableRow(16);
            row.add(new ItemStackElement(
                    new ItemStack(wareStack.item, wareStack.count), true), 0);
            row.add(new TextElement(
                    wareStack.item.getName(), true, true), 0.1f);
            row.add(new TextElement(
                    Integer.toString(wareStack.count), true, true), 0.4f);
            row.add(new TextElement(
                    Double.toString((int)(wareStack.price*100)/100.0), true, true), 0.5f);
            if(wareStack.bulkDiscount > 0)
                row.add(new TextElement(
                    wareStack.bulkDiscount + "% >=" + wareStack.discountVolume, true, true), 0.6f);
            row.add(new TextElement(
                    MinecraftClient.getInstance().world.getPlayerByUuid(UUID.fromString(wareStack.playerUuid)).getDisplayName(), true, true), 0.8f);
            table.addRow(row);
        }
    }
}
