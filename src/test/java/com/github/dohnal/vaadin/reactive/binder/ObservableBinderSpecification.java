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

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ObservableBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Specification for binding observable by {@link ObservableBinder}
 *
 * @author dohnal
 */
public interface ObservableBinderSpecification
{
    abstract class WhenBindObservableToRunnableSpecification implements ReactiveBinderExtension
    {
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> observable;
        protected Runnable runnable;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            runnable = Mockito.mock(Runnable.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(observable).then(runnable);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
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

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }

            @Nested
            @DisplayName("When runnable throws error")
            class WhenRunnableThrowsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                void before()
                {
                    Mockito.doThrow(ERROR).when(runnable).run();
                }

                @Test
                @DisplayName("Correct error should be handled")
                public void testHandleError()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    errorObserver.assertValue(ERROR);
                }
            }
        }

        @Nested
        @DisplayName("When observable emits error")
        class WhenObservableEmitsError
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Test
            @DisplayName("Runnable should not be called")
            public void testRunnable()
            {
                observable.onError(ERROR);
                testScheduler.triggerActions();

                Mockito.verify(runnable, Mockito.never()).run();
            }

            @Test
            @DisplayName("Correct error should be handled")
            public void testHandleError()
            {
                observable.onError(ERROR);
                testScheduler.triggerActions();

                errorObserver.assertValue(ERROR);
            }
        }

        @Nested
        @DisplayName("After observable is unbound from runnable")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
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

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    errorObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @Test
                @DisplayName("Runnable should not be called")
                public void testRunnable()
                {
                    observable.onError(ERROR);
                    testScheduler.triggerActions();

                    Mockito.verify(runnable, Mockito.never()).run();
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    observable.onError(ERROR);
                    testScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }
        }
    }

    abstract class WhenBindIsObservableToRunnableSpecification extends WhenBindObservableToRunnableSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            runnable = Mockito.mock(Runnable.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return observable;
                }
            }).then(runnable);
        }
    }

    abstract class WhenBindObservableToConsumerSpecification implements ReactiveBinderExtension
    {
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> observable;
        protected Consumer<Integer> consumer;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            consumer = Mockito.mock(Consumer.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(observable).then(consumer);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
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

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }

            @Nested
            @DisplayName("When consumer throws error")
            class WhenConsumerThrowsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                void before()
                {
                    Mockito.doThrow(ERROR).when(consumer).accept(7);
                }

                @Test
                @DisplayName("Correct error should be handled")
                public void testHandleError()
                {
                    observable.onNext(7);
                    testScheduler.triggerActions();

                    errorObserver.assertValue(ERROR);
                }
            }
        }

        @Nested
        @DisplayName("When observable emits error")
        class WhenObservableEmitsError
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Test
            @DisplayName("Consumer should not be called")
            public void testConsumer()
            {
                observable.onError(ERROR);
                testScheduler.triggerActions();

                Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
            }

            @Test
            @DisplayName("Correct error should be handled")
            public void testHandleError()
            {
                observable.onError(ERROR);
                testScheduler.triggerActions();

                errorObserver.assertValue(ERROR);
            }
        }

        @Nested
        @DisplayName("After observable is unbound from consumer")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
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

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    errorObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @Test
                @DisplayName("Consumer should not be called")
                public void testConsumer()
                {
                    observable.onError(ERROR);
                    testScheduler.triggerActions();

                    Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    observable.onError(ERROR);
                    testScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }
        }
    }

    abstract class WhenBindIsObservableToConsumerSpecification extends WhenBindObservableToConsumerSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            consumer = Mockito.mock(Consumer.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return observable;
                }
            }).then(consumer);
        }
    }

    abstract class WhenBindObservableToObservableSupplierSpecification implements ReactiveBinderExtension
    {
        protected TestScheduler observableScheduler;
        protected TestScheduler actionScheduler;
        protected PublishSubject<Integer> observable;
        protected PublishSubject<Integer> actionResult;
        protected Supplier<Observable<?>> supplier;
        protected Observable<Integer> action;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            observableScheduler = new TestScheduler();
            actionScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(observableScheduler);
            actionResult = PublishSubject.create();
            supplier = Mockito.mock(Supplier.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(observable).then(supplier);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("Supplier should not be called")
        public void testSupplier()
        {
            Mockito.verify(supplier, Mockito.never()).get();
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
        }

        @Nested
        @DisplayName("When observable emits value")
        class WhenObservableEmitsValue
        {
            @Test
            @DisplayName("Supplier should be called")
            public void testSupplier()
            {
                observable.onNext(5);
                observableScheduler.triggerActions();

                Mockito.verify(supplier).get();
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }

            @Test
            @DisplayName("Action result should be correct")
            public void testActionResult()
            {
                Mockito.when(supplier.get()).thenReturn(Observable
                        .timer(2, TimeUnit.SECONDS, actionScheduler)
                        .doOnNext(time -> actionResult.onNext(5))
                        .ignoreElements()
                        .toObservable());

                final TestObserver<Integer> testObserver = actionResult.test();

                observable.onNext(5);
                observableScheduler.triggerActions();

                actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                testObserver.assertValue(5);
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                void before()
                {
                    Mockito.when(supplier.get()).thenReturn(Observable.error(ERROR));
                }

                @Test
                @DisplayName("Correct error should be handled")
                public void testHandleError()
                {
                    observable.onNext(5);
                    observableScheduler.triggerActions();

                    errorObserver.assertValue(ERROR);
                }

                @Test
                @DisplayName("Action result should not emit any value")
                public void testActionResult()
                {
                    final TestObserver<Integer> testObserver = actionResult.test();

                    observable.onNext(5);
                    observableScheduler.triggerActions();

                    testObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("After observable emits value")
            class AfterObservableEmitsValue
            {
                @BeforeEach
                void before()
                {
                    Mockito.when(supplier.get()).thenReturn(Observable
                            .timer(2, TimeUnit.SECONDS, actionScheduler)
                            .doOnNext(time -> actionResult.onNext(5))
                            .ignoreElements()
                            .toObservable());

                    observable.onNext(5);
                    observableScheduler.triggerActions();
                }

                @Nested
                @DisplayName("When observable emits another value before action completes")
                class WhenObservableEmitsAnotherValueBeforeActionCompletes
                {
                    @BeforeEach
                    void before()
                    {
                        actionScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

                        Mockito.when(supplier.get()).thenReturn(Observable
                                .timer(2, TimeUnit.SECONDS, actionScheduler)
                                .doOnNext(time -> actionResult.onNext(7))
                                .ignoreElements()
                                .toObservable());
                    }

                    @Test
                    @SuppressWarnings("unchecked")
                    @DisplayName("Supplier should be called")
                    public void testSupplier()
                    {
                        Mockito.clearInvocations(supplier);

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        Mockito.verify(supplier).get();
                    }

                    @Test
                    @DisplayName("No error should be handled")
                    public void testHandleError()
                    {
                        errorObserver.assertNoValues();
                    }

                    @Test
                    @DisplayName("Action result should be correct")
                    public void testActionResult()
                    {
                        final TestObserver<Integer> testObserver = actionResult.test();

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        testObserver.assertValue(7);
                    }
                }

                @Nested
                @DisplayName("When observable emits another value after action completes")
                class WhenObservableEmitsAnotherValueAfterActionCompletes
                {
                    @BeforeEach
                    void before()
                    {
                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        Mockito.when(supplier.get()).thenReturn(Observable
                                .timer(2, TimeUnit.SECONDS, actionScheduler)
                                .doOnNext(time -> actionResult.onNext(7))
                                .ignoreElements()
                                .toObservable());
                    }

                    @Test
                    @SuppressWarnings("unchecked")
                    @DisplayName("Supplier should be called ")
                    public void testSupplier()
                    {
                        Mockito.clearInvocations(supplier);

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        Mockito.verify(supplier).get();
                    }

                    @Test
                    @DisplayName("No error should be handled")
                    public void testHandleError()
                    {
                        errorObserver.assertNoValues();
                    }

                    @Test
                    @DisplayName("Action result should be correct")
                    public void testActionResult()
                    {
                        final TestObserver<Integer> testObserver = actionResult.test();

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        testObserver.assertValue(7);
                    }
                }
            }
        }

        @Nested
        @DisplayName("When observable emits error")
        class WhenObservableEmitsError
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Test
            @DisplayName("Supplier should not be called")
            public void testSupplier()
            {
                observable.onError(ERROR);
                observableScheduler.triggerActions();

                Mockito.verify(supplier, Mockito.never()).get();
            }

            @Test
            @DisplayName("Correct error should be handled")
            public void testHandleError()
            {
                observable.onError(ERROR);
                observableScheduler.triggerActions();

                errorObserver.assertValue(ERROR);
            }
        }

        @Nested
        @DisplayName("After observable is unbound from observable supplier")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
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
                    observableScheduler.triggerActions();

                    Mockito.verify(supplier, Mockito.never()).get();
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    errorObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @Test
                @DisplayName("Supplier should not be called")
                public void testSupplier()
                {
                    observable.onError(ERROR);
                    observableScheduler.triggerActions();

                    Mockito.verify(supplier, Mockito.never()).get();
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    observable.onError(ERROR);
                    observableScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }
        }
    }

    abstract class WhenBindIsObservableToObservableSupplierSpecification extends
            WhenBindObservableToObservableSupplierSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            observableScheduler = new TestScheduler();
            actionScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(observableScheduler);
            actionResult = PublishSubject.create();
            supplier = Mockito.mock(Supplier.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return observable;
                }
            }).then(supplier);
        }
    }

    abstract class WhenBindObservableToObservableFunctionSpecification implements ReactiveBinderExtension
    {
        protected TestScheduler observableScheduler;
        protected TestScheduler actionScheduler;
        protected PublishSubject<Integer> observable;
        protected PublishSubject<Integer> actionResult;
        protected Function<Integer, Observable<?>> function;
        protected Observable<Integer> action;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            observableScheduler = new TestScheduler();
            actionScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(observableScheduler);
            actionResult = PublishSubject.create();
            function = Mockito.mock(Function.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(observable).then(function);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("Function should not be called")
        public void testFunction()
        {
            Mockito.verify(function, Mockito.never()).apply(Mockito.any());
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
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
                observableScheduler.triggerActions();

                Mockito.verify(function).apply(5);
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }

            @Test
            @DisplayName("Action result should be correct")
            public void testActionResult()
            {
                Mockito.when(function.apply(5)).thenReturn(Observable
                        .timer(2, TimeUnit.SECONDS, actionScheduler)
                        .doOnNext(time -> actionResult.onNext(5))
                        .ignoreElements()
                        .toObservable());

                final TestObserver<Integer> testObserver = actionResult.test();

                observable.onNext(5);
                observableScheduler.triggerActions();

                actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                testObserver.assertValue(5);
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                void before()
                {
                    Mockito.when(function.apply(5)).thenReturn(Observable.error(ERROR));
                }

                @Test
                @DisplayName("Correct error should be handled")
                public void testHandleError()
                {
                    observable.onNext(5);
                    observableScheduler.triggerActions();

                    errorObserver.assertValue(ERROR);
                }

                @Test
                @DisplayName("Action result should not emit any value")
                public void testActionResult()
                {
                    final TestObserver<Integer> testObserver = actionResult.test();

                    observable.onNext(5);
                    observableScheduler.triggerActions();

                    testObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("After observable emits value")
            class AfterObservableEmitsValue
            {
                @BeforeEach
                void before()
                {
                    Mockito.when(function.apply(5)).thenReturn(Observable
                            .timer(2, TimeUnit.SECONDS, actionScheduler)
                            .doOnNext(time -> actionResult.onNext(5))
                            .ignoreElements()
                            .toObservable());

                    observable.onNext(5);
                    observableScheduler.triggerActions();
                }

                @Nested
                @DisplayName("When observable emits another value before action completes")
                class WhenObservableEmitsAnotherValueBeforeActionCompletes
                {
                    @BeforeEach
                    void before()
                    {
                        actionScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

                        Mockito.when(function.apply(7)).thenReturn(Observable
                                .timer(2, TimeUnit.SECONDS, actionScheduler)
                                .doOnNext(time -> actionResult.onNext(7))
                                .ignoreElements()
                                .toObservable());
                    }

                    @Test
                    @DisplayName("Function should be called with correct value")
                    public void testFunction()
                    {
                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        Mockito.verify(function).apply(7);
                    }

                    @Test
                    @DisplayName("No error should be handled")
                    public void testHandleError()
                    {
                        errorObserver.assertNoValues();
                    }

                    @Test
                    @DisplayName("Action result should be correct")
                    public void testActionResult()
                    {
                        final TestObserver<Integer> testObserver = actionResult.test();

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        testObserver.assertValue(7);
                    }
                }

                @Nested
                @DisplayName("When observable emits another value after action completes")
                class WhenObservableEmitsAnotherValueAfterActionCompletes
                {
                    @BeforeEach
                    void before()
                    {
                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        Mockito.when(function.apply(7)).thenReturn(Observable
                                .timer(2, TimeUnit.SECONDS, actionScheduler)
                                .doOnNext(time -> actionResult.onNext(7))
                                .ignoreElements()
                                .toObservable());
                    }

                    @Test
                    @DisplayName("Function should be called with correct value")
                    public void testFunction()
                    {
                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        Mockito.verify(function).apply(7);
                    }

                    @Test
                    @DisplayName("No error should be handled")
                    public void testHandleError()
                    {
                        errorObserver.assertNoValues();
                    }

                    @Test
                    @DisplayName("Action result should be correct")
                    public void testActionResult()
                    {
                        final TestObserver<Integer> testObserver = actionResult.test();

                        observable.onNext(7);
                        observableScheduler.triggerActions();

                        actionScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

                        testObserver.assertValue(7);
                    }
                }
            }
        }

        @Nested
        @DisplayName("When observable emits error")
        class WhenObservableEmitsError
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Test
            @DisplayName("Function should not be called")
            public void testFunction()
            {
                observable.onError(ERROR);
                observableScheduler.triggerActions();

                Mockito.verify(function, Mockito.never()).apply(Mockito.any());
            }

            @Test
            @DisplayName("Correct error should be handled")
            public void testHandleError()
            {
                observable.onError(ERROR);
                observableScheduler.triggerActions();

                errorObserver.assertValue(ERROR);
            }
        }

        @Nested
        @DisplayName("After observable is unbound from observable function")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
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
                    observableScheduler.triggerActions();

                    Mockito.verify(function, Mockito.never()).apply(Mockito.any());
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    errorObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When observable emits error")
            class WhenObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @Test
                @DisplayName("Function should not be called")
                public void testFunction()
                {
                    observable.onError(ERROR);
                    observableScheduler.triggerActions();

                    Mockito.verify(function, Mockito.never()).apply(Mockito.any());
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    observable.onError(ERROR);
                    observableScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }
        }
    }

    abstract class WhenBindIsObservableToObservableFunctionSpecification extends
            WhenBindObservableToObservableFunctionSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            observableScheduler = new TestScheduler();
            actionScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(observableScheduler);
            actionResult = PublishSubject.create();
            function = Mockito.mock(Function.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            disposable = when(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return observable;
                }
            }).then(function);
        }
    }
}
