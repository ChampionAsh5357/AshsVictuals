package net.ashwork.mc.victuals.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

// TODO: Add more settings
public record CropMetadata(Deviation<Integer> nutrition, Deviation<Float> saturation, Deviation<Float> consumeSeconds) {

    public static final Codec<CropMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Deviation.codec(Codec.INT).fieldOf("nutrition").forGetter(CropMetadata::nutrition),
            Deviation.codec(Codec.FLOAT).fieldOf("saturation").forGetter(CropMetadata::saturation),
            Deviation.codec(Codec.FLOAT).fieldOf("consume_seconds").forGetter(CropMetadata::consumeSeconds)
    ).apply(instance, CropMetadata::new));

    public record Deviation<T>(T base, T amountPerDeviation) {

        public static <T> Codec<Deviation<T>> codec(Codec<T> codec) {
            return Codec.withAlternative(
                    RecordCodecBuilder.create((RecordCodecBuilder.Instance<Deviation<T>> instance) -> instance.group(
                            codec.fieldOf("base").forGetter(Deviation::base),
                            codec.fieldOf("amount_per_deviation").forGetter(Deviation::amountPerDeviation)
                    ).apply(instance, Deviation::new)),
                    codec.listOf(2, 2).xmap(
                            list -> new Deviation<>(list.getFirst(), list.get(1)),
                            deviation -> List.of(deviation.base(), deviation.amountPerDeviation())
                    )
            );
        }
    }
}
