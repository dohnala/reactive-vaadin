package com.github.dohnal.vaadin.reactive.command.progress;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.command.ReactiveProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ReactiveProgress}
 *
 * @author dohnal
 */
@DisplayName("Reactive progress")
public class ReactiveProgressTest
{
    private ReactiveProperty<Float> progressProperty;
    private ReactiveProgress progress;

    @Nested
    @DisplayName("After create from property")
    class AfterCreate
    {
        @BeforeEach
        public void create()
        {
            progressProperty = ReactiveProperty.withValue(0.0f);
            progress = new ReactiveProgress(progressProperty);
        }

        @Test
        @DisplayName("Value should be 0")
        public void testValue()
        {
            progressProperty.asObservable().test()
                    .assertValue(0.0f);
        }

        @Nested
        @DisplayName("After set negative value")
        class AfterSetNegativeValue
        {
            @Test
            @DisplayName("Value should not change")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.set(-0.5f))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("After set zero")
        class AfterSetZero
        {
            @Test
            @DisplayName("Value should not change")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.set(0.0f))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("After set one")
        class AfterSetOne
        {
            @Test
            @DisplayName("Value should be one")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.set(1.0f))
                        .assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("After set value bigger than one")
        class AfterSetMoreThanOne
        {
            @Test
            @DisplayName("Value should be one")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.set(2.0f))
                        .assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("After set value between zero and one")
        class AfterSetValue
        {
            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.set(0.5f))
                        .assertValue(0.5f);
            }

            @Nested
            @DisplayName("After set lesser value")
            class AfterSetLesserValue
            {
                @Test
                @DisplayName("Value should not change")
                public void testValue()
                {
                    progress.set(0.5f);

                    progressProperty.asObservable().test()
                            .assertValuesAndClear(0.5f)
                            .perform(() -> progress.set(0.3f))
                            .assertNoValues();
                }
            }
        }

        @Nested
        @DisplayName("After add negative value")
        class AfterAddNegativeValue
        {
            @Test
            @DisplayName("Value should not change")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.add(-0.5f))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("After add zero")
        class AfterAddZero
        {
            @Test
            @DisplayName("Value should not change")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.add(0.0f))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("After add one")
        class AfterAddOne
        {
            @Test
            @DisplayName("Value should be one")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.add(1.0f))
                        .assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("After add value bigger than one")
        class AfterAddMoreThanOne
        {
            @Test
            @DisplayName("Value should be one")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.add(2.0f))
                        .assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("After add value between zero and one")
        class AfterAddValue
        {
            @Test
            @DisplayName("Value should be correct")
            public void testValue()
            {
                progressProperty.asObservable().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> progress.add(0.5f))
                        .assertValue(0.5f);
            }

            @Nested
            @DisplayName("After add negative value")
            class AfterSetLesserValue
            {
                @Test
                @DisplayName("Value should not change")
                public void testValue()
                {
                    progress.set(0.5f);

                    progressProperty.asObservable().test()
                            .assertValuesAndClear(0.5f)
                            .perform(() -> progress.add(-0.3f))
                            .assertNoValues();
                }
            }
        }
    }
}
