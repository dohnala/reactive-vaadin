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

package com.github.dohnal.vaadin.reactive.property.factory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import com.github.dohnal.vaadin.reactive.property.SetValueSpecification;
import com.github.dohnal.vaadin.reactive.property.UpdateValueSpecification;
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
public interface CreateFromPropertiesWithCombinerSpecification extends
        SetValueSpecification,
        UpdateValueSpecification
{
    abstract class AbstractCreateFromNoPropertiesWithCombinerSpecification implements ReactivePropertyFactory
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

    abstract class AbstractCreateFromPropertiesWithCombinerSpecification implements ReactivePropertyFactory
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
            property.asObservable().test().assertNoValues();
        }

        @Nested
        @DisplayName("Set value specification")
        class SetValue extends AbstractSetValueReadOnlySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Update value specification")
        class UpdateValue extends AbstractUpdateValueReadOnlySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When source properties emit different value")
        class WhenSourceEmitValue
        {
            private final Integer FIRST_VALUE = 5;
            private final Integer SECOND_VALUE = 7;
            private final Integer THIRD_VALUE = 9;
            private final Integer COMBINED_VALUE = 21;

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                first.setValue(FIRST_VALUE);
                second.setValue(SECOND_VALUE);
                third.setValue(THIRD_VALUE);

                assertTrue(property.hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                first.setValue(FIRST_VALUE);
                second.setValue(SECOND_VALUE);
                third.setValue(THIRD_VALUE);

                assertEquals(COMBINED_VALUE, property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                testObserver.assertNoValues();
                first.setValue(FIRST_VALUE);
                second.setValue(SECOND_VALUE);
                third.setValue(THIRD_VALUE);

                testObserver.assertValue(COMBINED_VALUE);
            }
        }
    }
}