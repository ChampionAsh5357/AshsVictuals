package net.ashwork.mc.victuals.init;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class VictualTreeGrowers {

    // TODO: Add in other variants of the apple tree
    public static final TreeGrower APPLE = new TreeGrower(
            "apple",
            Optional.empty(),
            Optional.of(VictualConfiguredFeatures.APPLE),
            Optional.empty()
    );
}
