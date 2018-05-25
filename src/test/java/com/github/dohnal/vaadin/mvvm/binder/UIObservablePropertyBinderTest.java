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

import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Tests for {@link UIObservablePropertyBinder}
 *
 * @author dohnal
 */
@DisplayName("UI observable property binder specification")
public class UIObservablePropertyBinderTest
{
    @Nested
    @DisplayName("When observable UI property binder is created")
    class WhenCreate implements ReactiveBinderExtension, ReactivePropertyExtension
    {
        private Consumer<Runnable> withUIAccess;
        private ReactiveProperty<Integer> property;
        private PublishSubject<Throwable> errorSubject;

        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void before()
        {
            withUIAccess = Mockito.mock(Consumer.class);
            property = createProperty();
            errorSubject = PublishSubject.create();

            binder = new UIObservablePropertyBinder<>(withUIAccess,
                    bind(new UIObservableProperty<>(withUIAccess, property)));
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
        @DisplayName("When observable property is bound to observable")
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
                @SuppressWarnings("unchecked")
                @DisplayName("Property value should be set with correct value")
                public void testPropertyValue()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    final TestObserver<Integer> testObserver = property.asObservable().test();

                    sourceObservable.onNext(7);
                    testScheduler.triggerActions();

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    testObserver.assertValue(7);
                }
            }
        }

        @Nested
        @DisplayName("When observable property is bound to IsObservable")
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

        @Nested
        @DisplayName("When observable property is bound to observable property")
        class WhenBindToObservableProperty
        {
            protected Disposable disposable;
            private ReactiveProperty<Integer> sourceProperty;

            @BeforeEach
            void before()
            {
                sourceProperty = createProperty();

                disposable = binder.to(sourceProperty);
            }

            @Nested
            @DisplayName("When source property emits value")
            class WhenSourcePropertyEmitsValue
            {
                @Test
                @DisplayName("Property value should be set with UI access")
                public void testWithUIAccess()
                {
                    sourceProperty.setValue(7);

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }

                @Test
                @DisplayName("Property value should be set with correct value")
                public void testPropertyValue()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    final TestObserver<Integer> testObserver = property.asObservable().test();

                    sourceProperty.setValue(7);

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    testObserver.assertValue(7);
                }
            }

            @Nested
            @DisplayName("When property emits value")
            class WhenPropertyEmitsValue
            {
                @Test
                @DisplayName("Property value should be set with UI access")
                public void testWithUIAccess()
                {
                    property.setValue(7);

                    Mockito.verify(withUIAccess).accept(Mockito.any(Runnable.class));
                }

                @Test
                @DisplayName("Source property value should be set with correct value")
                public void testSourcePropertyValue()
                {
                    final ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);

                    final TestObserver<Integer> testObserver = sourceProperty.asObservable().test();

                    property.setValue(7);

                    Mockito.verify(withUIAccess).accept(captor.capture());

                    captor.getValue().run();

                    testObserver.assertValue(7);
                }
            }
        }
    }
}
