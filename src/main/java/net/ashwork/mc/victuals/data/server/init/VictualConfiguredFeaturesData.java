package net.ashwork.mc.victuals.data.server.init;

import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.init.VictualConfiguredFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class VictualConfiguredFeaturesData {

    static void register(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        // TODO: Handle specifically for apple tree instead of mimicking oak
        FeatureUtils.register(
                ctx, VictualConfiguredFeatures.APPLE, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(VictualBlocks.APPLE_LOG.get()),
                        new StraightTrunkPlacer(4, 2, 0),
                        BlockStateProvider.simple(VictualBlocks.APPLE_LEAVES.get()),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).ignoreVines().build()
        );
    }
}
