package net.ashwork.mc.victuals.util;

import net.ashwork.mc.victuals.AshsVictuals;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

public interface VictualHelper {

    static Component createTranslation(ResourceKey<? extends Registry<?>> key, String name) {
        return Component.translatable(Util.makeDescriptionId(key.location().toShortLanguageKey(), AshsVictuals.loc(name)));
    }
}
