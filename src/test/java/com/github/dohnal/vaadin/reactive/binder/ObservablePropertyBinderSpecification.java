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

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Specification for binding observable property by {@link ObservablePropertyBinder}
 *
 * @author dohnal
 */
public interface ObservablePropertyBinderSpecification
{
    abstract class WhenBindObservablePropertyToObservableSpecification
            implements ReactiveBinderExtension, ReactivePropertyExtension
    {
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> sourceObservable;
        protected ReactiveProperty<Integer> property;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected ObservablePropertyBinder<Integer> binder;
        protected Disposable disposable;

        @BeforeEach
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = createProperty();

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            binder = bind(property);
            disposable = binder.to(sourceObservable);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("GetProperty should return correct property")
        public void testGetProperty()
        {
            assertEquals(property, binder.getProperty());
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            property.asObservable().test().assertNoValues();
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
        }

        @Nested
        @DisplayName("When source observable emits value")
        class WhenSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                testObserver.assertValue(7);
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                errorObserver.assertNoValues();
            }
        }

        @Nested
        @DisplayName("When source observable emits error")
        class WhenSourceObservableEmitsError
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Property value should not be set")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                sourceObservable.onError(ERROR);
                testScheduler.triggerActions();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Correct error should be handled")
            public void testHandleError()
            {
                sourceObservable.onError(ERROR);
                testScheduler.triggerActions();

                errorObserver.assertValue(ERROR);
            }
        }

        @Nested
        @DisplayName("After property is unbound from observable")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
            }

            @Nested
            @DisplayName("When source observable emits value")
            class WhenSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Property value should not be set")
                public void testPropertyValue()
                {
                    final TestObserver<Integer> testObserver = property.asObservable().test();

                    disposable.dispose();
                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When source observable emits error")
            class WhenSourceObservableEmitsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @Test
                @SuppressWarnings("unchecked")
                @DisplayName("Property value should not be set")
                public void testPropertyValue()
                {
                    final TestObserver<Integer> testObserver = property.asObservable().test();

                    sourceObservable.onError(ERROR);
                    testScheduler.triggerActions();

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("No error should be handled")
                public void testHandleError()
                {
                    sourceObservable.onError(ERROR);
                    testScheduler.triggerActions();

                    errorObserver.assertNoValues();
                }
            }
        }
    }

    abstract class WhenBindObservablePropertyToIsObservableSpecification
            extends WhenBindObservablePropertyToObservableSpecification
    {
        @BeforeEach
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = createProperty();

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(testScheduler);
            errorObserver = errorSubject.test();

            binder = bind(property);
            disposable = binder.to(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return sourceObservable;
                }
            });
        }
    }

    abstract class WhenBindObservablePropertyToObservablePropertySpecification
            implements ReactiveBinderExtension, ReactivePropertyExtension
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private PublishSubject<Throwable> errorSubject;
        private TestObserver<Throwable> errorObserver;
        protected ObservablePropertyBinder<Integer> binder;
        private Disposable disposable;

        @BeforeEach
        protected void bind()
        {
            sourceProperty = createProperty();
            property = createProperty();

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            binder = bind(property);
            disposable = binder.to(sourceProperty);
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Test
        @DisplayName("GetProperty should return correct property")
        public void testGetProperty()
        {
            assertEquals(property, binder.getProperty());
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            property.asObservable().test().assertNoValues();
        }

        @Test
        @DisplayName("Source property value should not be set")
        public void testSourcePropertyValue()
        {
            sourceProperty.asObservable().test().assertNoValues();
        }

        @Test
        @DisplayName("No error should be handled")
        public void testHandleError()
        {
            errorObserver.assertNoValues();
        }

        @Nested
        @DisplayName("When source property emits value")
        class WhenSourcePropertyEmitsValue
        {
            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                sourceProperty.setValue(7);

                testObserver.assertValue(7);
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }
        }

        @Nested
        @DisplayName("When property emits value")
        class WhenPropertyEmitsValue
        {
            @Test
            @DisplayName("Source property value should be set with correct value")
            public void testSourcePropertyValue()
            {
                final TestObserver<Integer> testObserver = sourceProperty.asObservable().test();

                property.setValue(7);

                testObserver.assertValue(7);
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                errorObserver.assertNoValues();
            }
        }

        @Nested
        @DisplayName("After property is unbound from source property")
        class AfterUnbind
        {
            @BeforeEach
            void before()
            {
                disposable.dispose();
            }

            @Nested
            @DisplayName("When source property emits value")
            class WhenSourcePropertyEmitsValue
            {
                @Test
                @DisplayName("Property value should not be set")
                public void testPropertyValue()
                {
                    final TestObserver<Integer> testObserver = property.asObservable().test();

                    sourceProperty.setValue(7);

                    testObserver.assertNoValues();
                }
            }

            @Nested
            @DisplayName("When property emits value")
            class WhenPropertyEmitsValue
            {
                @Test
                @DisplayName("Source property value should not be set")
                public void testSourcePropertyValue()
                {
                    final TestObserver<Integer> testObserver = sourceProperty.asObservable().test();

                    property.setValue(7);

                    testObserver.assertNoValues();
                }
            }
        }
    }
}
