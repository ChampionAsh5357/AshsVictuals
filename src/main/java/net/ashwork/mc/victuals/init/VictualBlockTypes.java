package net.ashwork.mc.victuals.init;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.victuals.block.SeedSaplingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public class VictualBlockTypes {

    public static final Supplier<MapCodec<SeedSaplingBlock>> SEED_SAPLING = blockType("seed_sapling", properties -> RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TreeGrower.CODEC.fieldOf("tree").forGetter(SeedSaplingBlock::getTree),
                    TagKey.codec(Registries.BLOCK).fieldOf("soil").forGetter(SeedSaplingBlock::getSoil),
                    properties
            ).apply(instance, SeedSaplingBlock::new)
    ));

    private static <B extends Block> Supplier<MapCodec<B>> blockType(String name, Function<RecordCodecBuilder<B, BlockBehaviour.Properties>, MapCodec<B>> codec) {
        return VictualRegistrars.BLOCK_TYPE.register(name, () -> codec.apply(BlockBehaviour.propertiesCodec()));
    }

    static void register() {}
}
