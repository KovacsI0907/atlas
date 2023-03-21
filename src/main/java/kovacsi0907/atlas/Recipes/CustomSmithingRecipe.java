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
    public final DefaultedList<Integer> itemCounts;
    public final boolean allSkillsNecessary;

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

    private CustomSmithingRecipe(Identifier id, ItemStack result, DefaultedList<Ingredient> recipeItems, DefaultedList<Integer> itemCounts, List<String> skillsNeeded, boolean allSkillsNecessary, int xpGain, int timeToMake) {
        this.id = id;
        this.result = result;
        this.recipeItems = recipeItems;
        this.itemCounts = itemCounts;
        this.skillsNeeded = skillsNeeded;
        this.allSkillsNecessary = allSkillsNecessary;
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
            ItemStack stack = craftingInventory.getStack(invIndex);
            boolean enough = recipeItems.get(i).isEmpty() || stack.getCount() >= itemCounts.get(i);
            if(!recipeItems.get(i).test(stack) || !enough)
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

            DefaultedList<Integer> itemCounts = DefaultedList.ofSize(9, 1);
            try{
                JsonArray counts = JsonHelper.getArray(json, "counts");
                for(int i = 0;i<input.size();i++){
                    itemCounts.set(i, counts.get(i).getAsInt());
                }
            }catch (Exception ignored){}
            List<String> skillsNeeded = new ArrayList<>();
            try{
                JsonArray skillJsonArray = JsonHelper.getArray(json, "skills_needed");
                for(JsonElement element : skillJsonArray)
                    skillsNeeded.add(element.getAsString());
            }catch (Exception ignored){}
            boolean allSkillsNecessary = false;
            try{
                allSkillsNecessary = JsonHelper.getObject(json, "all_skills_necessary").getAsBoolean();
            }catch (Exception ignored){}
            return new CustomSmithingRecipe(id, result, input, itemCounts, skillsNeeded, allSkillsNecessary, JsonHelper.getInt(json, "xp"), JsonHelper.getInt(json, "time"));
        }

        @Override
        public CustomSmithingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> recipeItems = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            recipeItems.replaceAll(ignored -> Ingredient.fromPacket(buf));
            DefaultedList<Integer> itemCounts = DefaultedList.ofSize(9, 1);
            for(int i = 0;i<buf.readInt();i++){
                itemCounts.set(i, buf.readInt());
            }
            int xpGain = buf.readInt();
            int timeToMake = buf.readInt();
            ItemStack result = buf.readItemStack();
            List<String> skillsNeeded = new ArrayList<>();
            for(int i = buf.readInt();i>0;i--){
                skillsNeeded.add(buf.readString());
            }
            boolean allSkillsNecessary = buf.readBoolean();

            return new CustomSmithingRecipe(id, result, recipeItems, itemCounts, skillsNeeded, allSkillsNecessary, xpGain, timeToMake);
        }

        @Override
        public void write(PacketByteBuf buf, CustomSmithingRecipe recipe) {
            buf.writeInt(recipe.recipeItems.size());
            for(Ingredient input : recipe.recipeItems){
                input.write(buf);
            }
            buf.writeInt(recipe.itemCounts.size());
            for(Integer count : recipe.itemCounts){
                buf.writeInt(count);
            }
            buf.writeInt(recipe.xpGain);
            buf.writeInt(recipe.timeToMake);
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.skillsNeeded.size());
            for(String skill : recipe.skillsNeeded){
                buf.writeString(skill);
            }
            buf.writeBoolean(recipe.allSkillsNecessary);
        }
    }
}
