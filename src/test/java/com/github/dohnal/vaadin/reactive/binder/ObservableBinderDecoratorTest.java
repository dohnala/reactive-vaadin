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

import java.util.function.Consumer;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ObservableBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ObservableBinderDecorator}
 *
 * @author dohnal
 */
@DisplayName("Observable binder decorator specification")
public class ObservableBinderDecoratorTest
{
    @Nested
    @DisplayName("When observable binder decorator is created")
    class WhenCreate
    {
        private ObservableBinder<Integer> binder;
        private ObservableBinderDecorator<Integer> decorator;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            binder = Mockito.mock(ObservableBinder.class);
            decorator = new ObservableBinderDecorator<>(binder);
        }

        @Test
        @DisplayName("Binder should not be changed")
        public void testBinder()
        {
            Mockito.verifyZeroInteractions(binder);
        }

        @Nested
        @DisplayName("When then is called with consumer")
        class WhenThenWithConsumer
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final Consumer<Integer> consumer = Mockito.mock(Consumer.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.then(consumer)).thenReturn(disposable);

                assertEquals(disposable, decorator.then(consumer));

                Mockito.verify(binder).then(consumer);
            }
        }

        @Nested
        @DisplayName("When then is called with runnable")
        class WhenThenWithRunnable
        {
            @Test
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final Runnable runnable = Mockito.mock(Runnable.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.then(runnable)).thenReturn(disposable);

                assertEquals(disposable, decorator.then(runnable));

                Mockito.verify(binder).then(runnable);
            }
        }

        @Nested
        @DisplayName("When then is called with observable function")
        class WhenThenWithObservableFunction
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Binder should be called with correct argument")
            public void testBinder()
            {
                final Function<Integer, Observable<?>> function = Mockito.mock(Function.class);
                final Disposable disposable = Mockito.mock(Disposable.class);

                Mockito.when(binder.then(function)).thenReturn(disposable);

                assertEquals(disposable, decorator.then(function));

                Mockito.verify(binder).then(function);
            }
        }
    }
}
