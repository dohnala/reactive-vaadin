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

package com.github.dohnal.vaadin.reactive.activable;

import io.reactivex.disposables.Disposables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link CompositeActivable}
 *
 * @author dohnal
 */
@DisplayName("Composite activable specification")
public class CompositeActivableTest
{
    @Nested
    @DisplayName("When new composite activable is created")
    class WhenCreate
    {
        private CompositeActivable activable;

        @BeforeEach
        void before()
        {
            activable = new CompositeActivable();
        }

        @Test
        @DisplayName("IsActivated should be false")
        public void testIsActivated()
        {
            assertFalse(activable.isActivated());
        }

        @Test
        @DisplayName("IsDisposed should be false")
        public void testIsDisposed()
        {
            assertFalse(activable.asDisposable().isDisposed());
        }

        @Nested
        @DisplayName("When activable are added")
        class WhenAdd
        {
            private SerialActivable firstActivable;
            private SerialActivable secondActivable;

            @BeforeEach
            void before()
            {
                firstActivable = new SerialActivable(Disposables::empty);
                secondActivable = new SerialActivable(Disposables::empty);

                activable.add(firstActivable);
                activable.add(secondActivable);
            }

            @Test
            @DisplayName("IsActivated should be false")
            public void testIsActivated()
            {
                assertFalse(activable.isActivated());
            }

            @Test
            @DisplayName("IsDisposed should be false")
            public void testIsDisposed()
            {
                assertFalse(activable.asDisposable().isDisposed());
            }

            @Test
            @DisplayName("Children IsActivated should be false")
            public void testChildrenIsActivated()
            {
                assertFalse(firstActivable.isActivated());
                assertFalse(secondActivable.isActivated());
            }

            @Test
            @DisplayName("Children IsDisposed should be false")
            public void testChildrenIsDisposed()
            {
                assertFalse(firstActivable.asDisposable().isDisposed());
                assertFalse(secondActivable.asDisposable().isDisposed());
            }

            @Nested
            @DisplayName("When activated")
            class WhenActivate
            {
                @BeforeEach
                void before()
                {
                    activable.activate();
                }

                @Test
                @DisplayName("IsActivated should be true")
                public void testIsActivated()
                {
                    assertTrue(activable.isActivated());
                }

                @Test
                @DisplayName("IsDisposed should be false")
                public void testIsDisposed()
                {
                    assertFalse(activable.asDisposable().isDisposed());
                }

                @Test
                @DisplayName("Children IsActivated should be true")
                public void testChildrenIsActivated()
                {
                    assertTrue(firstActivable.isActivated());
                    assertTrue(secondActivable.isActivated());
                }

                @Test
                @DisplayName("Children IsDisposed should be false")
                public void testChildrenIsDisposed()
                {
                    assertFalse(firstActivable.asDisposable().isDisposed());
                    assertFalse(secondActivable.asDisposable().isDisposed());
                }

                @Nested
                @DisplayName("When deactivated")
                class WhenDeactivate
                {
                    @BeforeEach
                    void before()
                    {
                        activable.deactivate();
                    }

                    @Test
                    @DisplayName("IsActivated should be false")
                    public void testIsActivated()
                    {
                        assertFalse(activable.isActivated());
                    }

                    @Test
                    @DisplayName("IsDisposed should be false")
                    public void testIsDisposed()
                    {
                        assertFalse(activable.asDisposable().isDisposed());
                    }

                    @Test
                    @DisplayName("Children IsActivated should be false")
                    public void testChildrenIsActivated()
                    {
                        assertFalse(firstActivable.isActivated());
                        assertFalse(secondActivable.isActivated());
                    }

                    @Test
                    @DisplayName("Children IsDisposed should be false")
                    public void testChildrenIsDisposed()
                    {
                        assertFalse(firstActivable.asDisposable().isDisposed());
                        assertFalse(secondActivable.asDisposable().isDisposed());
                    }
                }

                @Nested
                @DisplayName("When disposed")
                class WhenDisposed
                {
                    @BeforeEach
                    void before()
                    {
                        activable.asDisposable().dispose();
                    }

                    @Test
                    @DisplayName("IsActivated should be false")
                    public void testIsActivated()
                    {
                        assertFalse(activable.isActivated());
                    }

                    @Test
                    @DisplayName("IsDisposed should be true")
                    public void testIsDisposed()
                    {
                        assertTrue(activable.asDisposable().isDisposed());
                    }

