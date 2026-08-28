package net.funny.funny_mod;

import net.fabricmc.api.ModInitializer;
import net.funny.funny_mod.block.ModBlocks;
import net.funny.funny_mod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunnyMod implements ModInitializer {
	public static final String MOD_ID = "funny_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		LOGGER.info("It's Running Funny Mod Now!");

	}
}