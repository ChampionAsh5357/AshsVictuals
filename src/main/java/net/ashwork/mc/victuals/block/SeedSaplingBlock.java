package net.ashwork.mc.victuals.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.victuals.init.VictualBlockTypes;
import net.ashwork.mc.victuals.init.VictualDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SeedSaplingBlock extends BushBlock implements BonemealableBlock {

    public static final Function<RecordCodecBuilder<SeedSaplingBlock, Properties>, MapCodec<SeedSaplingBlock>> CODEC_BUILDER =
            properties -> RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            TreeGrower.CODEC.fieldOf("tree").forGetter(sapling -> sapling.tree),
                            TagKey.codec(Registries.BLOCK).fieldOf("soil").forGetter(sapling -> sapling.soil),
                            properties
                    ).apply(instance, SeedSaplingBlock::new)
            );
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(6, 0, 6, 10, 3, 10),
            Block.box(5, 0, 5, 12, 8, 12),
            Block.box(2, 0, 2, 14, 13, 14),
            Block.box(0, 0, 0, 15, 15, 15)
    };

    protected final TreeGrower tree;
    protected final TagKey<Block> soil;

    public SeedSaplingBlock(TreeGrower tree, TagKey<Block> soil, Properties properties) {
        super(properties);
        this.tree = tree;
        this.soil = soil;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getItemInHand().has(VictualDataComponentTypes.SAPLING)
                ? this.defaultBlockState().setValue(AGE, 2)
                : super.getStateForPlacement(context);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return VictualBlockTypes.SEED_SAPLING.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(this.soil);
    }

    protected void advanceGrowth(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE).equals(AGE.getPossibleValues().getLast())) {
            this.tree.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        } else {
            level.setBlock(pos, state.cycle(AGE), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        // TODO: Check light information and add in mutation settings
        if (level.getMaxLocalRawBrightness(pos) >= 9 && random.nextInt(7) == 0) {
            this.advanceGrowth(state, level, pos, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        // TODO: Add ability for bonemeal to influence probability
        return random.nextFloat() < 0.45f;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.advanceGrowth(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
