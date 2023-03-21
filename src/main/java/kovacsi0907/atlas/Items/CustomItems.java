package kovacsi0907.atlas.Items;

import kovacsi0907.atlas.Atlas;
import kovacsi0907.atlas.Blocks.CustomBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomItems {

    public static  Item BUBI_KARDJA_XD = new BubiKardja(ToolMaterials.NETHERITE, new FabricItemSettings().maxCount(1));
    public static  Item TIN_INGOT= new Item(new FabricItemSettings().maxCount(64));
    public static  Item STEEL_INGOT= new Item(new FabricItemSettings().maxCount(64));
    public static Item STEEL_NUGGET= new Item(new FabricItemSettings().maxCount(64));
    public static Item SILVER_INGOT=new Item(new FabricItemSettings().maxCount(64));
    public static Item SILVER_ORE_ITEM=new BlockItem(CustomBlocks.SILVER_ORE, new FabricItemSettings());
    public static  Item TIN_ORE_ITEM=new BlockItem(CustomBlocks.TIN_ORE, new FabricItemSettings());
    public static void registerItems() {
        TIN_INGOT = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "tin_ingot"), TIN_INGOT);
        addToItemGroup(ItemGroups.INGREDIENTS,TIN_INGOT);
        STEEL_INGOT = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "steel_ingot"), STEEL_INGOT);
        addToItemGroup(ItemGroups.INGREDIENTS,STEEL_INGOT);
        BUBI_KARDJA_XD = Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "bubi_kardja"), BUBI_KARDJA_XD);
        addToItemGroup(ItemGroups.COMBAT,BUBI_KARDJA_XD);
        STEEL_NUGGET=Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "steel_nugget"), STEEL_NUGGET);
        addToItemGroup(ItemGroups.INGREDIENTS,STEEL_NUGGET);
        SILVER_INGOT=Registry.register(Registries.ITEM, new Identifier(Atlas.MOD_ID, "silver_ingot"),SILVER_INGOT);
        addToItemGroup(ItemGroups.INGREDIENTS,SILVER_INGOT);
        SILVER_ORE_ITEM=Registry.register(Registries.ITEM,new Identifier(Atlas.MOD_ID,"silver_ore"), SILVER_ORE_ITEM);
        addToItemGroup(ItemGroups.NATURAL,SILVER_ORE_ITEM);
        TIN_ORE_ITEM=Registry.register(Registries.ITEM,new Identifier(Atlas.MOD_ID,"tin_ore"),TIN_ORE_ITEM);
        addToItemGroup(ItemGroups.NATURAL,TIN_ORE_ITEM);
    }
    private static void addToItemGroup(ItemGroup group, Item  item ){
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
}

