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

package org.vaadin.addons.reactive.binder;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.IsObservable;
import org.vaadin.addons.reactive.Property;
import org.vaadin.addons.reactive.PropertyBinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PropertyBinderDecorator}
 *
 * @author dohnal
 */
@DisplayName("Property binder decorator specification")
public class PropertyBinderDecoratorTest
{
    @Nested
    @DisplayName("When observable binder decorator is created")
    class WhenCreate
    {
        private PropertyBinder<Integer> binder;
        private PropertyBinderDecorator<Integer> decorator;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            binder = Mockito.mock(PropertyBinder.class);
            decorator = new PropertyBinderDecorator<>(binder);
        }

        @Test
        @DisplayName("Binder should not be changed")
        public void testBinder()
        {
            Mockito.verifyZeroInteractions(binder);
        }

        @Nested
        @DisplayName("When GetProperty is called")
        class WhenGetProperty
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Binder should be called")
            public void testBinder()
            {
                final Property<Integer> property = Mockito.mock(Property.class);

                Mockito.when(binder.getProperty()).thenReturn(property);

                assertEquals(property, decorator.getProperty());

                Mockito.verify(binder).getProperty();
            }
        }

        @Nested
        @DisplayName("When to is called with observable")
        class WhenToWithObservable
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final Observable<Integer> observable = Mockito.mock(Observable.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.to(observable)).thenReturn(disposable);

                assertEquals(disposable, decorator.to(observable));

                Mockito.verify(binder).to(observable);
            }
        }

        @Nested
        @SuppressWarnings("unchecked")
        @DisplayName("When to is called with isObservable")
        class WhenToWithIsObservable
        {
            @Test
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final IsObservable<Integer> isObservable = Mockito.mock(IsObservable.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.to(isObservable)).thenReturn(disposable);

                assertEquals(disposable, decorator.to(isObservable));

                Mockito.verify(binder).to(isObservable);
            }
        }
    }
}
