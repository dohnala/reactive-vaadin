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

package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Specification for {@link ReactiveProperty} created by {@link ReactiveProperty#fromObservable(Observable)}
 *
 * @author dohnal
 */
public interface PropertyFromObservableSpecification extends BasePropertySpecification
{
    abstract class WhenCreateFromObservableSpecification
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createFromObservable()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            property = ReactiveProperty.fromObservable(testSubject);
        }

        @Test
        @DisplayName("Value should be null")
        public void testValue()
        {
            assertNull(property.getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("HasValue should be false")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Nested
        @DisplayName("When source observable emits different value")
        class WhenSourceObservableEmitsDifferentValue extends WhenSourceEmitsDifferentValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When source observable emits same value")
        class WhenSourceObservableEmitsSameValue extends WhenSourceEmitsSameValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue extends WhenSetDifferentValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When same value is set")
        class WhenSetSameValue extends WhenSetSameValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe extends WhenSubscribeSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }
    }
}
