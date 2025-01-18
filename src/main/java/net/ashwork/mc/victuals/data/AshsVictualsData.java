package net.ashwork.mc.victuals.data;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.data.server.loot.VictualBlockLoot;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class AshsVictualsData {

    protected AshsVictualsData(IEventBus modBus, Class<? extends GatherDataEvent> dataClass) {
        // Add data event to the listener
        modBus.addListener(dataClass, event -> {
            // Construct datapack registry objects
            var registryBuilder = new RegistrySetBuilder();
            Map<ResourceKey<?>, List<ICondition>> conditions = new IdentityHashMap<>();
            this.registerDatapackObjects(registryBuilder, (key, condition) -> conditions.computeIfAbsent(key, k -> new ArrayList<>()).add(condition));
            var registries = event.createProvider((output, base) -> new DatapackBuiltinEntriesProvider(output, base, registryBuilder, conditions, Set.of(AshsVictuals.ID))).getRegistryProvider();

            // Handle data construction
            var packOutput = event.getGenerator().getPackOutput();
            var manager = new DataManager() {

                @Override
                public <T extends DataProvider> T createProvider(DataProvider.Factory<T> factory) {
                    return event.addProvider(factory.create(packOutput));
                }

                @Override
                public <T extends DataProvider> T createProvider(FactoryWithRegistries<T> factory) {
                    return event.addProvider(factory.create(packOutput, registries));
                }

                @Override
                public PackManager createBuiltinDatapack(String modId, String path) {
                    var pack = event.getGenerator().getBuiltinDatapack(true, modId, path);
                    return new PackManager() {

                        @Override
                        public <T extends DataProvider> T createProvider(DataProvider.Factory<T> factory) {
                            return pack.addProvider(factory);
                        }

                        @Override
                        public <T extends DataProvider> T createProvider(FactoryWithRegistries<T> factory) {
                            return pack.addProvider(output -> factory.create(output, event.getLookupProvider()));
                        }
                    };
                }
            };

            this.addProviders(manager);
        });
    }

    protected void registerDatapackObjects(RegistrySetBuilder builder, BiConsumer<ResourceKey<?>, ICondition> conditions) {}

    protected void addProviders(DataManager manager) {
        manager.createProvider((output, registries) -> new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(VictualBlockLoot::new, LootContextParamSets.BLOCK)
        ), registries));
    }

    @FunctionalInterface
    public interface FactoryWithRegistries<T extends DataProvider> {

        T create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries);
    }

    public interface PackManager {

        <T extends DataProvider> T createProvider(DataProvider.Factory<T> factory);

        <T extends DataProvider> T createProvider(FactoryWithRegistries<T> factory);
    }

    public interface DataManager extends PackManager {

        PackManager createBuiltinDatapack(String modId, String path);
    }
}
