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
 * Tests for {@link SerialActivable}
 *
 * @author dohnal
 */
@DisplayName("Serial activable specification")
public class SerialActivableTest
{
    @Nested
    @DisplayName("When new serial activable is created")
    class WhenCreate
    {
        private SerialActivable activable;

        @BeforeEach
        void before()
        {
            activable = new SerialActivable(Disposables::empty);
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
            }
        }
    }
}
