package net.ashwork.mc.victuals.extension;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;

public interface StatePropertiesPredicateBuilderExtension {

    default StatePropertiesPredicate.Builder greaterOrEqualTo(Property<Integer> property, int minInclusive) {
        return null;
    }

    default StatePropertiesPredicate.Builder lessThan(Property<Integer> property, int maxExclusive) {
        return null;
    }

    default StatePropertiesPredicate.Builder between(Property<Integer> property, int minInclusive, int maxExclusive) {
        return null;
    }
}
