package net.ashwork.mc.victuals.mixin;

import com.google.common.collect.ImmutableList;
import net.ashwork.mc.victuals.extension.StatePropertiesPredicateBuilderExtension;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(StatePropertiesPredicate.Builder.class)
public class StatePropertiesPredicateBuilderMixin implements StatePropertiesPredicateBuilderExtension {

    @Shadow
    @Final
    private ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers;


    @Override
    public StatePropertiesPredicate.Builder greaterOrEqualTo(Property<Integer> property, int minInclusive) {
        return this.rangedMatcher(property, fromInt(minInclusive), Optional.empty());
    }

    @Override
    public StatePropertiesPredicate.Builder lessThan(Property<Integer> property, int maxExclusive) {
        return this.rangedMatcher(property, Optional.empty(), fromInt(maxExclusive - 1));
    }

    @Override
    public StatePropertiesPredicate.Builder between(Property<Integer> property, int minInclusive, int maxExclusive) {
        return this.rangedMatcher(property, fromInt(minInclusive), fromInt(maxExclusive - 1));
    }

    @Unique
    private static Optional<String> fromInt(int value) {
        return Optional.of(String.valueOf(value));
    }

    @Unique
    private StatePropertiesPredicate.Builder rangedMatcher(Property<Integer> property, Optional<String> min, Optional<String> max) {
        this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(
                property.getName(),
                new StatePropertiesPredicate.RangedMatcher(min, max)
        ));
        return (StatePropertiesPredicate.Builder) (Object) this;
    }
}
