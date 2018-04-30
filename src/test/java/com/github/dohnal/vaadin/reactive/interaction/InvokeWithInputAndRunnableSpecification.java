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

package com.github.dohnal.vaadin.reactive.interaction;

import java.util.List;

import com.github.dohnal.vaadin.reactive.InteractionContext;
import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.AlreadyHandledInteractionException;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveInteraction} invoked by {@link ReactiveInteraction#invoke(Object, Runnable)}
 *
 * @author dohnal
 */
public interface InvokeWithInputAndRunnableSpecification
{
    abstract class InvokeWithInputAndRunnableWhenSubscriberSpecification
    {
        protected final Integer INPUT = 5;

        private ReactiveInteraction<Integer, Void> interaction;
        private Runnable runnable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            runnable = Mockito.mock(Runnable.class);
        }

        @Test
        @DisplayName("Observable should emit correct interaction context")
        public void testObservable()
        {
            final List<InteractionContext<Integer, Void>> interactionContexts =
                    interaction.asObservable().test()
                            .perform(() -> interaction.invoke(INPUT, runnable))
                            .getOnNextEvents();

            assertEquals(1, interactionContexts.size());
            assertEquals(INPUT, interactionContexts.get(0).getInput());
            assertFalse(interactionContexts.get(0).isHandled());
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            interaction.asObservable().test();

            interaction.invoke(INPUT, runnable);

            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Nested
        @DisplayName("When interaction is handled")
        class WhenHandle
        {
            private InteractionContext<Integer, Void> interactionContext;

            @BeforeEach
            protected void handle()
            {
                final List<InteractionContext<Integer, Void>> interactionContexts =
                        interaction.asObservable().test()
                                .perform(() -> interaction.invoke(INPUT, runnable))
                                .getOnNextEvents();

                interactionContext = interactionContexts.get(0);

                interactionContext.handle();
            }

            @Test()
            @DisplayName("IsHandled should be true")
            public void testObservable()
            {
                assertTrue(interactionContext.isHandled());
            }

            @Test
            @DisplayName("Runnable should be called")
            public void testRunnable()
            {
                Mockito.verify(runnable).run();
            }
        }

        @Nested
        @DisplayName("When interaction is handled multiple times")
        class WhenHandleMultipleTimes
        {
            private InteractionContext<Integer, Void> interactionContext;

            @BeforeEach
            protected void handle()
            {
                final List<InteractionContext<Integer, Void>> interactionContexts =
                        interaction.asObservable().test()
                                .perform(() -> interaction.invoke(INPUT, runnable))
                                .getOnNextEvents();

                interactionContext = interactionContexts.get(0);

                interactionContext.handle();
            }

            @Test
            @DisplayName("AlreadyHandledInteractionException should be thrown")
            public void testError()
            {
                assertThrows(AlreadyHandledInteractionException.class, () -> interactionContext.handle());
            }

            @Test()
            @DisplayName("IsHandled should be true")
            public void testObservable()
            {
                try
                {
                    interactionContext.handle();
                }
                catch (AlreadyHandledInteractionException e)
                {
                    assertTrue(interactionContext.isHandled());
                }
            }

            @Test
            @DisplayName("Runnable should be called once")
            public void testRunnable()
            {
                Mockito.verify(runnable).run();
            }
        }
    }

    abstract class InvokeWithInputAndRunnableWhenNoSubscriberSpecification
    {
        protected final Integer INPUT = 5;

        private ReactiveInteraction<Integer, Boolean> interaction;
        private Runnable runnable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            runnable = Mockito.mock(Runnable.class);
        }

        @Test
        @DisplayName("UnhandledInteractionException should be thrown")
        public void testError()
        {
            assertThrows(UnhandledInteractionException.class, () -> interaction.invoke(INPUT, runnable));
        }

        @Test()
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            try
            {
                interaction.invoke(INPUT, runnable);
            }
            catch (UnhandledInteractionException e)
            {
                interaction.asObservable().test()
                        .assertNoValues();
            }
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            try
            {
                interaction.invoke(INPUT, runnable);
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(runnable, Mockito.never()).run();
            }
        }
    }
}
