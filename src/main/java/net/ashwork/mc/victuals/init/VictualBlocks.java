package net.ashwork.mc.victuals.init;

import net.ashwork.mc.victuals.block.SeedSaplingBlock;
import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class VictualBlocks {

    // TODO: Look into whether the map colors make sense
    // TODO: Add custom textures
    public static final DeferredBlock<RotatedPillarBlock> APPLEWOOD_LOG = blockWithSimpleItem("applewood_log", RotatedPillarBlock::new, logProperties(MapColor.WOOD, MapColor.PODZOL, SoundType.WOOD));
    public static final DeferredBlock<RotatedPillarBlock> APPLEWOOD = blockWithSimpleItem("applewood", RotatedPillarBlock::new, logProperties(MapColor.WOOD, SoundType.WOOD));
    // TODO: Change to custom leaves implementation for crop metadata logic
    // TODO: Add custom textures
    public static final DeferredBlock<LeavesBlock> APPLE_LEAVES = blockWithSimpleItem("apple_leaves", LeavesBlock::new, leavesProperties(SoundType.GRASS));

    // TODO: Change grower and tag to one specific for apple seeds
    public static final DeferredBlock<SeedSaplingBlock> APPLE_SEEDS = blockWithSimpleItem("apple_seeds", properties ->
            new SeedSaplingBlock(
                    TreeGrower.OAK, BlockTags.DIRT,
                    properties.mapColor(MapColor.PLANT)
                            .noCollission().randomTicks().instabreak().offsetType(BlockBehaviour.OffsetType.XZ)
                            .sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)
            )
    );

    private static <B extends Block> DeferredBlock<B> blockWithSimpleItem(String name, Function<BlockBehaviour.Properties, B> factory, UnaryOperator<BlockBehaviour.Properties> properties) {
        return blockWithSimpleItem(name, props -> factory.apply(properties.apply(props)));
    }

    private static <B extends Block> DeferredBlock<B> blockWithSimpleItem(String name, Function<BlockBehaviour.Properties, B> factory) {
        var block = VictualRegistrars.BLOCK.registerBlock(name, factory);
        VictualRegistrars.ITEM.registerSimpleBlockItem(block);
        return block;
    }

    private static final BlockBehaviour.StatePredicate NEVER_STATE_PREDICATE = (state, level, pos) -> false;

    private static UnaryOperator<BlockBehaviour.Properties> logProperties(MapColor color, SoundType sound) {
        return logProperties(state -> color, sound);
    }

    private static UnaryOperator<BlockBehaviour.Properties> logProperties(MapColor sideColor, MapColor topColor, SoundType sound) {
        return logProperties(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? sideColor : topColor, sound);
    }

    private static UnaryOperator<BlockBehaviour.Properties> logProperties(Function<BlockState, MapColor> color, SoundType sound) {
        return properties -> properties
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2f)
                .sound(sound)
                .ignitedByLava();
    }

    private static UnaryOperator<BlockBehaviour.Properties> leavesProperties(SoundType sound) {
        return properties -> properties
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(sound)
                .noOcclusion()
                // TODO: Change to tag implementation
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating(NEVER_STATE_PREDICATE)
                .isViewBlocking(NEVER_STATE_PREDICATE)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(NEVER_STATE_PREDICATE);
    }

    static void register() {}

    public interface Keys {

        Component APPLE_SAPLING = VictualHelper.createTranslation(Registries.BLOCK, "apple_sapling");
    }
}
