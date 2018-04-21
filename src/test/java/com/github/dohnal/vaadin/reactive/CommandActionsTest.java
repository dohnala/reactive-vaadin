package com.github.dohnal.vaadin.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link CommandActions}
 *
 * @author dohnal
 */
@DisplayName("Command actions specification")
public class CommandActionsTest implements CommandActions
{
    @Nested
    @DisplayName("When execute action is created")
    class WhenCreateExecute
    {
        private ReactiveCommand<Void, Void> command;
        private Action<Object> action;

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
                action.call(5);
            }

            @Test
            @DisplayName("Command should be executed with null input")
            public void testExecution()
            {
                Mockito.verify(command).execute(null);
            }
        }
    }

    @Nested
    @DisplayName("When execute with input action is created with custom input")
    class WhenCreateExecuteWithCustomInput
    {
        protected final Integer INPUT = 7;

        private ReactiveCommand<Integer, Void> command;
        private Action<Object> action;

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
                action.call(5);
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
        private Action<Integer> action;

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
                action.call(INPUT);
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
