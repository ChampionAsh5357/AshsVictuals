package net.ashwork.mc.victuals.extension;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;

public interface StatePropertiesPredicateBuilderExtension {

    StatePropertiesPredicate.Builder greaterOrEqualTo(Property<Integer> property, int minInclusive);

    StatePropertiesPredicate.Builder lessThan(Property<Integer> property, int maxExclusive);

    StatePropertiesPredicate.Builder between(Property<Integer> property, int minInclusive, int maxExclusive);
}
