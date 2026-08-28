package net.funny.funny_mod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.funny.funny_mod.FunnyMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static net.funny.funny_mod.FunnyMod.MOD_ID;

public class ModBlocks {

    //物品创建
    public static Block IMPROVE = registerBlock("improve", new Block(AbstractBlock.Settings.create().strength(10f, 1200f).requiresTool().sounds(BlockSoundGroup.WET_GRASS)));

    //1、创建 "注册BlockItem的" 方法
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }
    //2、使用 "注册BlockItem" 方法, 并创建 "注册Block" 的方法
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, name), block);
    }
    //3、使用 "注册Block" 的方法(内含 "注册BlockItem" 的方法)
    public static void registerModBlocks() {
        FunnyMod.LOGGER.info("Registering Mod Blocks For " + MOD_ID);
        //4、添加到物品组
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(IMPROVE);
        });
    }
}
