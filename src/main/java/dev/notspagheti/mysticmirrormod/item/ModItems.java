package dev.notspagheti.mysticmirrormod.item;

import dev.notspagheti.mysticmirrormod.MysticMirrorMod;
import dev.notspagheti.mysticmirrormod.item.custom.MysticMirrorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MysticMirrorMod.MOD_ID);

    public static final DeferredItem<Item> MYSTICMIRROR = ITEMS.register("mysticmirror",
            () -> new MysticMirrorItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(64)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}


