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

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.exceptions.ReadOnlyPropertyException;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty#setValue(Object)}
 *
 * @author dohnal
 */
public interface SetValueSpecification extends BasePropertySpecification
{
    /**
     * Specification that tests behavior of property when value is set
     */
    abstract class AbstractSetValueSpecification implements RequireProperty<Integer>
    {
        private final Integer INITIAL_VALUE = 5;

        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(INITIAL_VALUE);
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue
        {
            private final Integer VALUE = 7;

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(VALUE);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(VALUE);

                assertEquals(VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                getProperty().setValue(VALUE);

                testObserver.assertValues(INITIAL_VALUE, VALUE);
            }

            @Nested
            @DisplayName("After different value is set")
            class AfterSetDifferentValue
            {
                @BeforeEach
                void setValue()
                {
                    getProperty().setValue(VALUE);
                }

                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test().assertValue(VALUE);
                }
            }
        }

        @Nested
        @DisplayName("When same value is set")
        class WhenSetSameValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(INITIAL_VALUE);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(INITIAL_VALUE);

                assertEquals(INITIAL_VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                getProperty().setValue(INITIAL_VALUE);

                testObserver.assertValue(INITIAL_VALUE);
            }

            @Nested
            @DisplayName("After same value is set")
            class AfterSetSameValue
            {
                @BeforeEach
                void setValue()
                {
                    getProperty().setValue(INITIAL_VALUE);
                }

                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test().assertValue(INITIAL_VALUE);
                }
            }
        }
    }

    /**
     * Specification that tests behavior of read-only property when value is set
     */
    abstract class AbstractSetValueReadOnlySpecification implements RequireProperty<Integer>
    {
        @Nested
        @DisplayName("When value is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("ReadOnlyPropertyException should be thrown")
            public void testError()
            {
                final ReadOnlyPropertyException error = assertThrows(ReadOnlyPropertyException.class, () ->
                        getProperty().setValue(5));

                assertEquals(getProperty(), error.getProperty());
            }
        }
    }
}
