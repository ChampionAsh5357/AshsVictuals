package net.ashwork.mc.victuals.data.server.loot;

import net.ashwork.mc.victuals.block.SeedSaplingBlock;
import net.ashwork.mc.victuals.init.VictualBlocks;
import net.ashwork.mc.victuals.init.VictualDataComponentTypes;
import net.ashwork.mc.victuals.init.VictualRegistrars;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Collections;
import java.util.function.Supplier;

public class VictualBlockLoot extends BlockLootSubProvider {

    public VictualBlockLoot(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return VictualRegistrars.getRegisteredBlocks();
    }

    @Override
    protected void generate() {
        this.dropSeedsOrSapling(VictualBlocks.APPLE_SEEDS, VictualBlocks.Keys.APPLE_SAPLING, SeedSaplingBlock.AGE);
    }

    private void dropSeedsOrSapling(Supplier<? extends Block> blockHolder, MutableComponent saplingName, Property<Integer> property) {
        var block = blockHolder.get();
        var saplingCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .greaterOrEqualTo(property, (property.getPossibleValues().getLast() + 1) / 2)
                );


        this.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(SetComponentsFunction.setComponent(VictualDataComponentTypes.SAPLING.get(), Unit.INSTANCE).when(saplingCondition))
                        .apply(SetComponentsFunction.setComponent(DataComponents.ITEM_NAME, saplingName).when(saplingCondition))
                )
        ));
    }
}
