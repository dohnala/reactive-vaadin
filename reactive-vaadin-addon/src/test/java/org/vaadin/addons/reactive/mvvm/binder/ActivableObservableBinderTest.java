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

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.ObservableBinder;
import org.vaadin.addons.reactive.ReactiveBinderExtension;
import org.vaadin.addons.reactive.activable.CompositeActivable;

/**
 * Tests for {@link ActivableObservableBinder}
 *
 * @author dohnal
 */
@DisplayName("Activable observable binder specification")
public class ActivableObservableBinderTest
{
    @Nested
    @DisplayName("When activable observable binder is created from observable")
    class WhenCreateFromObservable implements ReactiveBinderExtension
    {
        protected CompositeActivable compositeActivable;
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> observable;
        protected PublishSubject<Throwable> errorSubject;

        protected ObservableBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            compositeActivable = new CompositeActivable();
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            errorSubject = PublishSubject.create();

            binder = new ActivableObservableBinder<>(compositeActivable, when(observable));
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
                @DisplayName("Runnable should not be called")
                public void testRunnable()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(runnable, Mockito.never()).run();
                }
            }

            @Nested
            @DisplayName("When activated")
            class WhenActivate
            {
                @BeforeEach
                void before()
                {
                    compositeActivable.activate();
                }

                @Nested
                @DisplayName("When observable emits value")
                class WhenObservableEmitsValue
                {
                    @Test
                    @DisplayName("Runnable should be called")
                    public void testRunnable()
                    {
                        observable.onNext(7);
                        testScheduler.triggerActions();

                        Mockito.verify(runnable).run();
                    }
                }

                @Nested
                @DisplayName("When activated")
                class WhenDeactivate
                {
                    @BeforeEach
                    void before()
                    {
                        compositeActivable.deactivate();
                    }

                    @Nested
                    @DisplayName("When observable emits value")
                    class WhenObservableEmitsValue
                    {
                        @Test
                        @DisplayName("Runnable should not be called")
                        public void testRunnable()
                        {
                            observable.onNext(7);
                            testScheduler.triggerActions();

                            Mockito.verify(runnable, Mockito.never()).run();
                        }
                    }
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
                @DisplayName("Consumer should not be called")
                public void testConsumer()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
                }
            }

            @Nested
            @DisplayName("When activated")
            class WhenActivate
            {
                @BeforeEach
                void before()
                {
                    compositeActivable.activate();
                }

                @Nested
                @DisplayName("When observable emits value")
                class WhenObservableEmitsValue
                {
                    @Test
                    @DisplayName("Consumer should be called with correct value")
                    public void testConsumer()
                    {
                        observable.onNext(7);
                        testScheduler.triggerActions();

                        Mockito.verify(consumer).accept(7);
                    }
                }

                @Nested
                @DisplayName("When activated")
                class WhenDeactivate
                {
                    @BeforeEach
                    void before()
                    {
                        compositeActivable.deactivate();
                    }

                    @Test
                    @DisplayName("Consumer should not be called")
                    public void testConsumer()
                    {
                        observable.onNext(7);
                        testScheduler.triggerActions();

                        Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
                    }
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
                @DisplayName("Supplier should not be called")
                public void tesSupplier()
                {
                    observable.onNext(5);
                    testScheduler.triggerActions();

                    Mockito.verify(supplier, Mockito.never()).get();
                }
            }

            @Nested
            @DisplayName("When activated")
            class WhenActivate
            {
                @BeforeEach
                void before()
                {
                    compositeActivable.activate();
                }

                @Nested
                @DisplayName("When observable emits value")
                class WhenObservableEmitsValue
                {
                    @Test
                    @DisplayName("Supplier should be called ")
                    public void testSupplier()
                    {
                        observable.onNext(7);
                        testScheduler.triggerActions();

                        Mockito.verify(supplier).get();
                    }
                }

                @Nested
                @DisplayName("When activated")
                class WhenDeactivate
                {
                    @BeforeEach
                    void before()
                    {
                        compositeActivable.deactivate();
                    }

                    @Test
                    @DisplayName("Supplier should not be called")
                    public void tesSupplier()
                    {
                        observable.onNext(5);
                        testScheduler.triggerActions();

                        Mockito.verify(supplier, Mockito.never()).get();
                    }
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
                @DisplayName("Function should not be called")
                public void testFunction()
                {
                    observable.onNext(5);
                    testScheduler.triggerActions();

                    Mockito.verify(function, Mockito.never()).apply(Mockito.any());
                }
            }

            @Nested
            @DisplayName("When activated")
            class WhenActivate
            {
                @BeforeEach
                void before()
                {
                    compositeActivable.activate();
                }

                @Nested
                @DisplayName("When observable emits value")
                class WhenObservableEmitsValue
                {
                    @Test
                    @DisplayName("Function should be called with correct value")
                    public void testFunction()
                    {
                        observable.onNext(5);
                        testScheduler.triggerActions();

                        Mockito.verify(function).apply(5);
                    }
                }

                @Nested
                @DisplayName("When activated")
                class WhenDeactivate
                {
                    @BeforeEach
                    void before()
                    {
                        compositeActivable.deactivate();
                    }

                    @Test
                    @DisplayName("Function should not be called")
                    public void testFunction()
                    {
                        observable.onNext(5);
                        testScheduler.triggerActions();

                        Mockito.verify(function, Mockito.never()).apply(Mockito.any());
                    }
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
            compositeActivable = new CompositeActivable();
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            errorSubject = PublishSubject.create();

            binder = new ActivableObservableBinder<>(compositeActivable, when(() -> observable));
        }
    }
}
