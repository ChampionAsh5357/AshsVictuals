package net.ashwork.mc.victuals.tag;

import net.ashwork.mc.victuals.util.VictualHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface VictualItemTags {

    TagKey<Item> APPLE_LOGS = key("logs/apple");

    private static TagKey<Item> key(String name) {
        return VictualHelper.createTagKey(Registries.ITEM, name);
    }
}
