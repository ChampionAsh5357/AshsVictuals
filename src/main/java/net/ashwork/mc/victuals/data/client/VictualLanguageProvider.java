package net.ashwork.mc.victuals.data.client;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.init.VictualCreativeTabs;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class VictualLanguageProvider extends LanguageProvider {

    public VictualLanguageProvider(PackOutput output) {
        super(output, AshsVictuals.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(VictualBlocks.APPLE_SEEDS, "Apple Seeds");
        this.add(VictualBlocks.Keys.APPLE_SAPLING, "Apple Sapling");

        this.addCreativeTab(VictualCreativeTabs.MAIN, "Ash's Victuals");
    }

    private void add(Component component, String name) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            this.add(translatable.getKey(), name);
        }
    }

    private void addCreativeTab(Supplier<? extends CreativeModeTab> tab, String name) {
        this.add(tab.get().getDisplayName(), name);
    }
}
