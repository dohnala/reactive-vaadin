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

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

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
 * {@link ReactivePropertyFactory#createPropertyFrom(Iterable, Function)}
 *
 * @author dohnal
 */
public interface PropertyFromPropertiesWithCombinerSpecification extends BasePropertySpecification
{
    abstract class WhenCreateFromNoPropertiesWithCombinerSpecification implements ReactivePropertyFactory
    {
        @Test
        @DisplayName("IllegalArgumentException should be thrown")
        public void testCreate()
        {
            assertThrows(IllegalArgumentException.class, () ->
                    createPropertyFrom(Collections.emptyList(), objects -> Arrays
                            .stream(Arrays.copyOf(objects, objects.length, Integer[].class))
                            .reduce(0, (acc, value) -> acc + value)));
        }
    }

    abstract class WhenCreateFromPropertiesWithCombinerSpecification implements ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> first;
        private ReactiveProperty<Integer> second;
        private ReactiveProperty<Integer> third;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createFromEmptyProperty()
        {
            first = createProperty();
            second = createProperty();
            third = createProperty();
            property = createPropertyFrom(Arrays.asList(first, second, third), objects -> Arrays
                    .stream(Arrays.copyOf(objects, objects.length, Integer[].class))
                    .reduce(0, (acc, value) -> acc + value));
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
                third.setValue(9);

                assertTrue(property.hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                assertEquals(new Integer(21), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                testObserver.assertValue(21);

                first.setValue(10);

                testObserver.assertValues(21, 26);

                second.setValue(14);

                testObserver.assertValues(21, 26, 33);

                third.setValue(18);

                testObserver.assertValues(21, 26, 33, 42);
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
                third.setValue(9);

                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                assertTrue(property.hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                assertEquals(new Integer(21), property.getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                testObserver.assertValue(21);

                first.setValue(5);
                second.setValue(7);
                third.setValue(9);

                testObserver.assertValue(21);
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
                third.setValue(9);

                first.setValue(10);
                second.setValue(14);
                third.setValue(18);
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(42);
            }
        }
    }
}
