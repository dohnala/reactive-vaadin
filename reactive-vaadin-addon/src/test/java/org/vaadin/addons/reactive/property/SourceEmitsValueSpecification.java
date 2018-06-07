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

package org.vaadin.addons.reactive.property;

import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.vaadin.addons.reactive.ReactiveProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveProperty} when source emits value
 *
 * @author dohnal
 */
public interface SourceEmitsValueSpecification extends BasePropertySpecification
{
    /**
     * Specification that tests behavior of property when created from source (observable / property)
     * and this source emits value
     */
    abstract class AbstractSourceEmitsValueSpecification implements RequireSource<Integer>
    {
        private final Integer INITIAL_VALUE = 5;

        @BeforeEach
        void setInitialValue()
        {
            emitValue(INITIAL_VALUE);
        }

        @Nested
        @DisplayName("When source emits different value")
        class WhenSourceEmitsDifferentValue
        {
            private final Integer VALUE = 7;

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(VALUE);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(VALUE);

                assertEquals(VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                emitValue(VALUE);

                testObserver.assertValues(INITIAL_VALUE, VALUE);
            }

            @Nested
            @DisplayName("After source emits different value")
            class AfterSourceEmitsDifferentValue
            {
                @BeforeEach
                void emitsValue()
                {
                    emitValue(VALUE);
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
        @DisplayName("When source emits same value")
        class WhenSourceEmitsSameValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(INITIAL_VALUE);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(INITIAL_VALUE);

                assertEquals(INITIAL_VALUE, getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(INITIAL_VALUE);

                emitValue(INITIAL_VALUE);

                testObserver.assertValues(INITIAL_VALUE, INITIAL_VALUE);
            }

            @Nested
            @DisplayName("After source emits same value")
            class AfterSourceEmitsSameValue
            {
                @BeforeEach
                void emitsValue()
                {
                    emitValue(INITIAL_VALUE);
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
}
