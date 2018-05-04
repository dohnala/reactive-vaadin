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
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Specification for binding observable property by {@link ObservablePropertyBinder}
 *
 * @author dohnal
 */
public interface ObservablePropertyBinderSpecification
{
    abstract class WhenBindObservablePropertyToObservableSpecification
            implements ReactiveBinder, ReactivePropertyFactory
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> sourceObservable;
        private ReactiveProperty<Integer> property;
        private Disposable disposable;

        @BeforeEach
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = createProperty();

            disposable = bind(property).to(sourceObservable);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            property.asObservable().test()
                    .assertNoValues();
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
        }

        @Nested
        @DisplayName("When source observable emits value after property is unbound from observable")
        class WhenSourceObservableEmitsValueAfterUnbind
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
        }
    }

    abstract class WhenBindObservablePropertyToIsObservableSpecification
            implements ReactiveBinder, ReactivePropertyFactory
    {
        private TestScheduler testScheduler;
        private PublishSubject<Integer> sourceObservable;
        private ReactiveProperty<Integer> property;
        private Disposable disposable;

        @BeforeEach
        protected void bind()
        {
            testScheduler = new TestScheduler();
            sourceObservable = PublishSubject.create();
            sourceObservable.observeOn(testScheduler);
            property = createProperty();

            disposable = bind(property).to(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return sourceObservable;
                }
            });
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            property.asObservable().test()
                    .assertNoValues();
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
        }

        @Nested
        @DisplayName("When source observable emits value after property is unbound from observable")
        class WhenSourceObservableEmitsValueAfterUnbind
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
        }
    }

    abstract class WhenBindObservablePropertyToObservablePropertySpecification
            implements ReactiveBinder, ReactivePropertyFactory
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private Disposable disposable;

        @BeforeEach
        protected void bind()
        {
            sourceProperty = createProperty();
            property = createProperty();

            disposable = bind(property).to(sourceProperty);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Source property value should not be set")
        public void testSourcePropertyValue()
        {
            sourceProperty.asObservable().test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When source property emits value")
        class WhenSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                sourceProperty.setValue(7);

                testObserver.assertValue(7);
            }
        }

        @Nested
        @DisplayName("When property emits value")
        class WhenObservableEmitsValue
        {
            @Test
            @DisplayName("Source property value should be set with correct value")
            public void testSourcePropertyValue()
            {
                final TestObserver<Integer> testObserver = sourceProperty.asObservable().test();

                property.setValue(7);

                testObserver.assertValue(7);
            }
        }

        @Nested
        @DisplayName("When source property emits value after property is unbound from source property")
        class WhenSourceObservableEmitsValueAfterUnbind
        {
            @Test
            @DisplayName("Property value should not be set")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                disposable.dispose();
                sourceProperty.setValue(7);

                testObserver.assertNoValues();
            }
        }

        @Nested
        @DisplayName("When property emits value after property is unbound from source property")
        class WhenObservableEmitsValueAfterUnbind
        {
            @Test
            @DisplayName("Source property value should not be set")
            public void testPropertyValue()
            {
                final TestObserver<Integer> testObserver = sourceProperty.asObservable().test();

                disposable.dispose();
                property.setValue(7);

                testObserver.assertNoValues();
            }
        }
    }
}
