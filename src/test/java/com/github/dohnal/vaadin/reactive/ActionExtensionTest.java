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

import java.util.function.Function;
import java.util.function.Supplier;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ActionExtension}
 *
 * @author dohnal
 */
@DisplayName("Action extension specification")
public class ActionExtensionTest implements ActionExtension
{
    @Nested
    @DisplayName("When execute action is created")
    class WhenCreateExecute implements ReactiveCommandExtension
    {
        private final Integer RESULT = 7;

        private ReactiveCommand<Void, Integer> command;
        private Supplier<Observable<?>> action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = createCommand(() -> RESULT);
            action = execute(command);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            command.getResult().test().assertNoValues();
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @Test
            @DisplayName("Command should be executed")
            public void testExecution()
            {
                final TestObserver<Integer> testObserver = command.getResult().test();

                action.get().subscribe();

                testObserver.assertValue(RESULT);
            }
        }
    }

    @Nested
    @DisplayName("When execute with input action is created with custom input")
    class WhenCreateExecuteWithCustomInput implements ReactiveCommandExtension
    {
        protected final Integer INPUT = 5;
        private final Integer RESULT = 7;

        private ReactiveCommand<Integer, Integer> command;
        private Supplier<Observable<?>> action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = createCommand(input -> input + 2);
            action = executeWithInput(command, INPUT);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            command.getResult().test().assertNoValues();
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {
            @Test
            @DisplayName("Command should be executed with custom input")
            public void testExecution()
            {
                final TestObserver<Integer> testObserver = command.getResult().test();

                action.get().subscribe();

                testObserver.assertValue(RESULT);
            }
        }
    }

    @Nested
    @DisplayName("When execute with input action is created")
    class WhenCreateExecuteWithInput implements ReactiveCommandExtension
    {
        protected final Integer INPUT = 5;
        private final Integer RESULT = 7;

        private ReactiveCommand<Integer, Integer> command;
        private Function<Integer, Observable<?>> action;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            command = createCommand(input -> input + 2);
            action = executeWithInput(command);
        }

        @Test
        @DisplayName("Command should not be executed")
        public void testExecution()
        {
            command.getResult().test().assertNoValues();
        }

        @Nested
        @DisplayName("When action is called")
        class WhenCall
        {

            @Test
            @DisplayName("Command should be executed with action input")
            public void testExecution()
            {
                final TestObserver<Integer> testObserver = command.getResult().test();

                action.apply(INPUT).subscribe();

                testObserver.assertValue(RESULT);
            }
        }
    }
}
