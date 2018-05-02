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

import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.InteractionContext;
import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.AlreadyHandledInteractionException;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveInteraction} invoked by {@link ReactiveInteraction#invoke(Consumer)}
 *
 * @author dohnal
 */
public interface InvokeWithConsumerSpecification
{
    abstract class InvokeWithConsumerWhenSubscriberSpecification
    {
        private ReactiveInteraction<Void, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            consumer = Mockito.mock(Consumer.class);
        }

        @Test
        @DisplayName("Observable should emit correct interaction context")
        public void testObservable()
        {
            final TestObserver<InteractionContext<Void, Boolean>> testObserver = interaction.asObservable().test();

            interaction.invoke(consumer);

            testObserver.assertValue(interactionContext ->
                    interactionContext.getInput() == null && !interactionContext.isHandled());
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            interaction.asObservable().test();

            interaction.invoke(consumer);

            Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
        }

        @Nested
        @DisplayName("When interaction is handled with no result")
        class WhenHandleWithNoResult
        {
            private InteractionContext<Void, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void invoke()
            {
                final TestObserver<InteractionContext<Void, Boolean>> testObserver = interaction.asObservable().test();

                interaction.invoke(consumer);

                interactionContext = (InteractionContext)testObserver.getEvents().get(0).get(0);
            }

            @Test()
            @DisplayName("IllegalArgumentException should be thrown")
            public void testHandle()
            {
                assertThrows(IllegalArgumentException.class, () -> interactionContext.handle());
            }
        }

        @Nested
        @DisplayName("When interaction is handled with result")
        class WhenHandleWithResult
        {
            protected final Boolean RESULT = true;

            private InteractionContext<Void, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Void, Boolean>> testObserver = interaction.asObservable().test();

                interaction.invoke(consumer);

                interactionContext = (InteractionContext)testObserver.getEvents().get(0).get(0);

                interactionContext.handle(RESULT);
            }

            @Test()
            @DisplayName("IsHandled should be true")
            public void testObservable()
            {
                assertTrue(interactionContext.isHandled());
            }

            @Test
            @DisplayName("Consumer should be called with correct result")
            public void testConsumer()
            {
                Mockito.verify(consumer).accept(RESULT);
            }
        }

        @Nested
        @DisplayName("When interaction is handled multiple times")
        class WhenHandleMultipleTimes
        {
            protected final Boolean RESULT = true;

            private InteractionContext<Void, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Void, Boolean>> testObserver = interaction.asObservable().test();

                interaction.invoke(consumer);

                interactionContext = (InteractionContext)testObserver.getEvents().get(0).get(0);

                interactionContext.handle(RESULT);
            }

            @Test
            @DisplayName("AlreadyHandledInteractionException should be thrown")
            public void testError()
            {
                assertThrows(AlreadyHandledInteractionException.class, () -> interactionContext.handle(RESULT));
            }

            @Test()
            @DisplayName("IsHandled should be true")
            public void testObservable()
            {
                try
                {
                    interactionContext.handle(RESULT);
                }
                catch (AlreadyHandledInteractionException e)
                {
                    assertTrue(interactionContext.isHandled());
                }
            }

            @Test
            @DisplayName("Consumer should be called once with correct result")
            public void testConsumer()
            {
                Mockito.verify(consumer).accept(RESULT);
            }
        }
    }

    abstract class InvokeWithConsumerWhenNoSubscriberSpecification
    {
        private ReactiveInteraction<Integer, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            consumer = Mockito.mock(Consumer.class);
        }

        @Test
        @DisplayName("UnhandledInteractionException should be thrown")
        public void testError()
        {
            assertThrows(UnhandledInteractionException.class, () -> interaction.invoke(consumer));
        }

        @Test()
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            try
            {
                interaction.invoke(consumer);
            }
            catch (UnhandledInteractionException e)
            {
                interaction.asObservable().test()
                        .assertNoValues();
            }
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            try
            {
                interaction.invoke(consumer);
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
            }
        }
    }
}
