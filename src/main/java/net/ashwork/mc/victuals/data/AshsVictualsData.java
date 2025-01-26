package net.ashwork.mc.victuals.data;

import net.ashwork.mc.victuals.data.server.init.VictualRegistrarsData;
import net.ashwork.mc.victuals.data.server.loot.VictualBlockLoot;
import net.ashwork.mc.victuals.data.server.tags.VictualBlockTagsProvider;
import net.ashwork.mc.victuals.data.server.tags.VictualItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AshsVictualsData {

    protected AshsVictualsData(IEventBus modBus, Class<? extends GatherDataEvent> dataClass) {
        // Add data event to the listener
        modBus.addListener(dataClass, event -> {
            // Construct datapack registry objects
            var registryBuilder = new RegistrySetBuilder();
            Map<ResourceKey<?>, List<ICondition>> conditions = new IdentityHashMap<>();
            VictualRegistrarsData.init(registryBuilder, (key, condition) -> conditions.computeIfAbsent(key, k -> new ArrayList<>()).add(condition));
            event.createDatapackRegistryObjects(registryBuilder, conditions);

            // Handle data construction
            var packOutput = event.getGenerator().getPackOutput();
            var manager = new DataManager() {

                @Override
                public <T extends DataProvider> T createProvider(DataProvider.Factory<T> factory) {
                    return event.createProvider(factory::create);
                }

                @Override
                public <T extends DataProvider> T createProvider(FactoryWithRegistries<T> factory) {
                    return event.createProvider(factory::create);
                }

                @Override
                public <B extends TagsProvider<Block>, I extends TagsProvider<Item>> void createBlockAndItemTags(FactoryWithRegistries<B> blockTags, ItemTagsProvider<I> itemTags) {
                    event.createBlockAndItemTags(blockTags::create, itemTags::create);
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


                        @Override
                        public <B extends TagsProvider<Block>, I extends TagsProvider<Item>> void createBlockAndItemTags(FactoryWithRegistries<B> blockTags, ItemTagsProvider<I> itemTags) {
                            var tagLookup = this.createProvider(blockTags).contentsGetter();
                            this.createProvider((output, registries) -> itemTags.create(output, registries, tagLookup));
                        }
                    };
                }
            };

            this.addProviders(manager);
        });
    }

    protected void addProviders(DataManager manager) {
        manager.createProvider((output, registries) -> new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(VictualBlockLoot::new, LootContextParamSets.BLOCK)
        ), registries));
        manager.createBlockAndItemTags(VictualBlockTagsProvider::new, VictualItemTagsProvider::new);
    }

    @FunctionalInterface
    public interface FactoryWithRegistries<T extends DataProvider> {

        T create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries);
    }

    @FunctionalInterface
    public interface ItemTagsProvider<I extends TagsProvider<Item>> {

        I create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags);
    }

    public interface PackManager {

        <T extends DataProvider> T createProvider(DataProvider.Factory<T> factory);

        <T extends DataProvider> T createProvider(FactoryWithRegistries<T> factory);

        <B extends TagsProvider<Block>, I extends TagsProvider<Item>> void createBlockAndItemTags(FactoryWithRegistries<B> blockTags, ItemTagsProvider<I> itemTags);
    }

    public interface DataManager extends PackManager {

        PackManager createBuiltinDatapack(String modId, String path);
    }
}
