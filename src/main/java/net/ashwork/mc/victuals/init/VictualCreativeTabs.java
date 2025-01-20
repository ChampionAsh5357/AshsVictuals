package net.ashwork.mc.victuals.init;

import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class VictualCreativeTabs {

    public static final Supplier<CreativeModeTab> MAIN = creativeTab("main", VictualBlocks.APPLE_SEEDS::toStack, (params, output) -> {
        output.accept(Items.APPLE);
        addSeedsAndSapling(VictualBlocks.APPLE_SEEDS::toStack, VictualBlocks.Keys.APPLE_SAPLING, output::accept);
    });

    private static Supplier<CreativeModeTab> creativeTab(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator generator) {
        return VictualRegistrars.CREATIVE_TAB.register(name, () -> CreativeModeTab.builder()
                .title(VictualHelper.createTranslation(Registries.CREATIVE_MODE_TAB, name)).icon(icon).displayItems(generator).build());
    }

    private static void modifyCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            addSeedsAndSapling(VictualBlocks.APPLE_SEEDS::toStack, VictualBlocks.Keys.APPLE_SAPLING, event::accept);
        }
    }

    private static void addSeedsAndSapling(Supplier<ItemStack> stackFactory, Component saplingName, Consumer<ItemStack> tab) {
        tab.accept(stackFactory.get());
        var sapling = stackFactory.get();
        sapling.set(VictualDataComponentTypes.SAPLING, Unit.INSTANCE);
        sapling.set(DataComponents.ITEM_NAME, saplingName);
        tab.accept(sapling);
    }

    static void register(IEventBus modBus) {
        modBus.addListener(VictualCreativeTabs::modifyCreativeTabs);
    }
}
