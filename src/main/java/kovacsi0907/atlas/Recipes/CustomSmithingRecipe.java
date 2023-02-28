package kovacsi0907.atlas.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CustomSmithingRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack result;
    private final DefaultedList<Ingredient> recipeItems;

    public List<String> getSkillsNeeded() {
        return skillsNeeded;
    }

    private final List<String> skillsNeeded;

    public int getXpGain() {
        return xpGain;
    }

    public int getTimeToMake() {
        return timeToMake;
    }

    private final int xpGain;
    private final int timeToMake;

    private CustomSmithingRecipe(Identifier id, ItemStack result, DefaultedList<Ingredient> recipeItems, List<String> skillsNeeded, int xpGain, int timeToMake) {
        this.id = id;
        this.result = result;
        this.recipeItems = recipeItems;
        this.skillsNeeded = skillsNeeded;
        this.xpGain = xpGain;
        this.timeToMake = timeToMake;
    }

    @Override
    public boolean matches(SimpleInventory craftingInventory, World world) {
        if(world.isClient())
            return false;
        for(int i = 0;i< recipeItems.size();i++){
            int x = i%3;
            int y = i/3;
            int invIndex = 3*x + y;
            if(!recipeItems.get(i).test(craftingInventory.getStack(invIndex)))
                return false;
        }

        return true;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return this.result;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.result.copy();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CustomSmithingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "custom_smithing";
    }

    public static class Serializer implements RecipeSerializer<CustomSmithingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "custom_smithing";
        @Override
        public CustomSmithingRecipe read(Identifier id, JsonObject json) {
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            JsonArray ingredientsJson = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(9, Ingredient.EMPTY);
            for(int i = 0; i< ingredientsJson.size()&&i<ingredients.size();i++){
                ingredients.set(i, Ingredient.fromJson(ingredientsJson.get(i)));
            }
            DefaultedList<Ingredient> input = DefaultedList.ofSize(9, Ingredient.EMPTY);
            JsonArray pattern = JsonHelper.getArray(json, "pattern");
            for(int i = 0;i< input.size();i++){
                if(pattern.get(i).getAsInt() == 0)
                    continue;
                input.set(i, ingredients.get(pattern.get(i).getAsInt()-1));
            }
            List<String> skillsNeeded = new ArrayList<>();
            try{
                JsonArray skillJsonArray = JsonHelper.getArray(json, "skills_needed");
                for(JsonElement element : skillJsonArray)
                    skillsNeeded.add(element.getAsString());
            }catch (Exception ignored){}

            return new CustomSmithingRecipe(id, result, input, skillsNeeded, JsonHelper.getInt(json, "xp"), JsonHelper.getInt(json, "time"));
        }

        @Override
        public CustomSmithingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> recipeItems = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            recipeItems.replaceAll(ignored -> Ingredient.fromPacket(buf));
            int xpGain = buf.readInt();
            int timeToMake = buf.readInt();
            ItemStack result = buf.readItemStack();
            List<String> skillsNeeded = new ArrayList<>();
            for(int i = buf.readInt();i>0;i--){
                skillsNeeded.add(buf.readString());
            }

            return new CustomSmithingRecipe(id, result, recipeItems, skillsNeeded, xpGain, timeToMake);
        }

        @Override
        public void write(PacketByteBuf buf, CustomSmithingRecipe recipe) {
            buf.writeInt(recipe.recipeItems.size());
            for(Ingredient input : recipe.recipeItems){
                input.write(buf);
            }
            buf.writeInt(recipe.xpGain);
            buf.writeInt(recipe.timeToMake);
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.skillsNeeded.size());
            for(String skill : recipe.skillsNeeded){
                buf.writeString(skill);
            }
        }
    }
}
