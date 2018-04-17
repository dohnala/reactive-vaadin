package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncProgressFunction;
import com.github.dohnal.vaadin.reactive.Progress;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
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
 * Tests for {@link ProgressCommand} created by
 * {@link ReactiveCommand#createFromAsyncProgressFunction(AsyncProgressFunction)}
 * {@link ReactiveCommand#createFromAsyncProgressFunction(Observable, AsyncProgressFunction)}
 *
 * @author dohnal
 */
@DisplayName("Progress command from asynchronous progress function")
public class ProgressCommandFromAsyncProgressFunctionTest extends AbstractProgressCommandTest
{
    @Nested
    @DisplayName("After create command from function")
    class AfterCreateCommandFromFunction extends AfterCreateCommand<Integer, Integer>
    {
        private AsyncProgressFunction<Integer, Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionResult = new CompletableFuture<>();
            execution = Mockito.mock(AsyncProgressFunction.class);
            command = ReactiveCommand.createFromAsyncProgressFunction(execution);
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
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.any());
        }

        @Nested
        @DisplayName("After command execution started")
        class AfterExecuteStarted extends AfterExecuteProgressCommandStarted<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));
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
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));
            }

            @Nested
            @DisplayName("After command execution finished")
            class AfterExecuteFinished extends AfterExecuteProgressCommandFinished<Integer, Integer>
            {
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
            class AfterExecuteFinishedWithError extends AfterExecuteProgressCommandFinishedWithError<Integer, Integer>
            {
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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));

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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));

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
    @DisplayName("After create command from function with observable")
    class AfterCreateCommandFromAsyncProgressFunctionWitObservable extends AfterCreateCommandWithObservable<Integer, Integer>
    {
        private AsyncProgressFunction<Integer, Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(AsyncProgressFunction.class);
            executionResult = new CompletableFuture<>();
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createFromAsyncProgressFunction(testSubject, execution);
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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));
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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));
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
                return INPUT;
            }

            @Test
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.any());
            }
        }
    }
}
