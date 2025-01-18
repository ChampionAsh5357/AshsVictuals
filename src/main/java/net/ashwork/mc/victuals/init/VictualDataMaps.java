package net.ashwork.mc.victuals.init;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.datamap.CropMetadata;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class VictualDataMaps {

    public static final DataMapType<Item, CropMetadata> CROP_METADATA = DataMapType.builder(
            AshsVictuals.loc("crop_metadata"), Registries.ITEM, CropMetadata.CODEC
    ).build();

    private static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(CROP_METADATA);
    }

    static void register(IEventBus modBus) {
        modBus.addListener(VictualDataMaps::registerDataMaps);
    }
}
