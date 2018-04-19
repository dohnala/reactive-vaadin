package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty} created by {@link ReactiveProperty#fromProperty(ReactiveProperty)}
 *
 * @author dohnal
 */
public interface PropertyFromPropertyWithValueSpecification extends BasePropertySpecification
{
    abstract class WhenCreateFromPropertyWithValueSpecification
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createPropertyFromObservable()
        {
            sourceProperty = ReactiveProperty.withValue(5);
            property = ReactiveProperty.fromProperty(sourceProperty);
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            assertEquals(new Integer(5), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertValue(5);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            assertTrue(property.hasValue());
        }

        @Nested
        @DisplayName("When source property emits different value")
        class WhenSourcePropertyEmitsDifferentValue extends WhenSourceEmitsDifferentValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }

        @Nested
        @DisplayName("When source property emits same value")
        class WhenSourcePropertyEmitsSameValue extends WhenSourceEmitsSameValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue extends WhenSetDifferentValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When same value is set")
        class WhenSetSameValue extends WhenSetSameValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe extends WhenSubscribeSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }
    }
}
