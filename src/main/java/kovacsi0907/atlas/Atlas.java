package kovacsi0907.atlas;

import Test.BoxBlock;
import Test.BoxBlockEntity;
import Test.BoxScreenHandler;
import Test.TestScreenHandler;
import kovacsi0907.atlas.Network.ServerNetworkReceiver;
import kovacsi0907.atlas.Skills.Skills;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Atlas implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("atlas");

	public static final String MOD_ID = "atlas";




	public static final Block BOX_BLOCK;
	public static final BlockItem BOX_BLOCK_ITEM;
	public static final BlockEntityType<BoxBlockEntity> BOX_BLOCK_ENTITY;
	public static final Identifier BOX = new Identifier(MOD_ID, "box_block");
	public static final ScreenHandlerType<TestScreenHandler> BOX_SCREEN_HANDLER;

	static {
		BOX_BLOCK = Registry.register(Registries.BLOCK, BOX, new BoxBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
		BOX_BLOCK_ITEM = Registry.register(Registries.ITEM, BOX, new BlockItem(BOX_BLOCK, new Item.Settings()));
		BOX_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, BOX, FabricBlockEntityTypeBuilder.create(BoxBlockEntity::new, BOX_BLOCK).build(null));
		BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BOX, TestScreenHandler::new);
	}





	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");
		ServerNetworkReceiver.registerListeners();
		Skills.registerSkills();
	}
}