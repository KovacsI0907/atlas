package kovacsi0907.atlas.Items;

import kovacsi0907.atlas.Atlas;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomItems {

    public static  Item BUBI_KARDJA_XD = new BubiKardja(ToolMaterials.NETHERITE, new FabricItemSettings().maxCount(1));
    public static  Item TIN_INGOT= new Item(new FabricItemSettings().maxCount(64));
    public static  Item STEEL_INGOT= new Item(new FabricItemSettings().maxCount(64));
    public static void registerItems() {
        TIN_INGOT = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "tin_ingot"), TIN_INGOT);
        addToItemGroup(ItemGroups.INGREDIENTS,TIN_INGOT);
        STEEL_INGOT = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "steel_ingot"), STEEL_INGOT);
        addToItemGroup(ItemGroups.INGREDIENTS,STEEL_INGOT);
        BUBI_KARDJA_XD = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "bubi_kardja"), BUBI_KARDJA_XD);
        addToItemGroup(ItemGroups.COMBAT,BUBI_KARDJA_XD);
    }
    private static void addToItemGroup(ItemGroup group, Item  item){
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
}

