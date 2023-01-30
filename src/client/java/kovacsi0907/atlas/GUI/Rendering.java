package kovacsi0907.atlas.GUI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Arrays;

public final class Rendering {

    static double PIXELS_PER_CHAR = 5.5f;
    static int LINE_HEIGHT = 10;

    public static void textBox(MatrixStack matrices, String text, int x, int y, int width, int height, int color, int bgcolor){
        DrawableHelper.fill(matrices, x, y, x+width, y+height, bgcolor);
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
}
