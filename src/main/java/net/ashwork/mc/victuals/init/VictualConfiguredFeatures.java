package net.ashwork.mc.victuals.init;

import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public interface VictualConfiguredFeatures {

    ResourceKey<ConfiguredFeature<?, ?>> APPLE = key("apple");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return VictualHelper.createKey(Registries.CONFIGURED_FEATURE, name);
    }
}
