package net.ashwork.mc.victuals.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.victuals.AshsVictuals;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class VictualRegistrars {

    private static final List<RegistryInit> REGISTRARS = new ArrayList<>();

    static final DeferredRegister.Blocks BLOCK = registrar(DeferredRegister::createBlocks, VictualBlocks::register);
    static final DeferredRegister.DataComponents DATA_COMPONENT_TYPE = registrar(id -> DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, id), VictualDataComponentTypes::register);
    static final DeferredRegister.Items ITEM = registrar(DeferredRegister::createItems, VictualItems::register);
    static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPE = registrar(Registries.BLOCK_TYPE, VictualBlockTypes::register);

    public static void init(IEventBus modBus) {
        REGISTRARS.forEach(registry -> {
            registry.registrar().register(modBus);
            registry.registerEntries().run();
        });

        VictualDataMaps.register(modBus);
    }

    private static <T> DeferredRegister<T> registrar(ResourceKey<? extends Registry<T>> registry, Runnable registerEntries) {
        return registrar(id -> DeferredRegister.create(registry, id), registerEntries);
    }

    private static <T, D extends DeferredRegister<T>> D registrar(Function<String, D> factory, Runnable registerEntries) {
        var result = factory.apply(AshsVictuals.ID);
        REGISTRARS.add(new RegistryInit(result, registerEntries));
        return result;
    }

    public static Iterable<Block> getRegisteredBlocks() {
        return BLOCK.getEntries().stream().map(e -> (Block) e.value()).toList();
    }

    private record RegistryInit(DeferredRegister<?> registrar, Runnable registerEntries) {}
}
