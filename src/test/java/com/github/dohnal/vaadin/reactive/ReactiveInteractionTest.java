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

import com.github.dohnal.vaadin.reactive.interaction.InvokeWithConsumerSpecification;
import com.github.dohnal.vaadin.reactive.interaction.InvokeWithInputAndConsumerSpecification;
import com.github.dohnal.vaadin.reactive.interaction.InvokeWithInputAndRunnableSpecification;
import com.github.dohnal.vaadin.reactive.interaction.InvokeWithRunnableSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveInteraction}
 *
 * @author dohnal
 */
@DisplayName("Reactive interaction specification")
public class ReactiveInteractionTest implements
        InvokeWithInputAndConsumerSpecification,
        InvokeWithConsumerSpecification,
        InvokeWithInputAndRunnableSpecification,
        InvokeWithRunnableSpecification
{
    @Nested
    @DisplayName("When new reactive interaction is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("When interaction is invoked with input and consumer")
        class WhenHandledWithInputAndConsumer
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends InvokeWithInputAndConsumerWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends InvokeWithInputAndConsumerWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is invoked with consumer")
        class WhenHandledWithConsumer
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends InvokeWithConsumerWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends InvokeWithConsumerWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is invoked with input and runnable")
        class WhenHandledWithInputAndRunnable
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends InvokeWithInputAndRunnableWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends InvokeWithInputAndRunnableWhenNoSubscriberSpecification {}
        }

        @Nested
        @DisplayName("When interaction is invoked with runnable")
        class WhenHandledWithRunnable
        {
            @Nested
            @DisplayName("When handler is subscribed")
            class WhenSubscriber extends InvokeWithRunnableWhenSubscriberSpecification {}

            @Nested
            @DisplayName("When no handler is subscribed")
            class WhenNoSubscribed extends InvokeWithRunnableWhenNoSubscriberSpecification {}
        }
    }
}
