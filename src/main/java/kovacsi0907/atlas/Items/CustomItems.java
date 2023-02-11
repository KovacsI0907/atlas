package kovacsi0907.atlas.Items;

import kovacsi0907.atlas.Atlas;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomItems {
    public static Item BUBI_KARDJA_XD = new BubiKardja(ToolMaterials.NETHERITE, new FabricItemSettings().maxCount(1));
    public static void registerItems() {
        BUBI_KARDJA_XD = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "bubi_kardja"), BUBI_KARDJA_XD);
    }
}
