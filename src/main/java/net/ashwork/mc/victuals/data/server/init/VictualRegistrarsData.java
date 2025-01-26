package net.ashwork.mc.victuals.data.server.init;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.function.BiConsumer;

public class VictualRegistrarsData {

    public static void init(RegistrySetBuilder builder, BiConsumer<ResourceKey<?>, ICondition> conditions) {
        builder.add(Registries.CONFIGURED_FEATURE, VictualConfiguredFeaturesData::register);
    }

    private static <T> RegistrySetBuilder add(RegistrySetBuilder builder, ResourceKey<? extends Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
        return builder.add(registry, bootstrap);
    }

    private static <T> RegistrySetBuilder add(RegistrySetBuilder builder, BiConsumer<ResourceKey<?>, ICondition> conditions, ResourceKey<? extends Registry<T>> registry, BiConsumer<BootstrapContext<T>, BiConsumer<ResourceKey<?>, ICondition>> bootstrap) {
        return builder.add(registry, ctx -> bootstrap.accept(ctx, conditions));
    }
}
