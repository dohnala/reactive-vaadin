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

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Specification for binding property by {@link PropertyBinder}
 *
 * @author dohnal
 */
public interface PropertyBinderSpecification
{
    abstract class WhenBindPropertyToObservableSpecification implements ReactiveBinder
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> sourceObservable;
        private Property<Integer> property;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = Schedulers.test();
            sourceObservable = TestSubject.create(testScheduler);
            property = Mockito.mock(Property.class);

            disposable = bind(property).to(sourceObservable);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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
        @DisplayName("When source observable emits value after property is unbound from observable")
        class WhenSourceObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Property value should not be set")
            public void testPropertyValue()
            {
                Mockito.clearInvocations(property);

                disposable.dispose();

                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
            }
        }
    }

    abstract class WhenBindPropertyToIsObservableSpecification implements ReactiveBinder
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> sourceObservable;
        private Property<Integer> property;
        private Disposable disposable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void bind()
        {
            testScheduler = Schedulers.test();
            sourceObservable = TestSubject.create(testScheduler);
            property = Mockito.mock(Property.class);

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
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
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
        @DisplayName("When source observable emits value after property is unbound from observable")
        class WhenSourceObservableEmitsValueAfterUnbind
        {
            @Test
            @SuppressWarnings("unchecked")
            @DisplayName("Property value should not be set")
            public void testPropertyValue()
            {
                Mockito.clearInvocations(property);

                disposable.dispose();

                sourceObservable.onNext(7);
                testScheduler.triggerActions();

                Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
            }
        }
    }
}