                    @Test
                    @DisplayName("Children IsActivated should be false")
                    public void testChildrenIsActivated()
                    {
                        assertFalse(firstActivable.isActivated());
                        assertFalse(secondActivable.isActivated());
                    }

                    @Test
                    @DisplayName("Children IsDisposed should be true")
                    public void testChildrenIsDisposed()
                    {
                        assertTrue(firstActivable.asDisposable().isDisposed());
                        assertTrue(secondActivable.asDisposable().isDisposed());
                    }
                }
            }

            @Nested
            @DisplayName("When deactivated")
            class WhenDeactivate
            {
                @BeforeEach
                void before()
                {
                    activable.deactivate();
                }

                @Test
                @DisplayName("IsActivated should be false")
                public void testIsActivated()
                {
                    assertFalse(activable.isActivated());
                }

                @Test
                @DisplayName("IsDisposed should be false")
                public void testIsDisposed()
                {
                    assertFalse(activable.asDisposable().isDisposed());
                }

                @Test
                @DisplayName("Children IsActivated should be false")
                public void testChildrenIsActivated()
                {
                    assertFalse(firstActivable.isActivated());
                    assertFalse(secondActivable.isActivated());
                }

                @Test
                @DisplayName("Children IsDisposed should be false")
                public void testChildrenIsDisposed()
                {
                    assertFalse(firstActivable.asDisposable().isDisposed());
                    assertFalse(secondActivable.asDisposable().isDisposed());
                }
            }

            @Nested
            @DisplayName("When disposed")
            class WhenDisposed
            {
                @BeforeEach
                void before()
                {
                    activable.asDisposable().dispose();
                }

                @Test
                @DisplayName("IsActivated should be false")
                public void testIsActivated()
                {
                    assertFalse(activable.isActivated());
                }

                @Test
                @DisplayName("IsDisposed should be true")
                public void testIsDisposed()
                {
                    assertTrue(activable.asDisposable().isDisposed());
                }

                @Test
                @DisplayName("Children IsActivated should be false")
                public void testChildrenIsActivated()
                {
                    assertFalse(firstActivable.isActivated());
                    assertFalse(secondActivable.isActivated());
                }

                @Test
                @DisplayName("Children IsDisposed should be true")
                public void testChildrenIsDisposed()
                {
                    assertTrue(firstActivable.asDisposable().isDisposed());
                    assertTrue(secondActivable.asDisposable().isDisposed());
                }

                @Nested
                @DisplayName("When activated")
                class WhenActivate
                {
                    @BeforeEach
                    void before()
                    {
                        activable.activate();
                    }

                    @Test
                    @DisplayName("IsActivated should be false")
                    public void testIsActivated()
                    {
                        assertFalse(activable.isActivated());
                    }

                    @Test
                    @DisplayName("IsDisposed should be true")
                    public void testIsDisposed()
                    {
                        assertTrue(activable.asDisposable().isDisposed());
                    }

                    @Test
                    @DisplayName("Children IsActivated should be false")
                    public void testChildrenIsActivated()
                    {
                        assertFalse(firstActivable.isActivated());
                        assertFalse(secondActivable.isActivated());
                    }

                    @Test
                    @DisplayName("Children IsDisposed should be true")
                    public void testChildrenIsDisposed()
                    {
                        assertTrue(firstActivable.asDisposable().isDisposed());
                        assertTrue(secondActivable.asDisposable().isDisposed());
                    }
                }
            }

            @Nested
            @DisplayName("When activable are cleared")
            class WhenClear
            {
                @BeforeEach
                void before()
                {
                    activable.clear();
                }

                @Test
                @DisplayName("IsActivated should be false")
                public void testIsActivated()
                {
                    assertFalse(activable.isActivated());
                }

                @Test
                @DisplayName("IsDisposed should be false")
                public void testIsDisposed()
                {
                    assertFalse(activable.asDisposable().isDisposed());
                }

                @Test
                @DisplayName("Children IsActivated should be false")
                public void testChildrenIsActivated()
                {
                    assertFalse(firstActivable.isActivated());
                    assertFalse(secondActivable.isActivated());
                }

                @Test
                @DisplayName("Children IsDisposed should be true")
                public void testChildrenIsDisposed()
                {
                    assertTrue(firstActivable.asDisposable().isDisposed());
                    assertTrue(secondActivable.asDisposable().isDisposed());
                }
            }
        }
    }
}
