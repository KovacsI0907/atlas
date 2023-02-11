package kovacsi0907.atlas;

import kovacsi0907.atlas.Blocks.CustomBlocks;
import kovacsi0907.atlas.Items.CustomItems;
import kovacsi0907.atlas.Network.ServerNetworkReceiver;
import kovacsi0907.atlas.Recipes.CustomRecipes;
import kovacsi0907.atlas.ScreenHandlers.CustomScreenHandlers;
import kovacsi0907.atlas.Skills.Skills;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Atlas implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("atlas");

	public static final String MOD_ID = "atlas";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");
		ServerNetworkReceiver.registerListeners();
		Skills.registerSkills();
		CustomBlocks.registerBlocksAndEntities();
		CustomItems.registerItems();
		CustomRecipes.registerRecipes();
		CustomScreenHandlers.regiterScreenHandlers();
	}
}