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

import javax.annotation.Nonnull;

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
import org.vaadin.addons.reactive.Property;
import org.vaadin.addons.reactive.PropertyBinder;
import org.vaadin.addons.reactive.ReactiveBinderExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Specification for binding property by {@link PropertyBinder}
 *
 * @author dohnal
 */
public interface PropertyBinderSpecification
{
    abstract class WhenBindPropertyToObservableSpecification implements ReactiveBinderExtension
    {
        protected TestScheduler testScheduler;
        protected PublishSubject<Integer> sourceObservable;
        protected Property<Integer> property;
        protected PublishSubject<Throwable> errorSubject;
        protected TestObserver<Throwable> errorObserver;
        protected PropertyBinder<Integer> binder;
        protected Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = Mockito.mock(Property.class);

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
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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
            @SuppressWarnings("unchecked")
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                Mockito.clearInvocations(property);

                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(property).setValue(7);
            }

            @Test
            @DisplayName("No error should be handled")
            public void testHandleError()
            {
                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                errorObserver.assertNoValues();
            }

            @Nested
            @DisplayName("When property throws error")
            class WhenPropertyThrowsError
            {
                private final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                void before()
                {
                    Mockito.doThrow(ERROR).when(property).setValue(7);
                }

                @Test
                @DisplayName("Correct error should be handled")
                public void testHandleError()
                {
                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    errorObserver.assertValue(ERROR);
                }
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
                Mockito.clearInvocations(property);

                sourceObservable.onError(ERROR);
                testScheduler.triggerActions();

                Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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
                @SuppressWarnings("unchecked")
                @DisplayName("Property value should not be set")
                public void testPropertyValue()
                {
                    Mockito.clearInvocations(property);

                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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
                    Mockito.clearInvocations(property);

                    sourceObservable.onError(ERROR);
                    testScheduler.triggerActions();

                    Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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

    abstract class WhenBindPropertyToIsObservableSpecification extends WhenBindPropertyToObservableSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = Mockito.mock(Property.class);

            errorSubject = PublishSubject.create();
            errorSubject.observeOn(Schedulers.trampoline());
            errorObserver = errorSubject.test();

            binder = bind(property);
            disposable = binder.to(() -> sourceObservable);
        }
    }
}
