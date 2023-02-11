package kovacsi0907.atlas.Recipes;

import kovacsi0907.atlas.Atlas;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER,
                new Identifier(Atlas.MOD_ID, CustomSmithingRecipe.Serializer.ID),
                CustomSmithingRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(Atlas.MOD_ID,
                CustomSmithingRecipe.Type.ID),
                CustomSmithingRecipe.Type.INSTANCE);
    }
}
