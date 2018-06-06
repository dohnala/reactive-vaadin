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

package org.vaadin.addons.reactive.mvvm;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ReactiveViewModel}
 *
 * @author dohnal
 */
@DisplayName("Reactive view model specification")
public class ReactiveViewModelTest
{
    private static class TestViewModel extends ReactiveViewModel
    {
        private final ReactiveProperty<Integer> property;

        private final ReactiveCommand<Integer, Integer> command;

        public TestViewModel()
        {
            this.property = createProperty();
            this.command = createCommandFromFunction(input -> input * 2);

            when(changed(property)).then(executeWithInput(command));
        }
    }

    @Nested
    @DisplayName("When new reactive view model is created")
    class WhenCreate
    {
        private TestViewModel viewModel;

        @BeforeEach
        void before()
        {
            viewModel = new TestViewModel();
        }

        @Test
        @DisplayName("IsSuppressed should be false")
        public void testIsSuppressed()
        {
            assertFalse(viewModel.isSuppressed());
        }

        @Test
        @DisplayName("IsDelayed should be false")
        public void testIsDelayed()
        {
            assertFalse(viewModel.isDelayed());
        }

        @Test
        @DisplayName("Activated observable should not emit any value")
        public void testActivated()
        {
            viewModel.activated().test().assertNoValues();
        }

        @Test
        @DisplayName("Deactivated observable should not emit any value")
        public void testDeactivated()
        {
            viewModel.deactivated().test().assertNoValues();
        }

        @Nested
        @DisplayName("When view model property is changed")
        class WhenChangeProperty
        {
            @Test
            @DisplayName("Property should be changed")
            public void testProperty()
            {
                final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                viewModel.property.setValue(5);

                assertTrue(viewModel.property.hasValue());
                assertEquals(new Integer(5), viewModel.property.getValue());
                testObserver.assertValue(5);
            }

            @Test
            @DisplayName("Command should not be executed")
            public void testCommand()
            {
                final TestObserver<Boolean> testObserver = viewModel.command.isExecuting().test();

                testObserver.assertValue(false);

                viewModel.property.setValue(5);

                testObserver.assertValue(false);
            }
        }

        @Nested
        @DisplayName("When view model is activated")
        class WhenActivate
        {
            @Test
            @DisplayName("Activated observable should emit true")
            public void testActivated()
            {
                final TestObserver<Boolean> testObserver = viewModel.activated().test();

                testObserver.assertNoValues();

                viewModel.activate();

                testObserver.assertValue(true);
            }

            @Test
            @DisplayName("Deactivated observable should not emit any value")
            public void testDeactivated()
            {
                final TestObserver<Boolean> testObserver = viewModel.deactivated().test();

                testObserver.assertNoValues();

                viewModel.activate();

                testObserver.assertNoValues();
            }

            @Nested
            @DisplayName("After view model is activated")
            class AfterActivate
            {
                private Disposable activation;

                @BeforeEach
                void before()
                {
                    activation = viewModel.activate();
                }

                @Nested
                @DisplayName("When view model property is changed")
                class WhenChangeProperty
                {
                    @Test
                    @DisplayName("Property should be changed")
                    public void testProperty()
                    {
                        final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                        viewModel.property.setValue(5);

                        assertTrue(viewModel.property.hasValue());
                        assertEquals(new Integer(5), viewModel.property.getValue());
                        testObserver.assertValue(5);
                    }

                    @Test
                    @DisplayName("Command should be executed")
                    public void testCommand()
                    {
                        final TestObserver<Integer> testObserver = viewModel.command.getResult().test();

                        viewModel.property.setValue(5);

                        testObserver.assertValue(10);
                    }
                }

                @Nested
                @DisplayName("When view model is suppressed")
                class WhenSuppressed
                {
                    private Disposable suppression;

                    @BeforeEach
                    void before()
                    {
                        suppression = viewModel.suppress();
                    }

                    @Test
                    @DisplayName("IsSuppressed should be true")
                    public void testIsSuppressed()
                    {
                        assertTrue(viewModel.isSuppressed());
                    }

                    @Nested
                    @DisplayName("When view model property is changed")
                    class WhenChangeProperty
                    {
                        @Test
                        @DisplayName("Property should not be changed")
                        public void testProperty()
                        {
                            final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                            viewModel.property.setValue(5);

                            testObserver.assertNoValues();
                        }
                    }

