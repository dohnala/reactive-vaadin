package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
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

        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> emitValue(7))
                    .assertValue(7);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            emitValue(7);

            assertTrue(getProperty().hasValue());
        }
    }

    /**
     * Specification that tests behavior of property when created from source (observable / property) and
     * this source emits same value
     */
    abstract class WhenSourceEmitsSameValueSpecification implements RequireProperty<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> emitValue(5))
                    .assertNoValues();
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            emitValue(5);

            assertTrue(getProperty().hasValue());
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> getProperty().setValue(7))
                    .assertValue(7);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().setValue(7);

            assertTrue(getProperty().hasValue());
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> getProperty().setValue(5))
                    .assertNoValues();
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().setValue(5);

            assertTrue(getProperty().hasValue());
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> getProperty().updateValue(value -> value + 2))
                    .assertValue(7);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().updateValue(value -> value + 2);

            assertTrue(getProperty().hasValue());
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
            getProperty().asObservable().test()
                    .assertValuesAndClear(5)
                    .perform(() -> getProperty().updateValue(value -> value))
                    .assertNoValues();
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            getProperty().updateValue(value -> value);

            assertTrue(getProperty().hasValue());
        }
    }

    /**
     * Specification that tests behavior of property when it is subscribed
     */
    abstract class WhenSubscribeSpecification implements RequireProperty<Integer>
    {
        @BeforeEach
        void setInitialValue()
        {
            getProperty().setValue(5);
            getProperty().setValue(7);
        }

        @Test
        @DisplayName("Value should be correct")
        public void testValue()
        {
            assertEquals(new Integer(7), getProperty().getValue());
        }

        @Test
        @DisplayName("Observable should emit only last value")
        public void testObservable()
        {
            getProperty().asObservable().test()
                    .assertValue(7);
        }

        @Test
        @DisplayName("HasValue should be true")
        public void testHasValue()
        {
            assertTrue(getProperty().hasValue());
        }
    }
}
