package com.github.dohnal.vaadin.reactive.property;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link BehaviorSubjectProperty}
 *
 * @author dohnal
 */
@DisplayName("Behavior subject property")
public class BehaviorSubjectPropertyTest
{
    private ReactiveProperty<Integer> property;

    @Nested
    @DisplayName("After empty property is created")
    class AfterCreateEmpty
    {
        @BeforeEach
        void createEmptyProperty()
        {
            property = ReactiveProperty.empty();
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
        @DisplayName("Property should not have value")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }
    }

    @Nested
    @DisplayName("After property is created with value")
    class AfterCreateWithValue
    {
        @BeforeEach
        void createPropertyWithValue()
        {
            property = ReactiveProperty.withValue(5);
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            assertEquals(new Integer(5), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit default value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertValue(5);
        }

        @Test
        @DisplayName("Property should have value")
        public void testHasValue()
        {
            assertTrue(property.hasValue());
        }
    }

    @Nested
    @DisplayName("After property is created from observable")
    class AfterCreateFromObservable
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;

        @BeforeEach
        void createPropertyFromObservable()
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
        @DisplayName("Property should not have value")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Nested
        @DisplayName("After source observable emits value")
        class AfterSourceObservableEmitsValue
        {
            @BeforeEach
            void sourceObservableEmitsValue()
            {
                testSubject.onNext(5);
                testScheduler.triggerActions();
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                assertEquals(new Integer(5), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(5);
            }

            @Nested
            @DisplayName("After source observable emits another different value")
            class AfterSourceObservableEmitsAnotherDifferentValue
            {
                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    testSubject.onNext(7);
                    testScheduler.triggerActions();

                    assertEquals(new Integer(7), property.getValue());
                }

                @Test
                @DisplayName("Observable should emit value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertValuesAndClear(5)
                            .perform(() -> {
                                testSubject.onNext(7);
                                testScheduler.triggerActions();
                            })
                            .assertValues(7);
                }
            }

            @Nested
            @DisplayName("After source observable emits another same value")
            class AfterSourceObservableEmitsAnotherSameValue
            {
                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertValuesAndClear(5)
                            .perform(() -> {
                                testSubject.onNext(5);
                                testScheduler.triggerActions();
                            })
                            .assertNoValues();
                }
            }
        }
    }

    @Nested
    @DisplayName("After property is created from empty property")
    class AfterCreateFromEmptyProperty
    {
        private ReactiveProperty<Integer> sourceProperty;

        @BeforeEach
        void createPropertyFromObservable()
        {
            sourceProperty = ReactiveProperty.empty();
            property = ReactiveProperty.fromProperty(sourceProperty);
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
        @DisplayName("Property should not have value")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Nested
        @DisplayName("After source property emits value")
        class AfterSourcePropertyEmitsValue
        {
            @BeforeEach
            void sourceObservableEmitsValue()
            {
                sourceProperty.setValue(5);
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                assertEquals(new Integer(5), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValue(5);
            }

            @Nested
            @DisplayName("After source property emits another different value")
            class AfterSourcePropertyEmitsAnotherDifferentValue
            {
                @Test
                @DisplayName("Value should be correct")
                public void testValue()
                {
                    property.setValue(7);

                    assertEquals(new Integer(7), property.getValue());
                }

                @Test
                @DisplayName("Observable should emit correct value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertValuesAndClear(5)
                            .perform(() -> property.setValue(7))
                            .assertValues(7);
                }
            }

            @Nested
            @DisplayName("After source property emits another same value")
            class AfterSourcePropertyEmitsAnotherSameValue
            {
                @Test
                @DisplayName("Observable should not emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .assertValuesAndClear(5)
                            .perform(() -> property.setValue(5))
                            .assertNoValues();
                }
            }
        }
    }

    @Nested
    @DisplayName("After property is created from property with value")
    class AfterCreatePropertyWithValue
    {
        private ReactiveProperty<Integer> sourceProperty;

        @BeforeEach
        void createPropertyFromObservable()
        {
            sourceProperty = ReactiveProperty.withValue(5);
            property = ReactiveProperty.fromProperty(sourceProperty);
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            assertEquals(new Integer(5), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertValue(5);
        }

        @Test
        @DisplayName("Property should have value")
        public void testHasValue()
        {
            assertTrue(property.hasValue());
        }
    }

    @Nested
    @DisplayName("After value is set")
    class AfterSetValue
    {
        private ReactiveProperty<Integer> sourceProperty;

        @BeforeEach
        void createPropertyFromObservable()
        {
            property = ReactiveProperty.empty();
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            property.setValue(2);

            assertEquals(new Integer(2), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            property.asObservable().test()
                    .perform(() -> property.setValue(2))
                    .assertValue(2);
        }

        @Nested
        @DisplayName("After another different value is set")
        class AfterAnotherDifferentValueSet
        {
            @BeforeEach
            void setValue()
            {
                property.setValue(2);
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                property.setValue(7);

                assertEquals(new Integer(7), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValuesAndClear(2)
                        .perform(() -> property.setValue(7))
                        .assertValues(7);
            }
        }

        @Nested
        @DisplayName("After another same value is set")
        class AfterAnotherSameValueSet
        {
            @BeforeEach
            void setValue()
            {
                property.setValue(2);
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValuesAndClear(2)
                        .perform(() -> property.setValue(2))
                        .assertNoValues();
            }
        }
    }

    @Nested
    @DisplayName("After value is updated")
    class AfterUpdate
    {
        private ReactiveProperty<Integer> sourceProperty;

        @BeforeEach
        void create()
        {
            property = ReactiveProperty.empty();
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            property.updateValue(value -> 5);

            assertEquals(new Integer(5), property.getValue());
        }

        @Test
        @DisplayName("Observable should emit correct value")
        public void testObservable()
        {
            property.asObservable().test()
                    .perform(() -> property.updateValue(value -> 5))
                    .assertValue(5);
        }

        @Nested
        @DisplayName("After another different value is set")
        class AfterAnotherDifferentUpdate
        {
            @BeforeEach
            void setValue()
            {
                property.setValue(2);
            }

            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                property.updateValue(value -> value + 5);

                assertEquals(new Integer(7), property.getValue());
            }

            @Test
            @DisplayName("Observable should emit correct value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValuesAndClear(2)
                        .perform(() -> property.updateValue(value -> value + 5))
                        .assertValues(7);
            }
        }

        @Nested
        @DisplayName("After another same value is set")
        class AfterAnotherSameValueSet
        {
            @BeforeEach
            void setValue()
            {
                property.setValue(2);
            }

            @Test
            @DisplayName("Observable should not emit any value")
            public void testObservable()
            {
                property.asObservable().test()
                        .assertValuesAndClear(2)
                        .perform(() -> property.updateValue(value -> 2))
                        .assertNoValues();
            }
        }
    }

    @Nested
    @DisplayName("After property is subscribed")
    class AfterSubscribed
    {
        private ReactiveProperty<Integer> sourceProperty;

        @BeforeEach
        void create()
        {
            property = ReactiveProperty.empty();
        }

        @Test
        @DisplayName("Observable should emit only last value")
        public void testObservable()
        {
            property.setValue(2);
            property.updateValue(value -> value + 2);

            property.asObservable().test()
                    .assertValue(4);
        }
    }
}