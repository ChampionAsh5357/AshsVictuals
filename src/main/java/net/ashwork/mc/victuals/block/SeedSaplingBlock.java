package net.ashwork.mc.victuals.block;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.victuals.init.VictualBlockTypes;
import net.ashwork.mc.victuals.init.VictualDataComponentTypes;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

public class SeedSaplingBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    protected final TreeGrower tree;
    protected final TagKey<Block> soil;

    public SeedSaplingBlock(TreeGrower tree, TagKey<Block> soil, Properties properties) {
        super(properties);
        this.tree = tree;
        this.soil = soil;
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
        // TODO: Implement stage increase
        if (!level.isAreaLoaded(pos, 1)) return;
        if (random.nextInt(7) == 0) {
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
        // TODO: Advance to next stage
        this.advanceGrowth(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public TagKey<Block> getSoil() {
        return this.soil;
    }

    public TreeGrower getTree() {
        return this.tree;
    }
}
