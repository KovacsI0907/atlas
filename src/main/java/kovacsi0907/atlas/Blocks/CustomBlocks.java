package kovacsi0907.atlas.Blocks;

import kovacsi0907.atlas.Atlas;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomBlocks {
    public static BlockEntityType<SmithingStationBlockEntity> SMITHING_STATION_ENTITY;
    public static Block SILVER_ORE = new Block(FabricBlockSettings.of(Material.METAL).strength(2.0f).resistance(6.0f).requiresTool());
    public static Block TIN_ORE = new Block(FabricBlockSettings.of(Material.METAL).strength(1.0f).resistance(6.0f).requiresTool());
    public static Block SMITHING_STATION_MIDDLE = new Smithing_Station_Middle(FabricBlockSettings.of(Material.STONE).strength(0.5f).resistance(3.0f));
    public static Block SMITHING_STATION_TOP = new Smithing_Station_Middle(FabricBlockSettings.of(Material.STONE).strength(0.5f).resistance(3.0f));
    public static Block SMITHING_STATION_BLOCK;

    public static void registerBlocksAndEntities() {
        SILVER_ORE = Registry.register(Registries.BLOCK, new Identifier(Atlas.MOD_ID, "silver_ore"), SILVER_ORE);
        TIN_ORE= Registry.register(Registries.BLOCK, new Identifier(Atlas.MOD_ID,"tin_ore"), TIN_ORE);
        SMITHING_STATION_MIDDLE = Registry.register(Registries.BLOCK, new Identifier(Atlas.MOD_ID, "smithing_station_middle"), SMITHING_STATION_MIDDLE);
        SMITHING_STATION_TOP = Registry.register(Registries.BLOCK, new Identifier(Atlas.MOD_ID, "smithing_station_top"), SMITHING_STATION_TOP);

        SMITHING_STATION_BLOCK = Registry.register(Registries.BLOCK, new Identifier(Atlas.MOD_ID, "smithing_station"), new SmithingStationBlock(FabricBlockSettings.of(Material.STONE).strength(3f)));

        SMITHING_STATION_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Atlas.MOD_ID, "smithing_station"),
                FabricBlockEntityTypeBuilder.create(SmithingStationBlockEntity::new, SMITHING_STATION_BLOCK).build(null));
    }
}
