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

package com.github.dohnal.vaadin.mvvm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.Property;
import com.vaadin.data.HasItems;
import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link ComponentProperties}
 *
 * @author dohnal
 */
@DisplayName("Component properties specification")
public class ComponentPropertiesTest implements ComponentProperties
{
    @Nested
    @DisplayName("When visibleOf property is created with component")
    class WhenCreateVisibleOfComponent
    {
        private Component component;
        private Property<Boolean> property;

        @BeforeEach
        protected void create()
        {
            component = Mockito.mock(Component.class);
            property = visibleOf(component);
        }

        @Test
        @DisplayName("Component should not be changed")
        public void testComponent()
        {
            Mockito.verifyZeroInteractions(component);
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Component's setVisible should be called with correct value")
            public void testComponent()
            {
                property.setValue(false);

                Mockito.verify(component).setVisible(false);
            }
        }
    }

    @Nested
    @DisplayName("When enabledOf property is created with component")
    class WhenCreateEnabledOfComponent
    {
        private Component component;
        private Property<Boolean> property;

        @BeforeEach
        protected void create()
        {
            component = Mockito.mock(Component.class);
            property = enabledOf(component);
        }

        @Test
        @DisplayName("Component should not be changed")
        public void testComponent()
        {
            Mockito.verifyZeroInteractions(component);
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Component's setEnabled should be called with correct value")
            public void testComponent()
            {
                property.setValue(false);

                Mockito.verify(component).setEnabled(false);
            }
        }
    }

    @Nested
    @DisplayName("When readOnlyOf property is created with field")
    class WhenCreateReadOnlyOfField
    {
        private HasValue<?> field;
        private Property<Boolean> property;

        @BeforeEach
        protected void create()
        {
            field = Mockito.mock(HasValue.class);
            property = readOnlyOf(field);
        }

        @Test
        @DisplayName("Field should not be changed")
        public void testField()
        {
            Mockito.verifyZeroInteractions(field);
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Field's readOnly should be called with correct value")
            public void testField()
            {
                property.setValue(false);

                Mockito.verify(field).setReadOnly(false);
            }
        }
    }

    @Nested
    @DisplayName("When valueOf property is created with field")
    class WhenCreateValueOfField
    {
        private TextField field;
        private ObservableProperty<String> property;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            field = new TextField();
            property = valueOf(field);
        }

        @Test
        @DisplayName("Property should not emit any value")
        public void testProperty()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetPropertyValue
        {
            protected final String VALUE = "value";

            @Test
            @DisplayName("Field's value should be correct")
            public void testField()
            {
                property.setValue(VALUE);

                assertEquals(VALUE, field.getValue());
            }

            @Test
            @DisplayName("Property should emit correct value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> property.setValue(VALUE))
                        .assertValue(VALUE);
            }
        }

        @Nested
        @DisplayName("When field value is set")
        class WhenSetFieldValue
        {
            protected final String VALUE = "value";

            @Test
            @DisplayName("Field's value should be correct")
            public void testField()
            {
                field.setValue(VALUE);

                assertEquals(VALUE, field.getValue());
            }

            @Test
            @DisplayName("Property should emit correct value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> field.setValue(VALUE))
                        .assertValue(VALUE);
            }
        }
    }

    @Nested
    @DisplayName("When valueOfNullable property is created with field")
    class WhenCreateValueOfNullableField
    {
        private ComboBox<Integer> field;
        private ObservableProperty<Optional<Integer>> property;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            field = new ComboBox();
            field.setItems(Arrays.asList(1, 2, 3, 4, 5));
            property = valueOfNullable(field);
        }

        @Test
        @DisplayName("Property should not emit any value")
        public void testProperty()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetPropertyValue
        {
            protected final Integer VALUE = 5;

            @Test
            @DisplayName("Field's value should be correct")
            public void testField()
            {
                property.setValue(Optional.of(VALUE));

                assertEquals(VALUE, field.getValue());
            }

            @Test
            @DisplayName("Property should emit correct value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> property.setValue(Optional.of(VALUE)))
                        .assertValue(Optional.of(VALUE));
            }
        }

        @Nested
        @DisplayName("When property value is set to null")
        class WhenSetNullPropertyValue
        {
            @BeforeEach
            protected void setInitialValue()
            {
                field.setValue(3);
            }

            @Test
            @DisplayName("Field's value should be null")
            public void testField()
            {
                property.setValue(Optional.empty());

                assertNull(field.getValue());
            }

            @Test
            @DisplayName("Property should emit empty value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> property.setValue(Optional.empty()))
                        .assertValue(Optional.empty());
            }
        }

        @Nested
        @DisplayName("When field value is set")
        class WhenSetFieldValue
        {
            protected final Integer VALUE = 5;

            @Test
            @DisplayName("Field's value should be correct")
            public void testField()
            {
                field.setValue(VALUE);

                assertEquals(VALUE, field.getValue());
            }

            @Test
            @DisplayName("Property should emit correct value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> field.setValue(VALUE))
                        .assertValue(Optional.of(VALUE));
            }
        }

        @Nested
        @DisplayName("When field value is set to null")
        class WhenSetNullFieldValue
        {
            @BeforeEach
            protected void setInitialValue()
            {
                field.setValue(3);
            }

            @Test
            @DisplayName("Field's value should be null")
            public void testField()
            {
                field.setValue(null);

                assertNull(field.getValue());
            }

            @Test
            @DisplayName("Property should emit empty value")
            public void testProperty()
            {
                property.asObservable().test()
                        .perform(() -> field.setValue(null))
                        .assertValue(Optional.empty());
            }
        }
    }

    @Nested
    @DisplayName("When valueOf property is created with progress bar")
    class WhenCreateValueOfProgressBar
    {
        private ProgressBar progressBar;
        private Property<Float> property;

        @BeforeEach
        protected void create()
        {
            progressBar = Mockito.mock(ProgressBar.class);
            property = valueOf(progressBar);
        }

        @Test
        @DisplayName("Progress bar should not be changed")
        public void testProgressBar()
        {
            Mockito.verifyZeroInteractions(progressBar);
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Progress bar's setValue should be called with correct value")
            public void testProgressBar()
            {
                property.setValue(0.5f);

                Mockito.verify(progressBar).setValue(0.5f);
            }
        }
    }

    @Nested
    @DisplayName("When itemsOf property is created with component")
    class WhenCreateItemsOfComponent
    {
        private HasItems<Integer> component;
        private Property<Collection<Integer>> property;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            component = Mockito.mock(HasItems.class);
            property = itemsOf(component);
        }

        @Test
        @DisplayName("Field should not be changed")
        public void testComponent()
        {
            Mockito.verifyZeroInteractions(component);
        }

        @Nested
        @DisplayName("When property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Progress bar's setItems should be called with correct value")
            public void testComponent()
            {
                final List<Integer> values = Arrays.asList(0, 1, 2);

                property.setValue(values);

                Mockito.verify(component).setItems(values);
            }
        }
    }
}
