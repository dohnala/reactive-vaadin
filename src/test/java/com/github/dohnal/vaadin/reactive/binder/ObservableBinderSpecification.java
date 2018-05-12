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
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.IsObservable;
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
import org.mockito.Mockito;

/**
 * Specification for binding observable by {@link ObservableBinder}
 *
 * @author dohnal
 */
public interface ObservableBinderSpecification
{
    abstract class WhenBindObservableToConsumerSpecification implements ReactiveBinderExtension
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> observable;
        private Consumer<Integer> consumer;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            consumer = Mockito.mock(Consumer.class);

            disposable = when(observable).then(consumer);
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
            @DisplayName("Consumer should be called with correct value")
            public void testPropertyValue()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(consumer).accept(7);
            }
        }

        @Nested
        @DisplayName("When observable emits value after observable is unbound from consumer")
        class WhenObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Consumer should not be called")
            public void testPropertyValue()
            {
                disposable.dispose();

                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
            }
        }
    }

    abstract class WhenBindObservableToRunnableSpecification implements ReactiveBinderExtension
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> observable;
        private Runnable runnable;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            runnable = Mockito.mock(Runnable.class);

            disposable = when(observable).then(runnable);
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testConsumer()
        {
            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Nested
        @DisplayName("When observable emits value")
        class WhenObservableEmitsValue
        {
            @Test
            @DisplayName("Runnable should be called")
            public void testPropertyValue()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(runnable).run();
            }
        }

        @Nested
        @DisplayName("When observable emits value after observable is unbound from runnable")
        class WhenObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Runnable should not be called")
            public void testPropertyValue()
            {
                disposable.dispose();

                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(runnable, Mockito.never()).run();
            }
        }
    }

    abstract class WhenBindIsObservableToConsumerSpecification implements ReactiveBinderExtension
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> observable;
        private Consumer<Integer> consumer;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            consumer = Mockito.mock(Consumer.class);

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
            @DisplayName("Consumer should be called with correct value")
            public void testPropertyValue()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(consumer).accept(7);
            }
        }

        @Nested
        @DisplayName("When observable emits value after observable is unbound from consumer")
        class WhenObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Consumer should not be called")
            public void testPropertyValue()
            {
                disposable.dispose();

                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
            }
        }
    }

    abstract class WhenBindIsObservableToRunnableSpecification implements ReactiveBinderExtension
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> observable;
        private Runnable runnable;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            observable = PublishSubject.create();
            observable.observeOn(testScheduler);
            runnable = Mockito.mock(Runnable.class);

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

        @Test
        @DisplayName("Runnable should not be called")
        public void testConsumer()
        {
            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Nested
        @DisplayName("When observable emits value")
        class WhenObservableEmitsValue
        {
            @Test
            @DisplayName("Runnable should be called")
            public void testPropertyValue()
            {
                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(runnable).run();
            }
        }

        @Nested
        @DisplayName("When observable emits value after observable is unbound from runnable")
        class WhenObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Runnable should not be called")
            public void testPropertyValue()
            {
                disposable.dispose();

                observable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(runnable, Mockito.never()).run();
            }
        }
    }
}
