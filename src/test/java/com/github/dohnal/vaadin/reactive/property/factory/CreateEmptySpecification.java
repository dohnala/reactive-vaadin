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

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import com.github.dohnal.vaadin.reactive.property.DelayActionSpecification;
import com.github.dohnal.vaadin.reactive.property.DelaySpecification;
import com.github.dohnal.vaadin.reactive.property.SetValueSpecification;
import com.github.dohnal.vaadin.reactive.property.SuppressActionSpecification;
import com.github.dohnal.vaadin.reactive.property.SuppressSpecification;
import com.github.dohnal.vaadin.reactive.property.UpdateValueSpecification;
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
public interface CreateEmptySpecification extends
        SetValueSpecification,
        UpdateValueSpecification,
        SuppressSpecification,
        SuppressActionSpecification,
        DelaySpecification,
        DelayActionSpecification
{
    abstract class AbstractCreateEmptySpecification implements ReactivePropertyFactory
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
            property.asObservable().test().assertNoValues();
        }

        @Nested
        @DisplayName("Set value specification")
        class SetValue extends AbstractSetValueSpecification
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
        class UpdateValue extends AbstractUpdateValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Suppress specification")
        class Suppress extends AbstractSuppressSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Suppress action specification")
        class SuppressAction extends AbstractSuppressActionSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Delay specification")
        class Delay extends AbstractDelaySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Delay action specification")
        class DelayAction extends AbstractDelayActionSpecification
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
