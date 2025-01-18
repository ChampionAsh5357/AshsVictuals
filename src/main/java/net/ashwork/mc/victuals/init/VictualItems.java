package net.ashwork.mc.victuals.init;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VictualItems {

    static void register(IEventBus modBus) {
        modBus.addListener(VictualItems::modifyCreativeTabs);
    }

    private static void modifyCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            // TODO: Add to custom tab as well
            addSeedsAndSapling(VictualBlocks.APPLE_SEEDS::toStack, VictualBlocks.Keys.APPLE_SAPLING, event::accept);
        }
    }

    private static void addSeedsAndSapling(Supplier<ItemStack> stackFactory, MutableComponent saplingName, Consumer<ItemStack> tab) {
        tab.accept(stackFactory.get());
        var sapling = stackFactory.get();
        sapling.set(VictualDataComponentTypes.SAPLING, Unit.INSTANCE);
        sapling.set(DataComponents.ITEM_NAME, saplingName);
        tab.accept(sapling);
    }
}
