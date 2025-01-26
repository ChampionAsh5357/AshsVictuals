package net.ashwork.mc.victuals.data.client;

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
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Supplier;

public class VictualModelProvider extends ModelProvider {

    public VictualModelProvider(PackOutput output) {
        super(output, AshsVictuals.ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.woodProvider(VictualBlocks.APPLE_LOG.get())
                .logWithHorizontal(VictualBlocks.APPLE_LOG.get())
                .wood(VictualBlocks.APPLE_WOOD.get());
        blockModels.createTintedLeaves(VictualBlocks.APPLE_LEAVES.get(), TexturedModel.LEAVES, 0x74AC47);
        createSeedOrSaplingBlock(blockModels, VictualBlocks.APPLE_SEEDS, SeedSaplingBlock.AGE);
    }

    private static void createSeedOrSaplingBlock(BlockModelGenerators blockModels, Supplier<? extends Block> blockHolder, Property<Integer> ageProperty) {
        var block = blockHolder.get();

        // Block model
        PropertyDispatch propertydispatch = PropertyDispatch.property(ageProperty).generate(age ->
                        Variant.variant().with(
                                VariantProperties.MODEL,
                                blockModels.createSuffixedVariant(
                                        block, "_stage" + age,
                                        ModelTemplates.CROSS.extend().renderType("cutout").build(), TextureMapping::cross
                                )
                        )
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block).with(propertydispatch));

        // Item model
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.conditional(
                ItemModelUtils.hasComponent(VictualDataComponentTypes.SAPLING.get()),
                ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
                        ModelLocationUtils.decorateItemModelLocation(AshsVictuals.ID + ":apple_sapling"),
                        TextureMapping.layer0(TextureMapping.getBlockTexture(block, "_stage2")),
                        blockModels.modelOutput
                )),
                ItemModelUtils.plainModel(blockModels.createFlatItemModel(block.asItem()))
        ));
    }
}
