package net.ashwork.mc.victuals.data.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.ashwork.mc.victuals.AshsVictuals;
import net.ashwork.mc.victuals.block.SeedSaplingBlock;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.init.VictualDataComponentTypes;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Supplier;

public class VictualModelProvider extends ModelProvider {

    public VictualModelProvider(PackOutput output) {
        super(output, AshsVictuals.ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        createSeedOrSaplingBlock(blockModels, VictualBlocks.APPLE_SEEDS, SeedSaplingBlock.AGE);
    }

    private static void createSeedOrSaplingBlock(BlockModelGenerators blockModels, Supplier<? extends Block> blockHolder, Property<Integer> ageProperty) {
        var block = blockHolder.get();

        // Block model
        PropertyDispatch propertydispatch = PropertyDispatch.property(ageProperty).generate(age ->
                        Variant.variant().with(
                                VariantProperties.MODEL,
                                blockModels.createSuffixedVariant(block, "_stage" + age, ModelTemplates.CROSS, TextureMapping::cross)
                        )
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(propertydispatch));

        // Item model
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.conditional(
                ItemModelUtils.hasComponent(VictualDataComponentTypes.SAPLING.get()),
                ItemModelUtils.plainModel(blockModels.createFlatItemModel(block.asItem())),
                ItemModelUtils.plainModel(blockModels.createFlatItemModelWithBlockTexture(block.asItem(), block, "_stage2"))
        ));
    }
}
