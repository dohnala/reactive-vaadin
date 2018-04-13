package com.github.dohnal.vaadin.reactive.binder;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
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
 * Tests for {@link ObservablePropertyBinder}
 *
 * @author dohnal
 */
@DisplayName("Binding observable to property")
public class ObservablePropertyBinderTest implements ReactiveBinder
{
    @Nested
    @DisplayName("After bind observable to empty property")
    class AfterBindObservableToProperty
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        void bindObservableToProperty()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            property = ReactiveProperty.empty();

            binder = bind(testSubject).to(property);
        }

        @Test
        @DisplayName("Property value should be null")
        public void testValue()
        {
            assertNull(property.getValue());
        }

        @Test
        @DisplayName("Property observable shouldn't emit any value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Property shouldn't have value")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Nested
        @DisplayName("After source observable emits value")
        class AfterSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be correct")
            public void testValue()
            {
                testSubject.onNext(5);
                testScheduler.triggerActions();

                assertEquals(new Integer(5), property.getValue());
            }

            @Test
            @DisplayName("Property observable should emit correct value")
            public void testObservable()
            {
                property.asObservable().test()
                        .perform(() -> {
                            testSubject.onNext(5);
                            testScheduler.triggerActions();
                        })
                        .assertValue(5);
            }

            @Test
            @DisplayName("Property should have value")
            public void testHasValue()
            {
                testSubject.onNext(5);
                testScheduler.triggerActions();

                assertTrue(property.hasValue());
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindObservableFromProperty()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source observable emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Property value should be null")
                public void testValue()
                {
                    testSubject.onNext(5);
                    testScheduler.triggerActions();

                    assertNull(property.getValue());
                }

                @Test
                @DisplayName("Property observable shouldn't emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .perform(() -> {
                                testSubject.onNext(5);
                                testScheduler.triggerActions();
                            })
                            .assertNoValues();
                }

                @Test
                @DisplayName("Property shouldn't have value")
                public void testHasValue()
                {
                    testSubject.onNext(5);
                    testScheduler.triggerActions();

                    assertFalse(property.hasValue());
                }
            }
        }
    }

    @Nested
    @DisplayName("After bind property to empty property")
    class AfterBindPropertyToProperty
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        void bindPropertyToProperty()
        {
            sourceProperty = ReactiveProperty.empty();
            property = ReactiveProperty.empty();

            binder = bind(sourceProperty).to(property);
        }

        @Test
        @DisplayName("Property value should be null")
        public void testValue()
        {
            assertNull(property.getValue());
        }

        @Test
        @DisplayName("Property observable shouldn't emit any value")
        public void testObservable()
        {
            property.asObservable().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Property shouldn't have value")
        public void testHasValue()
        {
            assertFalse(property.hasValue());
        }

        @Nested
        @DisplayName("After source property emits value")
        class AfterSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be correct")
            public void testValue()
            {
                sourceProperty.setValue(5);

                assertEquals(new Integer(5), property.getValue());
            }

            @Test
            @DisplayName("Property observable should emit correct value")
            public void testObservable()
            {
                property.asObservable().test()
                        .perform(() -> sourceProperty.setValue(5))
                        .assertValue(5);
            }

            @Test
            @DisplayName("Property should have value")
            public void testHasValue()
            {
                sourceProperty.setValue(5);

                assertTrue(property.hasValue());
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindPropertyFromProperty()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source property emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Property value should be null")
                public void testValue()
                {
                    sourceProperty.setValue(5);

                    assertNull(property.getValue());
                }

                @Test
                @DisplayName("Property observable shouldn't emit any value")
                public void testObservable()
                {
                    property.asObservable().test()
                            .perform(() -> sourceProperty.setValue(5))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Property shouldn't have value")
                public void testHasValue()
                {
                    sourceProperty.setValue(5);

                    assertFalse(property.hasValue());
                }
            }
        }
    }
}
