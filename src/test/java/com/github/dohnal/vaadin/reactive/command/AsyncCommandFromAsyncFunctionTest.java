package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncFunction;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Tests for {@link AsyncCommand} created by
 * {@link ReactiveCommand#createFromAsyncFunction(AsyncFunction)}
 * {@link ReactiveCommand#createFromAsyncFunction(Observable, AsyncFunction)}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous command from asynchronous function")
public class AsyncCommandFromAsyncFunctionTest extends AbstractAsyncCommandTest
{
    @Nested
    @DisplayName("After create command from function")
    class AfterCreateCommandFromFunction extends AfterCreateCommand<Integer, Integer>
    {
        private AsyncFunction<Integer, Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionResult = new CompletableFuture<>();
            execution = Mockito.mock(AsyncFunction.class);
            command = ReactiveCommand.createFromAsyncFunction(execution);
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
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
        }

        @Nested
        @DisplayName("After command execution started")
        class AfterExecuteStarted extends AfterExecuteCommandStarted<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Integer getInput()
            {
                return INPUT;
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(INPUT);
            }

            @Nested
            @DisplayName("After command execution finished")
            class AfterExecuteFinished extends AfterExecuteCommandFinished<Integer, Integer>
            {
                protected final Integer INPUT = 5;
                protected final Integer RESULT = 7;

                @BeforeEach
                protected void startExecution()
                {
                    getCommand().execute(getInput());
                }

                @Override
                protected void finishExecution()
                {
                    executionResult.complete(RESULT);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Integer, Integer> getCommand()
                {
                    return command;
                }

                @Nullable
                protected Integer getInput()
                {
                    return INPUT;
                }

                @Nullable
                protected Integer getCorrectResult()
                {
                    return RESULT;
                }
            }

            @Nested
            @DisplayName("After command execution finished with error")
            class AfterExecuteFinishedWithError extends AfterExecuteCommandFinishedWithError<Integer, Integer>
            {
                protected final Integer INPUT = 5;
                protected final Throwable ERROR = new RuntimeException("Error");

                @BeforeEach
                protected void startExecution()
                {
                    getCommand().execute(getInput());
                }

                @Override
                protected void finishExecution()
                {
                    executionResult.completeExceptionally(ERROR);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Integer, Integer> getCommand()
                {
                    return command;
                }

                @Nullable
                protected Integer getInput()
                {
                    return INPUT;
                }

                @Nonnull
                @Override
                protected Throwable getError()
                {
                    return ERROR;
                }
            }
        }

        @Nested
        @DisplayName("After execute")
        class AfterExecute extends AfterExecuteCommand<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
                executionResult.complete(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }
        }

        @Nested
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
                executionResult.completeExceptionally(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }
        }
    }

    @Nested
    @DisplayName("After create command from asynchronous function with observable")
    class AfterCreateCommandFromAsyncFunctionWitObservable extends AfterCreateCommandWithObservable<Integer, Integer>
    {
        private AsyncFunction<Integer, Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(AsyncFunction.class);
            executionResult = new CompletableFuture<>();
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createFromAsyncFunction(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Integer, Integer>
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
        @DisplayName("After observable emits false")
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Integer, Integer>
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
        @DisplayName("After observable emits true during execution")
        class AfterEmitsTrueDuringExecution extends AfterObservableEmitsTrueDuringExecution<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Integer getInput()
            {
                return INPUT;
            }

            @Override
            protected void emitsTrue()
            {
                testSubject.onNext(true);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("After observable emits false during execution")
        class AfterEmitsFalseDuringExecution extends AfterObservableEmitsFalseDuringExecution<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Integer getInput()
            {
                return INPUT;
            }

            @Override
            protected void emitsFalse()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("After execute disabled command")
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Integer, Integer>
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
            protected Integer getInput()
            {
                return null;
            }

            @Test
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
