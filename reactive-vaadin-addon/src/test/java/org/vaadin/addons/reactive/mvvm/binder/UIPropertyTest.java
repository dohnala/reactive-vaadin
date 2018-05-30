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

package org.vaadin.addons.reactive.mvvm.binder;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.Property;

/**
 * Tests for {@link UIProperty}
 *
 * @author dohnal
 */
@DisplayName("UI property specification")
public class UIPropertyTest
{
    @Nested
    @DisplayName("When UI property is created")
    class WhenCreate
    {
        private Consumer<Runnable> withUIAccess;
        private Property<Integer> property;
        private UIProperty<Integer> uiProperty;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            withUIAccess = Mockito.mock(Consumer.class);
            property = Mockito.mock(Property.class);
            uiProperty = new UIProperty<>(withUIAccess, property);
        }

        @Nested
        @DisplayName("When UI property value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Property value should be set with UI access")
            public void testWithUIAccess()
            {
                uiProperty.setValue(7);

                Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
            }

            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                uiProperty.setValue(7);

                Mockito.verify(withUIAccess).accept(captor.capture());

                captor.getValue().run();

                Mockito.verify(property).setValue(7);
            }
        }
    }
}
