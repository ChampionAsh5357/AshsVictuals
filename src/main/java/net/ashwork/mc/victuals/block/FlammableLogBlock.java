package net.ashwork.mc.victuals.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.victuals.init.VictualBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class FlammableLogBlock extends RotatedPillarBlock {

    public static final Function<RecordCodecBuilder<FlammableLogBlock, Properties>, MapCodec<FlammableLogBlock>> CODEC_BUILDER =
            properties -> RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.INT.fieldOf("ignite_odds").forGetter(log -> log.igniteOdds),
                            Codec.INT.fieldOf("burn_odds").forGetter(log -> log.burnOdds),
                            properties
                    ).apply(instance, FlammableLogBlock::new)
            );
    private final int igniteOdds;
    private final int burnOdds;

    public FlammableLogBlock(int igniteOdds, int burnOdds, Properties properties) {
        super(properties);
        this.igniteOdds = igniteOdds;
        this.burnOdds = burnOdds;
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return VictualBlockTypes.FLAMMABLE_LOG.get();
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.burnOdds;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.igniteOdds;
    }
}
