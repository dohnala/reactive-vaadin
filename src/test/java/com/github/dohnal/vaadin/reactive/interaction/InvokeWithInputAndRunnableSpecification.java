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

import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Specification for {@link ReactiveInteraction} invoked by {@link ReactiveInteraction#invoke(Object, Runnable)}
 *
 * @author dohnal
 */
public interface InvokeWithInputAndRunnableSpecification extends BaseInteractionSpecification
{
    abstract class InvokeWithInputAndRunnableWhenSubscriberSpecification extends InvokeWhenSubscriberSpecification
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

        @Nonnull
        @Override
        public ReactiveInteraction<Integer, Boolean> getInteraction()
        {
            return interaction;
        }

        @Nullable
        @Override
        protected Integer getInput()
        {
            return INPUT;
        }

        protected void invoke()
        {
            interaction.invoke(INPUT, runnable);
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            interaction.asObservable().test();

            invoke();

            Mockito.verify(runnable, Mockito.never()).run();
        }

        @Nested
        @DisplayName("When interaction is handled")
        class WhenHandle extends WhenHandleSpecification
        {
            protected final Boolean RESULT = true;

            @Nonnull
            @Override
            public ReactiveInteraction<Integer, Boolean> getInteraction()
            {
                return interaction;
            }

            @Override
            protected void invoke()
            {
                interaction.invoke(runnable);
            }

            @Nullable
            @Override
            protected Boolean getResult()
            {
                return RESULT;
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
        class WhenHandleMultipleTimes extends WhenHandleMultipleTimesSpecification
        {
            protected final Boolean RESULT = true;

            @Nonnull
            @Override
            public ReactiveInteraction<Integer, Boolean> getInteraction()
            {
                return interaction;
            }

            @Override
            protected void invoke()
            {
                interaction.invoke(runnable);
            }

            @Nullable
            @Override
            protected Boolean getResult()
            {
                return RESULT;
            }

            @Test
            @DisplayName("Runnable should be called once")
            public void testRunnable()
            {
                Mockito.verify(runnable).run();
            }
        }
    }

    abstract class InvokeWithInputAndRunnableWhenNoSubscriberSpecification extends InvokeWhenNoSubscriberSpecification
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

        @Nonnull
        @Override
        public ReactiveInteraction<Integer, Boolean> getInteraction()
        {
            return interaction;
        }

        protected void invoke()
        {
            interaction.invoke(INPUT, runnable);
        }

        @Test
        @DisplayName("Runnable should not be called")
        public void testRunnable()
        {
            try
            {
                invoke();
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(runnable, Mockito.never()).run();
            }
        }
    }
}