                    @Nested
                    @DisplayName("When view model suppression is disposed")
                    class WhenDisposed
                    {
                        @BeforeEach
                        void before()
                        {
                            suppression.dispose();
                        }

                        @Test
                        @DisplayName("IsSuppressed should be false")
                        public void testIsSuppressed()
                        {
                            assertFalse(viewModel.isSuppressed());
                        }

                        @Nested
                        @DisplayName("When view model property is changed")
                        class WhenChangeProperty
                        {
                            @Test
                            @DisplayName("Property should be changed")
                            public void testProperty()
                            {
                                final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                                viewModel.property.setValue(5);

                                assertTrue(viewModel.property.hasValue());
                                assertEquals(new Integer(5), viewModel.property.getValue());
                                testObserver.assertValue(5);
                            }

                            @Test
                            @DisplayName("Command should be executed")
                            public void testCommand()
                            {
                                final TestObserver<Integer> testObserver = viewModel.command.getResult().test();

                                viewModel.property.setValue(5);

                                testObserver.assertValue(10);
                            }
                        }
                    }
                }

                @Nested
                @DisplayName("When view model is delayed")
                class WhenDelayed
                {
                    private Disposable delay;

                    @BeforeEach
                    void before()
                    {
                        delay = viewModel.delay();
                    }

                    @Test
                    @DisplayName("IsDelayed should be true")
                    public void testIsDelayed()
                    {
                        assertTrue(viewModel.isDelayed());
                    }

                    @Nested
                    @DisplayName("When view model property is changed multiple times")
                    class WhenChangeProperty
                    {
                        @Test
                        @DisplayName("Property should not be changed")
                        public void testProperty()
                        {
                            final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                            viewModel.property.setValue(5);
                            viewModel.property.setValue(7);

                            testObserver.assertNoValues();
                        }
                    }

                    @Nested
                    @DisplayName("When view model delay is disposed")
                    class WhenDisposed
                    {
                        @Test
                        @DisplayName("IsDelayed should be false")
                        public void testIsDelayed()
                        {
                            delay.dispose();

                            assertFalse(viewModel.isDelayed());
                        }

                        @Test
                        @DisplayName("Property should be changed only once")
                        public void testProperty()
                        {
                            final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                            viewModel.property.setValue(5);
                            viewModel.property.setValue(7);

                            delay.dispose();

                            assertEquals(new Integer(7), viewModel.property.getValue());
                            testObserver.assertValue(7);
                        }

                        @Test
                        @DisplayName("Command should be executed only once")
                        public void testCommand()
                        {
                            final TestObserver<Integer> testObserver = viewModel.command.getResult().test();

                            viewModel.property.setValue(5);
                            viewModel.property.setValue(7);

                            delay.dispose();

                            testObserver.assertValue(14);
                        }
                    }
                }

                @Nested
                @DisplayName("When view model is deactivated")
                class WhenDeactivate
                {
                    @Test
                    @DisplayName("Activated observable should not emit any value")
                    public void testActivated()
                    {
                        final TestObserver<Boolean> testObserver = viewModel.activated().test();

                        testObserver.assertValue(true);

                        activation.dispose();

                        testObserver.assertValue(true);
                    }

                    @Test
                    @DisplayName("Deactivated observable should emit true")
                    public void testDeactivated()
                    {
                        final TestObserver<Boolean> testObserver = viewModel.deactivated().test();

                        testObserver.assertNoValues();

                        activation.dispose();

                        testObserver.assertValue(true);
                    }

                    @Nested
                    @DisplayName("After view model is deactivated")
                    class AfterDeactivate
                    {
                        @BeforeEach
                        void before()
                        {
                            activation.dispose();
                        }

                        @Nested
                        @DisplayName("When view model property is changed")
                        class WhenChangeProperty
                        {
                            @Test
                            @DisplayName("Property should be changed")
                            public void testProperty()
                            {
                                final TestObserver<Integer> testObserver = viewModel.property.asObservable().test();

                                viewModel.property.setValue(5);

                                assertTrue(viewModel.property.hasValue());
                                assertEquals(new Integer(5), viewModel.property.getValue());
                                testObserver.assertValue(5);
                            }

                            @Test
                            @DisplayName("Command should not be executed")
                            public void testCommand()
                            {
                                final TestObserver<Boolean> testObserver = viewModel.command.isExecuting().test();

                                testObserver.assertValue(false);

                                viewModel.property.setValue(5);

                                testObserver.assertValue(false);
                            }
                        }
                    }
                }
            }
        }
    }
}
