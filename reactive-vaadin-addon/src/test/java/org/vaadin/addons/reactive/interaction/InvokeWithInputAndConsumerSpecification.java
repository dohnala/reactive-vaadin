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

package org.vaadin.addons.reactive.interaction;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.ReplaySubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.InteractionContext;
import org.vaadin.addons.reactive.ReactiveInteraction;
import org.vaadin.addons.reactive.ReactiveInteractionExtension;
import org.vaadin.addons.reactive.exceptions.AlreadyHandledInteractionException;
import org.vaadin.addons.reactive.exceptions.UnhandledInteractionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Specification for {@link ReactiveInteraction} invoked by {@link ReactiveInteraction#invoke(Object, Consumer)}
 *
 * @author dohnal
 */
public interface InvokeWithInputAndConsumerSpecification
{
    abstract class InvokeWithInputAndConsumerWhenSubscriberSpecification implements ReactiveInteractionExtension
    {
        protected final Integer INPUT = 5;

        private ReplaySubject<ReactiveInteraction<?, ?>> capturedInteractions;
        private ReactiveInteraction<Integer, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            capturedInteractions = ReplaySubject.create();
            interaction = createInteraction();
            consumer = Mockito.mock(Consumer.class);
        }

        @Nonnull
        @Override
        public <T, R> ReactiveInteraction<T, R> onCreateInteraction(final @Nonnull ReactiveInteraction<T, R> interaction)
        {
            final ReactiveInteraction<T, R> created = ReactiveInteractionExtension.super.onCreateInteraction(interaction);

            capturedInteractions.onNext(created);

            return created;
        }

        @Test
        @DisplayName("Created interaction should be captured")
        public void testCreatedInteraction()
        {
            capturedInteractions.test().assertValue(interaction);
        }

        @Test
        @DisplayName("Observable should emit correct interaction context")
        public void testObservable()
        {
            final TestObserver<InteractionContext<Integer, Boolean>> testObserver =
                    interaction.asObservable().test();

            interaction.invoke(INPUT, consumer);

            testObserver.assertValue(interactionContext ->
                    INPUT.equals(interactionContext.getInput()) && !interactionContext.isHandled());
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            interaction.asObservable().test();

            interaction.invoke(INPUT, consumer);

            Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
        }

        @Nested
        @DisplayName("When interaction is handled with no result")
        class WhenHandleWithNoResult
        {
            private InteractionContext<Integer, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void invoke()
            {
                final TestObserver<InteractionContext<Integer, Boolean>> testObserver =
                        interaction.asObservable().test();

                interaction.invoke(INPUT, consumer);

                interactionContext = (InteractionContext) testObserver.getEvents().get(0).get(0);
            }

            @Test()
            @DisplayName("NullPointerException should be thrown")
            public void testHandle()
            {
                assertThrows(NullPointerException.class, () -> interactionContext.handle());
            }
        }

        @Nested
        @DisplayName("When interaction is handled with result")
        class WhenHandleWithResult
        {
            protected final Boolean RESULT = true;

            private InteractionContext<Integer, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Integer, Boolean>> testObserver =
                        interaction.asObservable().test();

                interaction.invoke(INPUT, consumer);

                interactionContext = (InteractionContext) testObserver.getEvents().get(0).get(0);

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

            private InteractionContext<Integer, Boolean> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Integer, Boolean>> testObserver =
                        interaction.asObservable().test();

                interaction.invoke(INPUT, consumer);

                interactionContext = (InteractionContext) testObserver.getEvents().get(0).get(0);

                interactionContext.handle(RESULT);
            }

            @Test
            @DisplayName("AlreadyHandledInteractionException should be thrown")
            public void testError()
            {
                final AlreadyHandledInteractionException error = assertThrows(AlreadyHandledInteractionException.class,
                        () -> interactionContext.handle(RESULT));

                assertEquals(interaction, error.getInteraction());
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

    abstract class InvokeWithInputAndConsumerWhenNoSubscriberSpecification implements ReactiveInteractionExtension
    {
        protected final Integer INPUT = 5;

        private ReplaySubject<ReactiveInteraction<?, ?>> capturedInteractions;
        private ReactiveInteraction<Integer, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            capturedInteractions = ReplaySubject.create();
            interaction = createInteraction();
            consumer = Mockito.mock(Consumer.class);
        }

        @Nonnull
        @Override
        public <T, R> ReactiveInteraction<T, R> onCreateInteraction(final @Nonnull ReactiveInteraction<T, R> interaction)
        {
            final ReactiveInteraction<T, R> created = ReactiveInteractionExtension.super.onCreateInteraction(interaction);

            capturedInteractions.onNext(created);

            return created;
        }

        @Test
        @DisplayName("Created interaction should be captured")
        public void testCreatedInteraction()
        {
            capturedInteractions.test().assertValue(interaction);
        }

        @Test
        @DisplayName("UnhandledInteractionException should be thrown")
        public void testError()
        {
            final UnhandledInteractionException error = assertThrows(UnhandledInteractionException.class, () ->
                    interaction.invoke(INPUT, consumer));

            assertEquals(interaction, error.getInteraction());
        }

        @Test()
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            try
            {
                interaction.invoke(INPUT, consumer);
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
                interaction.invoke(INPUT, consumer);
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
            }
        }
    }
}
