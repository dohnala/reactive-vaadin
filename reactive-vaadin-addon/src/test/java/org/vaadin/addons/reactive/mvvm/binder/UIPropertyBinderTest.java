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

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.Property;
import org.vaadin.addons.reactive.PropertyBinder;
import org.vaadin.addons.reactive.ReactiveBinderExtension;

/**
 * Tests for {@link UIPropertyBinder}
 *
 * @author dohnal
 */
@DisplayName("UI property binder specification")
public class UIPropertyBinderTest
{
    @Nested
    @DisplayName("When UI property binder is created")
    class WhenCreate implements ReactiveBinderExtension
    {
        private Consumer<Runnable> withUIAccess;
        private Property<Integer> property;
        private PublishSubject<Throwable> errorSubject;

        private PropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            withUIAccess = Mockito.mock(Consumer.class);
            property = Mockito.mock(Property.class);
            errorSubject = PublishSubject.create();

            binder = new UIPropertyBinder<>(withUIAccess, bind(new UIProperty<>(withUIAccess, property)));
        }

        @Override
        public void handleError(final @Nonnull Throwable error)
        {
            errorSubject.onNext(error);
        }

        @Nested
        @DisplayName("When GetProperty is called")
        class WhenGetProperty
        {
            @Nested
            @DisplayName("When value is set")
            class WhenSetValue
            {
                @Test
                @DisplayName("Property value should be set with UI access")
                public void testWithUIAccess()
                {
                    binder.getProperty().setValue(7);

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }
            }
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
                @DisplayName("Property value should be set with UI access")
                public void testWithUIAccess()
                {
                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }

                @Test
                @DisplayName("Property value should be set with correct value")
                public void testPropertyValue()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    Mockito.verify(property).setValue(7);
                }
            }
        }

        @Nested
        @DisplayName("When property is bound to IsObservable")
        class WhenBindToIsObservable extends WhenBindToObservable
        {
            @BeforeEach
            @SuppressWarnings("unchecked")
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
