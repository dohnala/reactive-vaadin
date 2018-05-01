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

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link Actions}
 *
 * @author dohnal
 */
@DisplayName("Actions specification")
public class ActionsTest implements Actions
{
    @Nested
    @DisplayName("When execute action is created")
    class WhenCreateExecute
    {
        private ReactiveCommand<Void, Void> command;
        private Runnable action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = Mockito.mock(ReactiveCommand.class);
            action = execute(command);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            Mockito.verify(command, Mockito.never()).execute(Mockito.any());
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @BeforeEach
            protected void call()
            {
                action.run();
            }

            @Test
            @DisplayName("Command should be executed with null input")
            public void testExecution()
            {
                Mockito.verify(command).execute();
            }
        }
    }

    @Nested
    @DisplayName("When execute with input action is created with custom input")
    class WhenCreateExecuteWithCustomInput
    {
        protected final Integer INPUT = 7;

        private ReactiveCommand<Integer, Void> command;
        private Runnable action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = Mockito.mock(ReactiveCommand.class);
            action = executeWithInput(command, INPUT);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            Mockito.verify(command, Mockito.never()).execute(Mockito.any());
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @BeforeEach
            protected void call()
            {
                action.run();
            }

            @Test
            @DisplayName("Command should be executed with custom input")
            public void testExecution()
            {
                Mockito.verify(command).execute(INPUT);
            }
        }
    }

    @Nested
    @DisplayName("When execute with input action is created")
    class WhenCreateExecuteWithInput
    {
        private ReactiveCommand<Integer, Void> command;
        private Consumer<Integer> action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = Mockito.mock(ReactiveCommand.class);
            action = executeWithInput(command);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            Mockito.verify(command, Mockito.never()).execute(Mockito.any());
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void call()
            {
                action.accept(INPUT);
            }

            @Test
            @DisplayName("Command should be executed with action input")
            public void testExecution()
            {
                Mockito.verify(command).execute(INPUT);
            }
        }
    }
}
