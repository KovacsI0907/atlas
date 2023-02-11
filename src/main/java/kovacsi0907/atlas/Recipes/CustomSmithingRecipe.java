package kovacsi0907.atlas.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CustomSmithingRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack result;
    private final DefaultedList<Ingredient> recipeItems;

    public CustomSmithingRecipe(Identifier id, ItemStack result, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.result = result;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient())
            return false;

        return recipeItems.get(0).test(inventory.getStack(1));
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
            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);
            for(int i = 0; i<inputs.size();i++){
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CustomSmithingRecipe(id, result, inputs);
        }

        @Override
        public CustomSmithingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            for (int i = 0;i<inputs.size();i++){
                inputs.set(i, Ingredient.fromPacket(buf));
            }
            ItemStack result = buf.readItemStack();
            return new CustomSmithingRecipe(id, result, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, CustomSmithingRecipe recipe) {
            buf.writeInt(recipe.recipeItems.size());
            for(Ingredient input : recipe.recipeItems){
                input.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
