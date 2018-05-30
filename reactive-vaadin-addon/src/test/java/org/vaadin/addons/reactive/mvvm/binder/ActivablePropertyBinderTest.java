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

import io.reactivex.disposables.Disposable;
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
import org.vaadin.addons.reactive.activable.CompositeActivable;

/**
 * Tests for {@link ActivablePropertyBinder}
 *
 * @author dohnal
 */
@DisplayName("Activable property binder specification")
public class ActivablePropertyBinderTest
{
    @Nested
    @DisplayName("When activable property binder is created")
    class WhenCreate implements ReactiveBinderExtension
    {
        private CompositeActivable compositeActivable;
        private Property<Integer> property;
        private PublishSubject<Throwable> errorSubject;

        private PropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            compositeActivable = new CompositeActivable();
            property = Mockito.mock(Property.class);
            errorSubject = PublishSubject.create();

            binder = new ActivablePropertyBinder<>(compositeActivable, bind(property));
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Nested
        @DisplayName("When property is bound to observable")
        class WhenBindToObservable
        {
            protected TestScheduler testScheduler;
            protected PublishSubject<Integer> sourceObservable;
            protected Disposable disposable;

            @BeforeEach
            void before()
            {
                testScheduler = new TestScheduler();
                sourceObservable = PublishSubject.create();
                sourceObservable.observeOn(testScheduler);

                disposable = binder.to(sourceObservable);
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
                    }
                }
            }
        }

        @Nested
        @DisplayName("When property is bound to IsObservable")
        class WhenBindToIsObservable extends WhenBindToObservable
        {
            @BeforeEach
            void before()
            {
                testScheduler = new TestScheduler();
                sourceObservable = PublishSubject.create();
                sourceObservable.observeOn(testScheduler);

                disposable = binder.to(() -> sourceObservable);
            }
        }
    }
}
