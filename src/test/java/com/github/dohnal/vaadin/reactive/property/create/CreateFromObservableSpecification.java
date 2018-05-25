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

package com.github.dohnal.vaadin.reactive.property.create;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
import com.github.dohnal.vaadin.reactive.property.SetValueSpecification;
import com.github.dohnal.vaadin.reactive.property.SourceEmitsValueSpecification;
import com.github.dohnal.vaadin.reactive.property.UpdateValueSpecification;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty} created by {@link ReactivePropertyExtension#createPropertyFrom(Observable)}
 *
 * @author dohnal
 */
public interface CreateFromObservableSpecification extends
        SetValueSpecification,
        UpdateValueSpecification,
        SourceEmitsValueSpecification
{
    abstract class AbstractCreateFromObservableSpecification implements ReactivePropertyExtension
    {
        private ReplaySubject<ReactiveProperty<?>> capturedProperties;
        private TestScheduler testScheduler;
        private PublishSubject<Integer> testSubject;
        private ReactiveProperty<Integer> property;

        @BeforeEach
        void createFromObservable()
        {
            capturedProperties = ReplaySubject.create();
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            property = createPropertyFrom(testSubject);
        }

        @Nonnull
        @Override
        public  <T> ReactiveProperty<T> onCreateProperty(final @Nonnull ReactiveProperty<T> property)
        {
            final ReactiveProperty<T> created = ReactivePropertyExtension.super.onCreateProperty(property);

            capturedProperties.onNext(created);

            return created;
        }

        @Test
        @DisplayName("Created property should be captured")
        public void testCreatedProperty()
        {
            capturedProperties.test().assertValue(property);
        }

        @Test
        @DisplayName("IsReadOnly should be true")
        public void testIsReadOnly()
        {
            assertTrue(property.isReadOnly());
        }

        @Test
        @DisplayName("HasValue should be false")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
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
            property.asObservable().test().assertNoValues();
        }

        @Nested
        @DisplayName("Set value specification")
        class SetValue extends AbstractSetValueReadOnlySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Update value specification")
        class UpdateValue extends AbstractUpdateValueReadOnlySpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }
        }

        @Nested
        @DisplayName("Source emits value specification")
        class SourceEmitsValue extends AbstractSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            public void emitValue(final @Nonnull Integer value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When source observable emits error")
        class WhenSourceEmitsError
        {
            protected final Throwable ERROR = new RuntimeException("Error");

            @Test
            @DisplayName("Property should emit error")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                testSubject.onError(ERROR);
                testScheduler.triggerActions();

                testObserver.assertError(ERROR);
            }
        }

        @Nested
        @DisplayName("When source observable completes")
        class WhenSourceCompletes
        {
            @Test
            @DisplayName("Property should complete")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = property.asObservable().test();

                testSubject.onComplete();
                testScheduler.triggerActions();

                testObserver.assertComplete();
            }
        }
    }
}
