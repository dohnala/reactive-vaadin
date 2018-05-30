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
 * Specification for {@link ReactiveInteraction} invoked by {@link ReactiveInteraction#invoke(Runnable)}
 *
 * @author dohnal
 */
public interface InvokeWithRunnableSpecification
{
    abstract class InvokeWithRunnableWhenSubscriberSpecification implements ReactiveInteractionExtension
    {
        private ReplaySubject<ReactiveInteraction<?, ?>> capturedInteractions;
        private ReactiveInteraction<Void, Void> interaction;
        private Runnable runnable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            capturedInteractions = ReplaySubject.create();
            interaction = createInteraction();
            runnable = Mockito.mock(Runnable.class);
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
            final TestObserver<InteractionContext<Void, Void>> testObserver = interaction.asObservable().test();

            interaction.invoke(runnable);

            testObserver.assertValue(interactionContext ->
                    interactionContext.getInput() == null && !interactionContext.isHandled());
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            interaction.asObservable().test();

            interaction.invoke(runnable);

            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Nested
        @DisplayName("When interaction is handled")
        class WhenHandle
        {
            private InteractionContext<Void, Void> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Void, Void>> testObserver =
                        interaction.asObservable().test();

                interaction.invoke(runnable);

                interactionContext = (InteractionContext) testObserver.getEvents().get(0).get(0);

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
            private InteractionContext<Void, Void> interactionContext;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void handle()
            {
                final TestObserver<InteractionContext<Void, Void>> testObserver =
                        interaction.asObservable().test();

                interaction.invoke(runnable);

                interactionContext = (InteractionContext) testObserver.getEvents().get(0).get(0);

                interactionContext.handle();
            }

            @Test
            @DisplayName("AlreadyHandledInteractionException should be thrown")
            public void testError()
            {
                final AlreadyHandledInteractionException error = assertThrows(AlreadyHandledInteractionException.class,
                        () -> interactionContext.handle());

                assertEquals(interaction, error.getInteraction());
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

    abstract class InvokeWithRunnableWhenNoSubscriberSpecification implements ReactiveInteractionExtension
    {
        private ReplaySubject<ReactiveInteraction<?, ?>> capturedInteractions;
        private ReactiveInteraction<Integer, Boolean> interaction;
        private Runnable runnable;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            capturedInteractions = ReplaySubject.create();
            interaction = createInteraction();
            runnable = Mockito.mock(Runnable.class);
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
                    interaction.invoke(runnable));

            assertEquals(interaction, error.getInteraction());
        }

        @Test()
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            try
            {
                interaction.invoke(runnable);
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
                interaction.invoke(runnable);
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(runnable, Mockito.never()).run();
            }
        }
    }
}
