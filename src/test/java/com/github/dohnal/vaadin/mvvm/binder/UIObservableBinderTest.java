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

package com.github.dohnal.vaadin.mvvm.binder;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ObservableBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Tests for {@link UIObservableBinder}
 *
 * @author dohnal
 */
@DisplayName("UI observable binder specification")
public class UIObservableBinderTest
{
    @Nested
    @DisplayName("When activable observable binder is created from observable")
    class WhenCreateFromObservable implements ReactiveBinderExtension
    {
        protected Consumer<Runnable> withUIAccess;
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> observable;
        protected PublishSubject<Throwable> errorSubject;

        protected ObservableBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            withUIAccess = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            errorSubject = PublishSubject.create();

            binder = new UIObservableBinder<>(withUIAccess, when(observable));
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Nested
        @DisplayName("When observable is bound to runnable")
        class WhenBindToRunnable
        {
            protected Runnable runnable;
            protected Disposable disposable;

            @BeforeEach
            void before()
            {
                runnable = Mockito.mock(Runnable.class);

                disposable = binder.then(runnable);
            }

            @Test
            @DisplayName("Runnable should not be called with UI access")
            public void testWithUIAccess()
            {
                Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
            }

            @Test
            @DisplayName("Runnable should not be called")
            public void testRunnable()
            {
                Mockito.verify(runnable, Mockito.never()).run();
            }

            @Nested
            @DisplayName("When observable emits value")
            class WhenObservableEmitsValue
            {
                @Test
                @DisplayName("Runnable should be called with UI access")
                public void testWithUIAccess()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }

                @Test
                @DisplayName("Runnable should be called")
                public void testRunnable()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    Mockito.verify(runnable).run();
                }
            }
        }

        @Nested
        @DisplayName("When observable is bound to consumer")
        class WhenBindToConsumer
        {
            protected Consumer<Integer> consumer;
            protected Disposable disposable;

            @BeforeEach
            @SuppressWarnings("unchecked")
            void before()
            {
                consumer = Mockito.mock(Consumer.class);

                disposable = binder.then(consumer);
            }

            @Test
            @DisplayName("Consumer should not be called with UI access")
            public void testWithUIAccess()
            {
                Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
            }

            @Test
            @DisplayName("Consumer should not be called")
            public void testConsumer()
            {
                Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
            }

            @Nested
            @DisplayName("When observable emits value")
            class WhenObservableEmitsValue
            {
                @Test
                @DisplayName("Runnable should be called with UI access")
                public void testWithUIAccess()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }

                @Test
                @DisplayName("Consumer should be called with correct value")
                public void testConsumer()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    Mockito.verify(consumer).accept(7);
                }
            }
        }

        @Nested
        @DisplayName("When observable is bound to observable supplier")
        class WhenBindToObservableSupplier
        {
            protected Supplier<Observable<?>> supplier;
            protected Disposable disposable;

            @BeforeEach
            @SuppressWarnings("unchecked")
            void before()
            {
                supplier = Mockito.mock(Supplier.class);

                disposable = binder.then(supplier);
            }

            @Test
            @DisplayName("Supplier should not be called with UI access")
            public void testWithUIAccess()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
            }

            @Test
            @DisplayName("Supplier should not be called")
            public void testSupplier()
            {
                Mockito.verify(supplier, Mockito.never()).get();
            }

            @Nested
            @DisplayName("When observable emits value")
            class WhenObservableEmitsValue
            {
                @Test
                @DisplayName("Supplier should not be called with UI access")
                public void testWithUIAccess()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
                }

                @Test
                @DisplayName("Supplier should be called ")
                public void testSupplier()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(supplier).get();
                }
            }
        }

        @Nested
        @DisplayName("When observable is bound to observable function")
        class WhenBindToObservableFunction
        {
            protected Function<Integer, Observable<?>> function;
            protected Disposable disposable;

            @BeforeEach
            @SuppressWarnings("unchecked")
            void before()
            {
                function = Mockito.mock(Function.class);

                disposable = binder.then(function);
            }

            @Test
            @DisplayName("Function should not be called with UI access")
            public void testWithUIAccess()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
            }

            @Test
            @DisplayName("Function should not be called")
            public void testFunction()
            {
                Mockito.verify(function, Mockito.never()).apply(Mockito.any());
            }

            @Nested
            @DisplayName("When observable emits value")
            class WhenObservableEmitsValue
            {
                @Test
                @DisplayName("Function should not be called with UI access")
                public void testWithUIAccess()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess, Mockito.never()).accept(Mockito.any());
                }

                @Test
                @DisplayName("Function should be called with correct value")
                public void testFunction()
                {
                    observable.onNext(5);
                    testScheduler.triggerActions();

                    Mockito.verify(function).apply(5);
                }
            }
        }
    }

    @Nested
    @DisplayName("When activable observable binder is created from IsObservable")
    class WhenCreateFromIsObservable extends WhenCreateFromObservable
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            withUIAccess = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            errorSubject = PublishSubject.create();

            binder = new UIObservableBinder<>(withUIAccess, when(() -> observable));
        }
    }
}
