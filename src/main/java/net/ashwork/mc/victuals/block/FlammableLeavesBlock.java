package net.ashwork.mc.victuals.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.victuals.init.VictualBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class FlammableLeavesBlock extends LeavesBlock {

    public static final Function<RecordCodecBuilder<FlammableLeavesBlock, Properties>, MapCodec<FlammableLeavesBlock>> CODEC_BUILDER =
            properties -> RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.INT.fieldOf("ignite_odds").forGetter(log -> log.igniteOdds),
                            Codec.INT.fieldOf("burn_odds").forGetter(log -> log.burnOdds),
                            properties
                    ).apply(instance, FlammableLeavesBlock::new)
            );
    private final int igniteOdds;
    private final int burnOdds;

    public FlammableLeavesBlock(int igniteOdds, int burnOdds, Properties properties) {
        super(properties);
        this.igniteOdds = igniteOdds;
        this.burnOdds = burnOdds;
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return VictualBlockTypes.FLAMMABLE_LEAVES.get();
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
