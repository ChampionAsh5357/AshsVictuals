package net.ashwork.mc.victuals.data.server.tags;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.tag.VictualBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class VictualBlockTagsProvider extends BlockTagsProvider {

    public VictualBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AshsVictuals.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(VictualBlockTags.APPLE_LOGS).add(VictualBlocks.APPLE_LOG.get(), VictualBlocks.APPLE_WOOD.get());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(VictualBlockTags.APPLE_LOGS);
        this.tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(VictualBlocks.APPLE_LOG.get());
        this.tag(BlockTags.LEAVES).add(VictualBlocks.APPLE_LEAVES.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(VictualBlocks.APPLE_LEAVES.get());
        // TODO: Figure out how to handle the seed/sapling disparity
        this.tag(BlockTags.SAPLINGS).add(VictualBlocks.APPLE_SEEDS.get());
    }
}
