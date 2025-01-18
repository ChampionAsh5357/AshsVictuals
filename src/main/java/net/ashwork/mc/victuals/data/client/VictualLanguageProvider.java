package net.ashwork.mc.victuals.data.client;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class VictualLanguageProvider extends LanguageProvider {

    public VictualLanguageProvider(PackOutput output) {
        super(output, AshsVictuals.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(VictualBlocks.APPLE_SEEDS, "Apple Seeds");
        this.add(VictualBlocks.Keys.APPLE_SAPLING, "Apple Sapling");
    }

    private void add(MutableComponent component, String name) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            this.add(translatable.getKey(), name);
        }
    }
}
