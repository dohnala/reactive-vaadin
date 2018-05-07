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
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    /**
     * Specification that tests behavior of property when its change notifications are suppressed
     */
    abstract class WhenSuppressSpecification implements RequireProperty<Integer>
    {
        private Disposable disposable;

        @BeforeEach
        void suppress()
        {
            disposable = getProperty().suppress();
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(5);
                getProperty().setValue(7);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(5);
                getProperty().setValue(7);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                getProperty().setValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                getProperty().setValue(7);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateDifferentValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(5);
                getProperty().updateValue(value -> value + 2);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(5);
                getProperty().updateValue(value -> value + 2);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                getProperty().setValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                getProperty().updateValue(value -> value + 2);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When suppression is disposed")
        class WhenDisposeSuppressionSpecification
        {
            @BeforeEach
            void disposeSuppression()
            {
                getProperty().setValue(5);

                disposable.dispose();
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test()
                            .assertValue(5);
                }
            }

            @Nested
            @DisplayName("When different value is set")
            class WhenSetDifferentValue
            {
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

            @Nested
            @DisplayName("When same value is set")
            class WhenSetSameValue
            {
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

                    testObserver.assertValues(5);
                }
            }

            @Nested
            @DisplayName("When different value is updated")
            class WhenUpdateDifferentValue
            {
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

            @Nested
            @DisplayName("When same value is updated")
            class WhenUpdateSameValue
            {
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

                    testObserver.assertValues(5);
                }
            }
        }
    }

    /**
     * Specification that tests behavior of property with source when its change notifications are suppressed
     */
    abstract class WhenSuppressWithSourceSpecification implements RequireProperty<Integer>
    {
        private Disposable disposable;

        protected abstract void emitValue(final @Nonnull Integer value);

        @BeforeEach
        void suppress()
        {
            disposable = getProperty().suppress();
        }

        @Nested
        @DisplayName("When source emits different value")
        class WhenSourceEmitsDifferentValue
        {
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
                emitValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(7);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When suppression is disposed")
        class WhenDisposeSuppressionSpecification
        {
            @BeforeEach
            void disposeSuppression()
            {
                emitValue(5);

                disposable.dispose();
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test()
                            .assertValue(5);
                }
            }

            @Nested
            @DisplayName("When source emits different value")
            class WhenSourceEmitsDifferentValue
            {
                @Test
                @DisplayName("HasValue should be true")
                public void testHasValue()
                {
                    emitValue(7);

                    assertTrue(getProperty().hasValue());
                }

                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    emitValue(7);

                    assertEquals(new Integer(7), getProperty().getValue());
                }

                @Test
                @DisplayName("Observable should emit correct value")
                public void testObservable()
                {
                    final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                    testObserver.assertValue(5);

                    emitValue(7);

                    testObserver.assertValues(5, 7);
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
                    emitValue(5);

                    assertTrue(getProperty().hasValue());
                }

                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    emitValue(5);

                    assertEquals(new Integer(5), getProperty().getValue());
                }

                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                    testObserver.assertValue(5);

                    emitValue(5);

                    testObserver.assertValues(5);
                }
            }
        }
    }

    /**
     * Specification that tests behavior of property when its change notifications are suppressed when
     * running action
     */
    abstract class WhenSuppressActionSpecification implements RequireProperty<Integer>
    {
        void suppressAction()
        {
            getProperty().suppress(() -> getProperty().setValue(5));
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            suppressAction();

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            suppressAction();

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            getProperty().setValue(3);

            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(3);

            suppressAction();

            testObserver.assertValues(3);
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void before()
            {
                suppressAction();
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                getProperty().asObservable().test()
                        .assertValue(5);
            }
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
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

        @Nested
        @DisplayName("When same value is set")
        class WhenSetSameValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
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

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateDifferentValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
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

        @Nested
        @DisplayName("When same value is updated")
        class WhenUpdateSameValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
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

                testObserver.assertValues(5);
            }
        }
    }

    /**
     * Specification that tests behavior of property with source when its change notifications are suppressed when
     * running action
     */
    abstract class WhenSuppressActionWithSourceSpecification implements RequireProperty<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        void suppressAction()
        {
            getProperty().suppress(() -> emitValue(5));
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            suppressAction();

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            suppressAction();

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            emitValue(3);

            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(3);

            suppressAction();

            testObserver.assertValues(3);
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void before()
            {
                suppressAction();
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                getProperty().asObservable().test()
                        .assertValue(5);
            }
        }

        @Nested
        @DisplayName("When source emits different value")
        class WhenSourceEmitsDifferentValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
            }

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(7);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(7);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(7);

                testObserver.assertValues(5, 7);
            }
        }

        @Nested
        @DisplayName("When source emits same value")
        class WhenSourceEmitsSameValue
        {
            @BeforeEach
            void before()
            {
                suppressAction();
            }

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(5);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(5);

                assertEquals(new Integer(5), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(5);

                testObserver.assertValues(5);
            }
        }
    }

    /**
     * Specification that tests behavior of property when its change notifications are delayed
     */
    abstract class WhenDelaySpecification implements RequireProperty<Integer>
    {
        private Disposable disposable;

        @BeforeEach
        void delay()
        {
            disposable = getProperty().delay();
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(5);
                getProperty().setValue(7);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(5);
                getProperty().setValue(7);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                getProperty().setValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                getProperty().setValue(7);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateDifferentValue
        {
            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                getProperty().setValue(5);
                getProperty().updateValue(value -> value + 2);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                getProperty().setValue(5);
                getProperty().updateValue(value -> value + 2);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                getProperty().setValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                getProperty().updateValue(value -> value + 2);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When delay is disposed")
        class WhenDisposeDelaySpecification
        {
            @BeforeEach
            void disposeDelay()
            {
                getProperty().setValue(3);
                getProperty().setValue(5);

                disposable.dispose();
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test()
                            .assertValue(5);
                }
            }

            @Nested
            @DisplayName("When different value is set")
            class WhenSetDifferentValue
            {
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

            @Nested
            @DisplayName("When same value is set")
            class WhenSetSameValue
            {
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

                    testObserver.assertValues(5);
                }
            }

            @Nested
            @DisplayName("When different value is updated")
            class WhenUpdateDifferentValue
            {
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

            @Nested
            @DisplayName("When same value is updated")
            class WhenUpdateSameValue
            {
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

                    testObserver.assertValues(5);
                }
            }
        }
    }

    /**
     * Specification that tests behavior of property with source when its change notifications are delayed
     */
    abstract class WhenDelayWithSourceSpecification implements RequireProperty<Integer>
    {
        private Disposable disposable;

        protected abstract void emitValue(final @Nonnull Integer value);

        @BeforeEach
        void delay()
        {
            disposable = getProperty().delay();
        }

        @Nested
        @DisplayName("When source emits different value")
        class WhenSourceEmitsDifferentValue
        {
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
                emitValue(5);

                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(7);

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When delay is disposed")
        class WhenDisposeDelaySpecification
        {
            @BeforeEach
            void disposeDelay()
            {
                emitValue(3);
                emitValue(5);

                disposable.dispose();
            }

            @Nested
            @DisplayName("When property is subscribed")
            class WhenSubscribe
            {
                @Test
                @DisplayName("Observable should emit only last value")
                public void testObservable()
                {
                    getProperty().asObservable().test()
                            .assertValue(5);
                }
            }

            @Nested
            @DisplayName("When source emits different value")
            class WhenSourceEmitsDifferentValue
            {
                @Test
                @DisplayName("HasValue should be true")
                public void testHasValue()
                {
                    emitValue(7);

                    assertTrue(getProperty().hasValue());
                }

                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    emitValue(7);

                    assertEquals(new Integer(7), getProperty().getValue());
                }

                @Test
                @DisplayName("Observable should emit correct value")
                public void testObservable()
                {
                    final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                    testObserver.assertValue(5);

                    emitValue(7);

                    testObserver.assertValues(5, 7);
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
                    emitValue(5);

                    assertTrue(getProperty().hasValue());
                }

                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    emitValue(5);

                    assertEquals(new Integer(5), getProperty().getValue());
                }

                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                    testObserver.assertValue(5);

                    emitValue(5);

                    testObserver.assertValues(5);
                }
            }
        }
    }

    /**
     * Specification that tests behavior of property when its change notifications are delayed when
     * running action
     */
    abstract class WhenDelayActionSpecification implements RequireProperty<Integer>
    {
        void delayAction()
        {
            getProperty().delay(() -> {
                getProperty().setValue(3);
                getProperty().setValue(5);
            });
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            delayAction();

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            delayAction();

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit only last value")
        public void testObservable()
        {
            getProperty().setValue(1);

            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(1);

            delayAction();

            testObserver.assertValues(1, 5);
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void before()
            {
                delayAction();
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                getProperty().asObservable().test()
                        .assertValue(5);
            }
        }

        @Nested
        @DisplayName("When different value is set")
        class WhenSetDifferentValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
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

        @Nested
        @DisplayName("When same value is set")
        class WhenSetSameValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
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

                testObserver.assertValues(5);
            }
        }

        @Nested
        @DisplayName("When different value is updated")
        class WhenUpdateDifferentValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
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

        @Nested
        @DisplayName("When same value is updated")
        class WhenUpdateSameValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
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

                testObserver.assertValues(5);
            }
        }
    }

    /**
     * Specification that tests behavior of property with source when its change notifications are delayed when
     * running action
     */
    abstract class WhenDelayActionWithSourceSpecification implements RequireProperty<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        void delayAction()
        {
            getProperty().delay(() -> {
                emitValue(3);
                emitValue(5);
            });
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            delayAction();

            assertTrue(getProperty().hasValue());
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            delayAction();

            assertEquals(new Integer(5), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit only last value")
        public void testObservable()
        {
            emitValue(1);

            final TestObserver<Integer> testObserver = getProperty().asObservable().test();

            testObserver.assertValue(1);

            delayAction();

            testObserver.assertValues(1, 5);
        }

        @Nested
        @DisplayName("When property is subscribed")
        class WhenSubscribe
        {
            @BeforeEach
            void before()
            {
                delayAction();
            }

            @Test
            @DisplayName("Observable should emit only last value")
            public void testObservable()
            {
                getProperty().asObservable().test()
                        .assertValue(5);
            }
        }

        @Nested
        @DisplayName("When source emits different value")
        class WhenSourceEmitsDifferentValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
            }

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(7);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(7);

                assertEquals(new Integer(7), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(7);

                testObserver.assertValues(5, 7);
            }
        }

        @Nested
        @DisplayName("When source emits same value")
        class WhenSourceEmitsSameValue
        {
            @BeforeEach
            void before()
            {
                delayAction();
            }

            @Test
            @DisplayName("HasValue should be true")
            public void testHasValue()
            {
                emitValue(5);

                assertTrue(getProperty().hasValue());
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                emitValue(5);

                assertEquals(new Integer(5), getProperty().getValue());
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                final TestObserver<Integer> testObserver = getProperty().asObservable().test();

                testObserver.assertValue(5);

                emitValue(5);

                testObserver.assertValues(5);
            }
        }
    }
}
