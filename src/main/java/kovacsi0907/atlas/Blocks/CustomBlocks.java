package kovacsi0907.atlas.Blocks;

import kovacsi0907.atlas.Atlas;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomBlocks {
    public static BlockEntityType<SmithingStationBlockEntity> SMITHING_STATION_ENTITY;
    public static final Block SMITHING_STATION_BLOCK = Registry.register(Registries.BLOCK,
            new Identifier(Atlas.MOD_ID, "smithing_station"),
            new SmithingStationBlock(FabricBlockSettings.of(Material.STONE).strength(3f)));

    public static void registerBlocksAndEntities() {
        SMITHING_STATION_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Atlas.MOD_ID, "smithing_station"),
                FabricBlockEntityTypeBuilder.create(SmithingStationBlockEntity::new, SMITHING_STATION_BLOCK).build(null));
    }
}
