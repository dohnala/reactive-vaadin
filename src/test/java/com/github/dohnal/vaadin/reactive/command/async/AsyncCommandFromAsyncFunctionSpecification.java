package com.github.dohnal.vaadin.reactive.command.async;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncFunction;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
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
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createFromAsyncFunction(AsyncFunction)}
 * {@link ReactiveCommand#createFromAsyncFunction(Observable, AsyncFunction)}
 *
 * @author dohnal
 */
public interface AsyncCommandFromAsyncFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromAsyncFunctionSpecification extends WhenCreateSpecification<Integer, Integer>
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
        @DisplayName("When command execution started")
        class WhenExecutionStarted extends WhenExecutionStartedSpecification<Integer, Integer>
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
            @DisplayName("When command execution finished")
            class WhenExecutionFinished extends WhenExecutionFinishedSpecification<Integer, Integer>
            {
                protected final Integer INPUT = 5;
                protected final Integer RESULT = 7;

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
                protected Integer getResult()
                {
                    return RESULT;
                }
            }

            @Nested
            @DisplayName("When command execution finished with error")
            class WhenExecutionFinishedWithError extends WhenExecutionFinishedWithErrorSpecification<Integer, Integer>
            {
                protected final Integer INPUT = 5;
                protected final Throwable ERROR = new RuntimeException("Error");

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
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
                executionResult.complete(RESULT);

                super.execute();
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
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);
                executionResult.completeExceptionally(ERROR);

                super.execute();
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

    abstract class WhenCreateFromAsyncFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer>
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
        @DisplayName("When CanExecute observable emits true during execution")
        class WhenCanExecuteEmitsTrueDuringExecution extends WhenCanExecuteEmitsTrueDuringExecutionSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);

                super.startExecution();
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
        @DisplayName("When CanExecute observable emits false during execution")
        class WhenCanExecuteEmitsFalseDuringExecution extends WhenCanExecuteEmitsFalseDuringExecutionSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(executionResult);

                super.startExecution();
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
