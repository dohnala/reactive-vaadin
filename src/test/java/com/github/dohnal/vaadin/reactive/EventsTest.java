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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests of {@link Events}
 *
 * @author dohnal
 */
@DisplayName("Events specification")
public class EventsTest implements Events
{
    @Nested
    @DisplayName("When changed event is created with observable")
    class WhenCreateChangedWithObservable
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private Observable<Integer> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            event = changed(testSubject);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When observable emits value")
        class WhenEmits
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                event.test()
                        .perform(() -> {
                            testSubject.onNext(5);
                            testScheduler.triggerActions();
                        })
                        .assertValue(5);
            }
        }
    }

    @Nested
    @DisplayName("When changed event is created with isObservable")
    class WhenCreateChangedWithIsObservable
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private Observable<Integer> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            event = changed(new IsObservable<Integer>()
            {
                @Nonnull
                @Override
                public Observable<Integer> asObservable()
                {
                    return testSubject;
                }
            });
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When observable emits value")
        class WhenEmits
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                event.test()
                        .perform(() -> {
                            testSubject.onNext(5);
                            testScheduler.triggerActions();
                        })
                        .assertValue(5);
            }
        }
    }

    @Nested
    @DisplayName("When succeed event is created with command")
    class WhenCreateSucceed
    {
        private ReactiveCommand<Integer, Integer> command;
        private Observable<Integer> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = ReactiveCommand.create(input -> input + 1);
            event = succeed(command);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecuted
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                event.test()
                        .perform(() -> command.execute(5))
                        .assertValue(6);
            }
        }
    }

    @Nested
    @DisplayName("When succeed event is created with command")
    class WhenCreateFailed
    {
        protected final RuntimeException ERROR = new RuntimeException("Error");

        private ReactiveCommand<Void, Void> command;
        private Observable<Throwable> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = ReactiveCommand.create(() -> {
                throw ERROR;
            });
            event = failed(command);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecuted
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                event.test()
                        .perform(() -> command.execute(null))
                        .assertValue(ERROR);
            }
        }
    }

    @Nested
    @DisplayName("When completed event is created")
    class WhenCreateCompleted
    {
        @Nested
        @DisplayName("With command which succeed")
        class WithCommand
        {
            private ReactiveCommand<Integer, Integer> command;
            private Observable<Integer> event;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                command = ReactiveCommand.create(input -> input + 1);
                event = completed(command);
            }

            @Test
            @DisplayName("Event should not emit any value")
            public void testEvent()
            {
                event.test()
                        .assertNoValues();
            }

            @Nested
            @DisplayName("When command is executed")
            class WhenExecuted
            {
                @Test
                @DisplayName("Event should emit correct value")
                public void testEvent()
                {
                    event.test()
                            .perform(() -> command.execute(5))
                            .assertValue(1);
                }
            }
        }

        @Nested
        @DisplayName("With command which failed")
        class WithCommandWithError
        {
            protected final RuntimeException ERROR = new RuntimeException("Error");

            private ReactiveCommand<Void, Void> command;
            private Observable<Integer> event;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                command = ReactiveCommand.create(() -> {
                    throw ERROR;
                });
                event = completed(command);
            }

            @Test
            @DisplayName("Event should not emit any value")
            public void testEvent()
            {
                event.test()
                        .assertNoValues();
            }

            @Nested
            @DisplayName("When command is executed")
            class WhenExecuted
            {
                @Test
                @DisplayName("Event should emit correct value")
                public void testEvent()
                {
                    command.getError().test();

                    event.test()
                            .perform(() -> command.execute(null))
                            .assertValue(1);
                }
            }
        }
    }

    @Nested
    @DisplayName("When invoked event is created with interaction")
    class WhenCreateInvoked
    {
        private ReactiveInteraction<Integer, Boolean> interaction;
        private Observable<InteractionContext<Integer, Boolean>> event;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            event = invoked(interaction);
        }

        @Test
        @DisplayName("Event should not emit any value")
        public void testEvent()
        {
            event.test()
                    .assertNoValues();
        }

        @Nested
        @DisplayName("When interaction is invoked")
        class WhenExecuted
        {
            @Test
            @DisplayName("Event should emit correct value")
            public void testEvent()
            {
                final List<InteractionContext<Integer, Boolean>> interactionContexts = event.test()
                        .perform(() -> interaction.invoke(5, Mockito.mock(Runnable.class)))
                        .getOnNextEvents();

                assertEquals(1, interactionContexts.size());
                assertEquals(new Integer(5), interactionContexts.get(0).getInput());
                assertFalse(interactionContexts.get(0).isHandled());
            }
        }
    }
}
