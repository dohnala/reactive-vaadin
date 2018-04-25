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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import com.github.dohnal.vaadin.reactive.InteractionContext;
import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.AlreadyHandledInteractionException;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base specifications for {@link ReactiveInteraction}
 *
 * @author dohnal
 */
public interface BaseInteractionSpecification
{
    /**
     * Base interface for tests which needs command
     *
     * @param <T> type of interaction input
     * @param <R> type of interaction result
     */
    interface RequireInteraction<T, R>
    {
        @Nonnull
        ReactiveInteraction<T, R> getInteraction();
    }

    /**
     * Specification that tests behavior of interaction after it is invoked when handler is subscribed
     */
    abstract class InvokeWhenSubscriberSpecification implements RequireInteraction<Integer, Boolean>
    {
        protected abstract void invoke();

        @Nullable
        protected abstract Integer getInput();

        @Test()
        @DisplayName("Observable should emit correct interaction context")
        public void testObservable()
        {
            final List<InteractionContext<Integer, Boolean>> interactionContexts =
                    getInteraction().asObservable().test()
                            .perform(this::invoke)
                            .getOnNextEvents();

            assertEquals(1, interactionContexts.size());
            assertEquals(getInput(), interactionContexts.get(0).getInput());
            assertFalse(interactionContexts.get(0).isHandled());
        }
    }

    /**
     * Specification that tests behavior of interaction after it is handled
     */
    abstract class WhenHandleSpecification implements RequireInteraction<Integer, Boolean>
    {
        private InteractionContext<Integer, Boolean> interactionContext;

        protected abstract void invoke();

        @Nullable
        protected abstract Boolean getResult();

        @BeforeEach
        protected void handle()
        {
            final List<InteractionContext<Integer, Boolean>> interactionContexts =
                    getInteraction().asObservable().test()
                            .perform(this::invoke)
                            .getOnNextEvents();

            interactionContext = interactionContexts.get(0);

            interactionContext.handle(getResult());
        }

        @Test()
        @DisplayName("IsHandled should be true")
        public void testObservable()
        {
            assertTrue(interactionContext.isHandled());
        }
    }

    /**
     * Specification that tests behavior of interaction after it is handled multiple times
     */
    abstract class WhenHandleMultipleTimesSpecification implements RequireInteraction<Integer, Boolean>
    {
        private InteractionContext<Integer, Boolean> interactionContext;

        protected abstract void invoke();

        @Nullable
        protected abstract Boolean getResult();

        @BeforeEach
        protected void handle()
        {
            final List<InteractionContext<Integer, Boolean>> interactionContexts =
                    getInteraction().asObservable().test()
                            .perform(this::invoke)
                            .getOnNextEvents();

            interactionContext = interactionContexts.get(0);

            interactionContext.handle(getResult());
        }

        @Test
        @DisplayName("AlreadyHandledInteractionException should be thrown")
        public void testError()
        {
            assertThrows(AlreadyHandledInteractionException.class, () -> interactionContext.handle(getResult()));
        }

        @Test()
        @DisplayName("IsHandled should be true")
        public void testObservable()
        {
            try
            {
                interactionContext.handle(getResult());
            }
            catch (AlreadyHandledInteractionException e)
            {
                assertTrue(interactionContext.isHandled());
            }
        }
    }

    /**
     * Specification that tests behavior of interaction after it is invoked when no handler is subscribed
     */
    abstract class InvokeWhenNoSubscriberSpecification implements RequireInteraction<Integer, Boolean>
    {
        protected abstract void invoke();

        @Test
        @DisplayName("UnhandledInteractionException should be thrown")
        public void testError()
        {
            assertThrows(UnhandledInteractionException.class, this::invoke);
        }

        @Test()
        @DisplayName("Observable should not emit any value")
        public void testObservable()
        {
            try
            {
                invoke();
            }
            catch (UnhandledInteractionException e)
            {
                getInteraction().asObservable().test()
                        .assertNoValues();
            }
        }
    }
}
