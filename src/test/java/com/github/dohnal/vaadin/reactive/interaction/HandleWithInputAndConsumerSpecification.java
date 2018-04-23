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
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Specification for {@link ReactiveInteraction} handled by {@link ReactiveInteraction#handle(Object, Consumer)}
 *
 * @author dohnal
 */
public interface HandleWithInputAndConsumerSpecification extends BaseInteractionSpecification
{
    abstract class HandleWithInputAndConsumerWhenSubscriberSpecification extends HandleWhenSubscriberSpecification
    {
        protected final Integer INPUT = 5;

        private ReactiveInteraction<Integer, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            consumer = Mockito.mock(Consumer.class);
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

        protected void handle()
        {
            interaction.handle(INPUT, consumer);
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            interaction.asObservable().test();

            handle();

            Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
        }

        @Nested
        @DisplayName("When interaction result is set")
        class WhenSetResult extends WhenSetResultSpecification
        {
            protected final Boolean RESULT = true;

            @Nonnull
            @Override
            public ReactiveInteraction<Integer, Boolean> getInteraction()
            {
                return interaction;
            }

            @Override
            protected void handle()
            {
                interaction.handle(consumer);
            }

            @Nullable
            @Override
            protected Boolean getResult()
            {
                return RESULT;
            }

            @Test
            @DisplayName("Consumer should be called with correct result")
            public void testConsumer()
            {
                Mockito.verify(consumer).accept(RESULT);
            }
        }

        @Nested
        @DisplayName("When interaction result is set multiple times")
        class WhenSetResultMultipleTimes extends WhenSetResultMultipleTimesSpecification
        {
            protected final Boolean RESULT = true;

            @Nonnull
            @Override
            public ReactiveInteraction<Integer, Boolean> getInteraction()
            {
                return interaction;
            }

            @Override
            protected void handle()
            {
                interaction.handle(consumer);
            }

            @Nullable
            @Override
            protected Boolean getResult()
            {
                return RESULT;
            }

            @Test
            @DisplayName("Consumer should be called once with correct result")
            public void testConsumer()
            {
                Mockito.verify(consumer).accept(RESULT);
            }
        }
    }

    abstract class HandleWithInputAndConsumerWhenNoSubscriberSpecification extends HandleWhenNoSubscriberSpecification
    {
        protected final Integer INPUT = 5;

        private ReactiveInteraction<Integer, Boolean> interaction;
        private Consumer<Boolean> consumer;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            interaction = ReactiveInteraction.create();
            consumer = Mockito.mock(Consumer.class);
        }

        @Nonnull
        @Override
        public ReactiveInteraction<Integer, Boolean> getInteraction()
        {
            return interaction;
        }

        protected void handle()
        {
            interaction.handle(INPUT, consumer);
        }

        @Test
        @DisplayName("Consumer should not be called")
        public void testConsumer()
        {
            try
            {
                handle();
            }
            catch (UnhandledInteractionException e)
            {
                Mockito.verify(consumer, Mockito.never()).accept(Mockito.anyBoolean());
            }
        }
    }
}
