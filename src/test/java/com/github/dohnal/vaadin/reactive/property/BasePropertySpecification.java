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
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base specifications for {@link ReactiveProperty}
 *
 * @author dohnal
 */
public interface BasePropertySpecification
{
    /**
     * Base interface for tests which needs property
     *
     * @param <T> type of property
     */
    interface RequireProperty<T>
    {
        @Nonnull
        ReactiveProperty<T> getProperty();
    }

    /**
     * Specification that tests behavior of property when created from source (observable / property) and
     * this source emits different value
     */
    abstract class WhenSourceEmitsDifferentValueSpecification implements RequireProperty<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            emitValue(5);
            emitValue(7);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            emitValue(5);
            emitValue(7);

            assertEquals(new Integer(7), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            emitValue(5);

            testObserver.assertValue(5);

            emitValue(7);

            testObserver.assertValues(5, 7);
        }
    }

    /**
     * Specification that tests behavior of property when created from source (observable / property) and
     * this source emits same value
     */
    abstract class WhenSourceEmitsSameValueSpecification implements RequireProperty<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            emitValue(5);
            emitValue(5);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            emitValue(5);
            emitValue(5);

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            emitValue(5);

            testObserver.assertValue(5);

            emitValue(5);

            testObserver.assertValue(5);
        }
    }

    /**
     * Specification that tests behavior of property when different value is set
     */
    abstract class WhenSetDifferentValueSpecification implements RequireProperty<Integer>
    {
        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().setValue(7);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            getProperty().setValue(7);

            assertEquals(new Integer(7), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(5);

            getProperty().setValue(7);

            testObserver.assertValues(5, 7);
        }
    }

    /**
     * Specification that tests behavior of property when same value is set
     */
    abstract class WhenSetSameValueSpecification implements RequireProperty<Integer>
    {
        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().setValue(5);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            getProperty().setValue(5);

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(5);

            getProperty().setValue(5);

            testObserver.assertValue(5);
        }
    }

    /**
     * Specification that tests behavior of property when different value is updated
     */
    abstract class WhenUpdateDifferentValueSpecification implements RequireProperty<Integer>
    {
        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().updateValue(value -> value + 2);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            getProperty().updateValue(value -> value + 2);

            assertEquals(new Integer(7), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(5);

            getProperty().updateValue(value -> value + 2);

            testObserver.assertValues(5, 7);
        }
    }

    /**
     * Specification that tests behavior of property when same value is updated
     */
    abstract class WhenUpdateSameValueSpecification implements RequireProperty<Integer>
    {
        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().updateValue(value -> value);

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            getProperty().updateValue(value -> value);

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(5);

            getProperty().updateValue(value -> value);

            testObserver.assertValue(5);
        }
    }
}
