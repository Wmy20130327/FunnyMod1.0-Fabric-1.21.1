package net.funny.funny_mod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.funny.funny_mod.FunnyMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.funny.funny_mod.FunnyMod.MOD_ID;

public class ModItems {

    // 必须使用 new JokerItem(...) 而不是 new Item(...)
    public static final Item JOKER = registerItem("joker", new JokerItem(new Item.Settings().maxCount(16)));
    public static final Item ANTIDOTE = registerItem("antidote", new AntidoteItem(new Item. Settings()));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }


    public static void registerModItems() {
        FunnyMod.LOGGER.info("Registering Mod Items For" + MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(JOKER);
            entries.add(ANTIDOTE);
        });
    }

}
