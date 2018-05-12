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
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ReactiveProgressContext}
 *
 * @author dohnal
 */
@DisplayName("Reactive progress specification")
public class ReactiveProgressTest implements ReactivePropertyExtension
{
    private ReactiveProperty<Float> progressProperty;
    private ReactiveProgressContext progress;

    @Nested
    @DisplayName("When progress is created without value")
    class WhenCreateWithoutValue
    {
        @BeforeEach
        public void create()
        {
            progressProperty = createProperty();
            progress = new ReactiveProgressContext(progressProperty);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgressObservable()
        {
            progressProperty.asObservable().test().assertValue(0.0f);
        }

        @Test
        @DisplayName("Progress should be 0")
        public void testProgress()
        {
            assertEquals(0.0f, progress.getCurrentProgress());
        }
    }

    @Nested
    @DisplayName("When progress is created")
    class WhenCreate
    {
        @BeforeEach
        public void create()
        {
            progressProperty = createProperty(0.0f);
            progress = new ReactiveProgressContext(progressProperty);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgressObservable()
        {
            progressProperty.asObservable().test().assertValue(0.0f);
        }

        @Test
        @DisplayName("Progress should be 0")
        public void testProgress()
        {
            assertEquals(0.0f, progress.getCurrentProgress());
        }

        @Nested
        @DisplayName("When negative value is set")
        class WhenSetNegativeValue
        {
            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.set(-0.5f);

                testObserver.assertValue(0.0f);
            }

            @Test
            @DisplayName("Progress should be 0")
            public void testProgress()
            {
                progress.set(-0.5f);

                assertEquals(0.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When 0 is set")
        class WhenSetZero
        {
            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.set(0.0f);

                testObserver.assertValue(0.0f);
            }

            @Test
            @DisplayName("Progress should be 0")
            public void testProgress()
            {
                progress.set(0.0f);

                assertEquals(0.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When 1 is set")
        class WhenSetOne
        {
            @Test
            @DisplayName("Progress observable should emit 1")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.set(1.0f);

                testObserver.assertValues(0.0f, 1.0f);
            }

            @Test
            @DisplayName("Progress should be 1")
            public void testProgress()
            {
                progress.set(1.0f);

                assertEquals(1.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("when value bigger than 1 is set")
        class WhenSetMoreThanOne
        {
            @Test
            @DisplayName("Progress observable should emit 1")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.set(2.0f);

                testObserver.assertValues(0.0f, 1.0f);
            }

            @Test
            @DisplayName("Progress should be 1")
            public void testProgress()
            {
                progress.set(2.0f);

                assertEquals(1.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When value between 0 and 1 is set")
        class WhenSetValue
        {
            @Test
            @DisplayName("Progress observable should emit correct value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.set(0.5f);

                testObserver.assertValues(0.0f, 0.5f);
            }

            @Test
            @DisplayName("Progress should be correct")
            public void testProgress()
            {
                progress.set(0.5f);

                assertEquals(0.5f, progress.getCurrentProgress());
            }

            @Nested
            @DisplayName("When lesser value is set")
            class WhenSetLesserValue
            {
                @BeforeEach
                void before()
                {
                    progress.set(0.5f);
                }

                @Test
                @DisplayName("Progress observable should not emit any value")
                public void testProgressObservable()
                {
                    final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                    testObserver.assertValue(0.5f);

                    progress.set(0.3f);

                    testObserver.assertValue(0.5f);
                }

                @Test
                @DisplayName("Progress should be correct")
                public void testProgress()
                {
                    progress.set(0.3f);

                    assertEquals(0.5f, progress.getCurrentProgress());
                }
            }
        }

        @Nested
        @DisplayName("When negative value is added")
        class WhenAddNegativeValue
        {
            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.add(-0.5f);

                testObserver.assertValue(0.0f);
            }

            @Test
            @DisplayName("Progress should be 0")
            public void testProgress()
            {
                progress.add(-0.5f);

                assertEquals(0.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When 0 is added")
        class WhenAddZero
        {
            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.add(0.0f);

                testObserver.assertValue(0.0f);
            }

            @Test
            @DisplayName("Progress should be 0")
            public void testProgress()
            {
                progress.add(0.0f);

                assertEquals(0.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When 1 is added")
        class WhenAddOne
        {
            @Test
            @DisplayName("Progress observable should emit 1")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.add(1.0f);

                testObserver.assertValues(0.0f, 1.0f);
            }

            @Test
            @DisplayName("Progress should be 1")
            public void testProgress()
            {
                progress.add(1.0f);

                assertEquals(1.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When value bigger than 1 is added")
        class WhenAddMoreThanOne
        {
            @Test
            @DisplayName("Progress observable should emit 1")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.add(2.0f);

                testObserver.assertValues(0.0f, 1.0f);
            }

            @Test
            @DisplayName("Progress should be 1")
            public void testProgress()
            {
                progress.add(2.0f);

                assertEquals(1.0f, progress.getCurrentProgress());
            }
        }

        @Nested
        @DisplayName("When value between 0 and 1 is added")
        class WhenAddValue
        {
            @Test
            @DisplayName("Progress observable should emit correct value")
            public void testProgressObservable()
            {
                final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                testObserver.assertValue(0.0f);

                progress.add(0.5f);

                testObserver.assertValues(0.0f, 0.5f);
            }

            @Test
            @DisplayName("Progress should be correct")
            public void testProgress()
            {
                progress.add(0.5f);

                assertEquals(0.5f, progress.getCurrentProgress());
            }

            @Nested
            @DisplayName("When negative value is added")
            class WhenSetLesserValue
            {
                @BeforeEach
                void before()
                {
                    progress.set(0.5f);
                }

                @Test
                @DisplayName("Progress observable should not emit any value")
                public void testProgressObservable()
                {
                    final TestObserver<Float> testObserver = progressProperty.asObservable().test();

                    testObserver.assertValue(0.5f);

                    progress.add(-0.3f);

                    testObserver.assertValue(0.5f);
                }

                @Test
                @DisplayName("Progress should be correct")
                public void testProgress()
                {
                    progress.add(-0.3f);

                    assertEquals(0.5f, progress.getCurrentProgress());
                }
            }
        }
    }
}
