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

package com.github.dohnal.vaadin.reactive.command;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ReactiveProgressContext}
 *
 * @author dohnal
 */
@DisplayName("Reactive progress specification")
public class ReactiveProgressTest
{
    private ReactiveProperty<Float> progressProperty;
    private ReactiveProgressContext progress;

    @Nested
    @DisplayName("When progress is created")
    class WhenCreate
    {
        @BeforeEach
        public void create()
        {
            progressProperty = ReactiveProperty.withValue(0.0f);
            progress = new ReactiveProgressContext(progressProperty);
        }

        @Test
        @DisplayName("Value should be 0")
        public void testValue()
        {
            progressProperty.asObservable().test()
                    .assertValue(0.0f);
        }

        @Nested
        @DisplayName("When negative value is set")
        class WhenSetNegativeValue
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
        @DisplayName("When zero is set")
        class WhenSetZero
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
        @DisplayName("When one is set")
        class WhenSetOne
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
        @DisplayName("when value bigger than one is set")
        class WhenSetMoreThanOne
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
        @DisplayName("When value between zero and one is set")
        class WhenSetValue
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
            @DisplayName("When lesser value is set")
            class WhenSetLesserValue
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
        @DisplayName("When negative value is added")
        class WhenAddNegativeValue
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
        @DisplayName("When zero is added")
        class WhenAddZero
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
        @DisplayName("When one is added")
        class WhenAddOne
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
        @DisplayName("When value bigger than one is added")
        class WhenAddMoreThanOne
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
        @DisplayName("When value between zero and one is added")
        class WhenAddValue
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
            @DisplayName("When negative value is added")
            class WhenSetLesserValue
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
