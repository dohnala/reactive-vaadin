/*
 * Copyright (c) 2018-present, reactive-mvvm Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import com.github.dohnal.vaadin.reactive.exceptions.ReadOnlyPropertyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty} created by
 * {@link ReactivePropertyFactory#createPropertyFrom(ReactiveProperty)}
 *
 * @author dohnal
 */
public interface PropertyFromPropertySpecification extends BasePropertySpecification
{
    abstract class WhenCreateFromEmptyPropertySpecification implements ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createFromEmptyProperty()
        {
            sourceProperty = createProperty();
            property = createPropertyFrom(sourceProperty);
        }

        @Test
        @DisplayName("IsReadOnly should be true")
        public void testIsReadOnly()
        {
            assertTrue(property.isReadOnly());
        }

        @Test
        @DisplayName("HasValue should be false")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Test
        @DisplayName("Value should be null")
        public void testValue()
        {
            assertNull(property.getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertNoValues();
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
        @DisplayName("When value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("ReadOnlyPropertyException should be thrown")
            public void testValue()
            {
                assertThrows(ReadOnlyPropertyException.class, () -> property.setValue(5));
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateValue
        {
            @Test
            @DisplayName("ReadOnlyPropertyException should be thrown")
            public void testValue()
            {
                assertThrows(ReadOnlyPropertyException.class, () -> property.updateValue(value -> 5));
            }
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void setInitialValue()
            {
                sourceProperty.setValue(5);
                sourceProperty.setValue(7);
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(7);
            }
        }
    }

    abstract class WhenCreateFromPropertyWithValueSpecification implements ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createPropertyFromPropertyWithValue()
        {
            sourceProperty = createProperty(5);
            property = createPropertyFrom(sourceProperty);
        }

        @Test
        @DisplayName("IsReadOnly should be true")
        public void testIsReadOnly()
        {
            assertTrue(property.isReadOnly());
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            assertTrue(property.hasValue());
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
        @DisplayName("When value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("ReadOnlyPropertyException should be thrown")
            public void testValue()
            {
                assertThrows(ReadOnlyPropertyException.class, () -> property.setValue(5));
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateValue
        {
            @Test
            @DisplayName("ReadOnlyPropertyException should be thrown")
            public void testValue()
            {
                assertThrows(ReadOnlyPropertyException.class, () -> property.updateValue(value -> 5));
            }
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void setInitialValue()
            {
                sourceProperty.setValue(5);
                sourceProperty.setValue(7);
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(7);
            }
        }
    }
}
