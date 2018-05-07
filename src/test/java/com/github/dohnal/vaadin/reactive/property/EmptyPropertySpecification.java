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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Specification for {@link ReactiveProperty} created by {@link ReactivePropertyFactory#createProperty()}
 *
 * @author dohnal
 */
public interface EmptyPropertySpecification extends BasePropertySpecification
{
    abstract class WhenCreateEmptySpecification implements ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createEmpty()
        {
            property = createProperty();
        }

        @Test
        @DisplayName("HasValue should be false")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Test
        @DisplayName("IsReadOnly should be false")
        public void testIsReadOnly()
        {
            assertFalse(property.isReadOnly());
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
        @DisplayName("When different value is updated")
        class WhenUpdateDifferentValue extends WhenUpdateDifferentValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When same value is updated")
        class WhenUpdateSameValue extends WhenUpdateSameValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When property is subscribed after set values")
        class WhenSubscribeAfterSetValues
        {
            @BeforeEach
            void setValues()
            {
                property.setValue(5);
                property.setValue(7);
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(7);
            }
        }

        @Nested
        @DisplayName("When property change notifications are suppressed")
        class WhenSuppress extends WhenSuppressSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertNoValues();
                }
            }
        }

        @Nested
        @DisplayName("When property change notifications are suppressed while run action")
        class WhenSuppressAction extends WhenSuppressActionSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When property change notifications are delayed")
        class WhenDelay extends WhenDelaySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertNoValues();
                }
            }
        }

        @Nested
        @DisplayName("When property change notifications are delayed while run action")
        class WhenDelayAction extends WhenDelayActionSpecification
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
