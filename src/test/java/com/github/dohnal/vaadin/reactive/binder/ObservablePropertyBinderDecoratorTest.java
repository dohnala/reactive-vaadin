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

package com.github.dohnal.vaadin.reactive.binder;

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ObservablePropertyBinderDecorator}
 *
 * @author dohnal
 */
@DisplayName("Observable property binder decorator specification")
public class ObservablePropertyBinderDecoratorTest
{
    @Nested
    @DisplayName("When observable property binder decorator is created")
    class WhenCreate
    {
        private ObservablePropertyBinder<Integer> binder;
        private ObservablePropertyBinderDecorator<Integer> decorator;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            binder = Mockito.mock(ObservablePropertyBinder.class);
            decorator = new ObservablePropertyBinderDecorator<>(binder);
        }

        @Test
        @DisplayName("Binder should not be changed")
        public void testBinder()
        {
            Mockito.verifyZeroInteractions(binder);
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

        @Nested
        @SuppressWarnings("unchecked")
        @DisplayName("When to is called with observable property")
        class WhenToWithObservableProperty
        {
            @Test
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final ObservableProperty<Integer> property = Mockito.mock(ObservableProperty.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.to(property)).thenReturn(disposable);

                assertEquals(disposable, decorator.to(property));

                Mockito.verify(binder).to(property);
            }
        }
    }
}
