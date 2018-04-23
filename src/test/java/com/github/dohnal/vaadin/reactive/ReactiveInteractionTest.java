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

import com.github.dohnal.vaadin.reactive.interaction.HandleWithConsumerSpecification;
import com.github.dohnal.vaadin.reactive.interaction.HandleWithInputAndConsumerSpecification;
import com.github.dohnal.vaadin.reactive.interaction.HandleWithInputAndRunnableSpecification;
import com.github.dohnal.vaadin.reactive.interaction.HandleWithRunnableSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveInteraction}
 *
 * @author dohnal
 */
@DisplayName("Reactive interaction specification")
public class ReactiveInteractionTest implements
        HandleWithInputAndConsumerSpecification,
        HandleWithConsumerSpecification,
        HandleWithInputAndRunnableSpecification,
        HandleWithRunnableSpecification
{
    @Nested
    @DisplayName("When new reactive interaction is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("When interaction is handled with input and consumer")
        class WhenHandledWithInputAndConsumer
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends HandleWithInputAndConsumerWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends HandleWithInputAndConsumerWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is handled with consumer")
        class WhenHandledWithConsumer
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends HandleWithConsumerWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends HandleWithConsumerWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is handled with input and runnable")
        class WhenHandledWithInputAndRunnable
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends HandleWithInputAndRunnableWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends HandleWithInputAndRunnableWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is handled with runnable")
        class WhenHandledWithRunnable
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends HandleWithRunnableWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends HandleWithRunnableWhenNoSubscriberSpecification {}
        }
    }
}
