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
 * Specification for {@link ReactiveProperty} created by {@link ReactiveProperty#withValue(Object)}
 *
 * @author dohnal
 */
public interface PropertyWithValueSpecification extends BasePropertySpecification
{
    abstract class WhenCreateWithValueSpecification
    {
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createWithValue()
        {
            property = ReactiveProperty.withValue(5);
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            assertEquals(new Integer(5), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit default value")
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
