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

import java.util.function.BiFunction;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import com.github.dohnal.vaadin.reactive.exceptions.ReadOnlyPropertyException;
import io.reactivex.observers.TestObserver;
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
 * {@link ReactivePropertyFactory#createPropertyFrom(ReactiveProperty, ReactiveProperty, BiFunction)}
 *
 * @author dohnal
 */
public interface PropertyFromTwoPropertiesWithCombinerSpecification extends BasePropertySpecification
{
    abstract class WhenCreateFromTwoPropertiesWithCombinerSpecification implements ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> first;
        private ReactiveProperty<Integer> second;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createFromEmptyProperty()
        {
            first = createProperty();
            second = createProperty();
            property = createPropertyFrom(first, second, (x, y) -> x + y);
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
        @DisplayName("When source properties emits different value")
        class WhenSourcePropertyEmitsDifferentValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                first.setValue(5);
                second.setValue(7);

                first.setValue(10);
                second.setValue(14);

                assertTrue(property.hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                first.setValue(5);
                second.setValue(7);

                first.setValue(10);
                second.setValue(14);

                assertEquals(new Integer(24), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                first.setValue(5);
                second.setValue(7);

                testObserver.assertValue(12);

                first.setValue(10);

                testObserver.assertValues(12, 17);

                second.setValue(14);

                testObserver.assertValues(12, 17, 24);
            }
        }

        @Nested
        @DisplayName("When source property emits same value")
        class WhenSourcePropertyEmitsSameValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                first.setValue(5);
                second.setValue(7);

                first.setValue(5);
                second.setValue(7);

                assertTrue(property.hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                first.setValue(5);
                second.setValue(7);

                first.setValue(5);
                second.setValue(7);

                assertEquals(new Integer(12), property.getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                first.setValue(5);
                second.setValue(7);

                testObserver.assertValue(12);

                first.setValue(5);
                second.setValue(7);

                testObserver.assertValue(12);
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
                first.setValue(5);
                second.setValue(7);

                first.setValue(10);
                second.setValue(14);
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(24);
            }
        }
    }
}
