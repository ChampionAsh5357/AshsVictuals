package net.ashwork.mc.victuals.data.server.tags;

import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.tag.VictualBlockTags;
import net.ashwork.mc.victuals.tag.VictualItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class VictualItemTagsProvider extends ItemTagsProvider {

    public VictualItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, AshsVictuals.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.copy(VictualBlockTags.APPLE_LOGS, VictualItemTags.APPLE_LOGS);
    }
}
