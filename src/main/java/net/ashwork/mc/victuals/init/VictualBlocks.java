package net.ashwork.mc.victuals.init;

import net.ashwork.mc.victuals.block.SeedSaplingBlock;
import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class VictualBlocks {

    // TODO: Change grower and tag to one specific for apple seeds
    // TODO: Change stage 2 and 3 texture to apple specific textures
    // TODO: Change apple seeds item texture to apple specific texture
    public static final DeferredBlock<SeedSaplingBlock> APPLE_SEEDS = blockWithSimpleItem("apple_seeds", properties ->
            new SeedSaplingBlock(TreeGrower.OAK, BlockTags.DIRT, properties.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY))
    );

    private static <B extends Block> DeferredBlock<B> blockWithSimpleItem(String name, Function<BlockBehaviour.Properties, B> factory) {
        var block = VictualRegistrars.BLOCK.registerBlock(name, factory);
        VictualRegistrars.ITEM.registerSimpleBlockItem(block);
        return block;
    }

    static void register() {}

    public interface Keys {

        MutableComponent APPLE_SAPLING = VictualHelper.createTranslation(Registries.BLOCK, "apple_sapling");
    }
}
