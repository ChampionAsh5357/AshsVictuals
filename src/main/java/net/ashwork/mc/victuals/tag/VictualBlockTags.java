package net.ashwork.mc.victuals.tag;

import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface VictualBlockTags {

    TagKey<Block> APPLE_LOGS = key("logs/apple");

    private static TagKey<Block> key(String name) {
        return VictualHelper.createTagKey(Registries.BLOCK, name);
    }
}
