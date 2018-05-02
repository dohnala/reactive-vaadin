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

package com.github.dohnal.vaadin.reactive.command.async;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createAsync(Function, Executor)}
 * {@link ReactiveCommand#createAsync(Observable, Function, Executor)}
 *
 * @author dohnal
 */
public interface AsyncCommandFromFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromFunctionSpecification extends WhenCreateSpecification<Integer, Integer>
    {
        private Function<Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Function.class);
            command = ReactiveCommand.createAsync(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Function should not be run")
        public void testFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class WhenExecuteWithInput
        {
            @Test
            @DisplayName("IllegalArgumentException should be thrown")
            public void testExecute()
            {
                assertThrows(IllegalArgumentException.class, () -> command.execute().blockingAwait());
            }
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }

            @Test
            @DisplayName("Result observable should emit correct result")
            public void testResult()
            {
                final TestObserver<Integer> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertValue(RESULT);
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(INPUT);
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }

            @Nonnull
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(INPUT);
            }
        }

        @Nested
        @DisplayName("When subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }
        }

        @Nested
        @DisplayName("When subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer,
                Integer>
        {
            protected final Integer INPUT = 5;
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }
        }
    }

    abstract class WhenCreateFromFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer>
    {
        private Function<Integer, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void emitsTrue()
            {
                testSubject.onNext(true);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When CanExecute observable emits false")
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void emitsFalse()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When command is executed while disabled")
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            public void disableCommand()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }

            @Test
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }

    abstract class WhenCreateFromFunctionWithExecutorSpecification extends WhenCreateSpecification<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            command = ReactiveCommand.createAsync(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Function should not be run")
        public void testFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class WhenExecuteWithInput
        {
            @Test
            @DisplayName("IllegalArgumentException should be thrown")
            public void testExecute()
            {
                assertThrows(IllegalArgumentException.class, () -> command.execute());
            }
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should emit correct result")
            public void testResult()
            {
                final TestObserver<Integer> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertValue(RESULT);
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(INPUT);
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Nonnull
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(INPUT);
            }
        }

        @Nested
        @DisplayName("When subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }
        }

        @Nested
        @DisplayName("When subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer,
                Integer>
        {
            protected final Integer INPUT = 5;
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }
        }
    }

    abstract class WhenCreateFromFunctionWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Integer, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void emitsTrue()
            {
                testSubject.onNext(true);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When CanExecute observable emits false")
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void emitsFalse()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When command is executed while disabled")
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            public void disableCommand()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
