package net.ashwork.mc.victuals.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;

import java.util.function.Supplier;

public class VictualDataComponentTypes {

    public static final Supplier<DataComponentType<Unit>> SAPLING = unitType("sapling");

    private static Supplier<DataComponentType<Unit>> unitType(String name) {
        return VictualRegistrars.DATA_COMPONENT_TYPE.registerComponentType(name, builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
    }

    static void register() {}
}
