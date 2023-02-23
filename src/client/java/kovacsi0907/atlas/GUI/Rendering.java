package kovacsi0907.atlas.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import kovacsi0907.atlas.Atlas;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Rendering {

    static double PIXELS_PER_CHAR = 5.5f;
    static int LINE_HEIGHT = 10;

    static final Identifier FANCY_BORDER_VERTICAL_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/fancy_border_vertical.png");
    static final Identifier FANCY_BORDER_HORIZONTAL_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/fancy_border_horizontal.png");

    static final Identifier FANCY_BORDER_CORNER_TEXTURE = new Identifier(Atlas.MOD_ID, "textures/gui/fancy_border_corner.png");

    public static void textBox(MatrixStack matrices, String text, float x, float y, float width, float height, int color, int bgcolor){
        DrawableHelper.fill(matrices, (int)x, (int)y, (int)(x+width), (int)(y+height), bgcolor);
        ArrayList<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
        String row = "";
        y += 5;
        while(words.size() != 0){
            String word = words.get(0);
            if((row.length() + word.length())*PIXELS_PER_CHAR > width || word.equals("\n")){
                MinecraftClient.getInstance().textRenderer.draw(matrices, row, x, y, color);
                y += LINE_HEIGHT;
                row = "";
            }
            if(!word.equals("\n"))
                row += " " + word;
            words.remove(0);
        }
        MinecraftClient.getInstance().textRenderer.draw(matrices, row, x, y, color);
    }

    static void textBox(MatrixStack matrices, Text text, float x, float y, int width, int height, int color, int bgColor, int borderColor){
        textBox(matrices, new Text[]{text}, x, y, width, height, color, bgColor, borderColor);
    }

    public static void textBox(MatrixStack matrices, Text[] lines, float x, float y, float width, float height, int color, int bgColor, int borderColor) {
        List<OrderedText> wrappedLines = new ArrayList<>();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        for(Text line : lines){
            if(textRenderer.getWidth(line.asOrderedText())>width-6)
                wrappedLines.addAll(textRenderer.wrapLines(line, (int)width-6));
            else
                wrappedLines.add(line.asOrderedText());
        }

        int newHeight = (int)height;
        if(height == 0)
            newHeight = LINE_HEIGHT*wrappedLines.size();

        DrawableHelper.fill(matrices, (int)x, (int)y, (int)(x+width), (int)(y+newHeight), bgColor);
        int lineY = (int)(y+3);
        for(OrderedText line : wrappedLines){
            textRenderer.draw(matrices, line, x+3, lineY, color);
            lineY+=LINE_HEIGHT;
        }

        horizontalLine(matrices, x, x+width, y, 2, borderColor);
        horizontalLine(matrices, x, x+width, y+newHeight, 2, borderColor);
        verticalLine(matrices, x, y, y+newHeight, 2, borderColor);
        verticalLine(matrices, x+width, y, y+newHeight, 2, borderColor);
    }

    public static void verticalLine(MatrixStack matrices, float x, float y1, float y2, int thickness, int color){
        DrawableHelper.fill(matrices, (int)(x-thickness/2f), (int)y1, (int)(x+thickness/2f), (int)y2, color);
    }

    public static void horizontalLine(MatrixStack matrices, float x1, float x2, float y, int thickness, int color){
        DrawableHelper.fill(matrices, (int)x1, (int)(y-thickness/2f), (int)x2, (int)(y+thickness/2f), color);
    }

    public static void connectingLine(MatrixStack matrices, float x1, float y1, int x2, int y2, int thickness, int color){
        int dist1 = (int) Math.ceil((x2-x1) / 2.0);
        int dist2 = (int) Math.ceil((x1-x2) / 2.0);
        horizontalLine(matrices, x1, x1+dist1, y1, thickness, color);
        horizontalLine(matrices, x2, x2+dist2, y2, thickness, color);
        verticalLine(matrices, x1+dist1, y1, y2, thickness, color);
    }

    public static void connectingLine(MatrixStack matrices, Vec2f a, Vec2f b, int thickness, int color){
        connectingLine(matrices, (int)a.x, (int)a.y, (int)b.x, (int)b.y, thickness, color);
    }

    public static void connectingLine(MatrixStack matrices, Pos2 a, Pos2 b, int thickness, int color){
        connectingLine(matrices, a.x, a.y, b.x, b.y, thickness, color);
    }

    public static void texture(MatrixStack matrices, float x, float y, int w, int h, Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(matrices, (int)x, (int)y, 0, 0, w, h, w, h);
    }

    public static void text(MatrixStack matrices, Text txt, float x, float y, int color, int bgColor){
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        DrawableHelper.fill(matrices, (int)x, (int)y, (int)(x+textRenderer.getWidth(txt)), (int)(y+10), bgColor);
        textRenderer.draw(matrices, txt, x, y, color);
    }

    public static void text(MatrixStack matrices, String str, float x, float y, int color, int bgColor){
        Text txt = Text.literal(str);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        DrawableHelper.fill(matrices, (int)x, (int)y, (int)(x+textRenderer.getWidth(txt)), (int)(y+7), bgColor);
        textRenderer.draw(matrices, txt, x, y, color);
    }

    public static void rectangle(MatrixStack matrices,float x1, float y1, float x2, float y2, int broderWidth, int borderColor, int bgColor){
        DrawableHelper.fill(matrices, (int)x1, (int)y1,(int)x2, (int)y2, bgColor);
        Rendering.horizontalLine(matrices, x1, x2, y1, broderWidth, borderColor);
        Rendering.horizontalLine(matrices, x1, x2, y2, broderWidth, borderColor);
        Rendering.verticalLine(matrices, x1, y1, y2, broderWidth, borderColor);
        Rendering.verticalLine(matrices, x2, y1, y2, broderWidth, borderColor);
    }

    public static void drawItemStack(ItemRenderer itemRenderer, TextRenderer textRenderer, ItemStack stack, float x, float y, boolean drawCount){
        itemRenderer.renderGuiItemIcon(stack, (int)x, (int)y);
        if(drawCount){
            itemRenderer.renderGuiItemOverlay(textRenderer, stack, (int)x, (int)y, Integer.toString(stack.getCount()));
        }
    }

    public static void drawItemStack(ItemRenderer itemRenderer, TextRenderer textRenderer, ItemStack stack, float x, float y, int count){
        itemRenderer.renderGuiItemIcon(stack, (int)x, (int)y);
        itemRenderer.renderGuiItemOverlay(textRenderer, stack, (int)x, (int)y, Integer.toString(count));
    }

    public static void fancyBorderAround(MatrixStack matrices, float x1, float y1, float x2, float y2, int bgColor){
        int x = (int)x1-10;
        int y = (int)y1-10;
        int width = (int)(x2-x1)/10+1;
        int height = (int)(y2-y1)/10+1;
        DrawableHelper.fill(matrices, x+5, y+5, x+width*10+5, y+height*10+5, bgColor);
        for(int i = 1;i<width;i++){
            texture(matrices, x+i*10, y, 10,10, FANCY_BORDER_HORIZONTAL_TEXTURE);
            texture(matrices, x+i*10, y+height*10, 10, 10, FANCY_BORDER_HORIZONTAL_TEXTURE);
        }
        for(int i = 1;i<height;i++){
            texture(matrices, x, y+i*10, 10,10, FANCY_BORDER_VERTICAL_TEXTURE);
            texture(matrices, x+width*10, y+i*10, 10,10, FANCY_BORDER_VERTICAL_TEXTURE);
        }
        texture(matrices, x, y, 10,10,FANCY_BORDER_CORNER_TEXTURE);
        texture(matrices, x, y+height*10, 10, 10, FANCY_BORDER_CORNER_TEXTURE);
        texture(matrices, x+width*10, y, 10, 10, FANCY_BORDER_CORNER_TEXTURE);
        texture(matrices, x+width*10, y+height*10, 10, 10, FANCY_BORDER_CORNER_TEXTURE);
    }
}
