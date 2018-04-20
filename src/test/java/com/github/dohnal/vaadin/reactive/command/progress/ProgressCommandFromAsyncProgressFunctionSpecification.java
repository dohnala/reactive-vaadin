package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncProgressFunction;
import com.github.dohnal.vaadin.reactive.Progress;
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
 * {@link ReactiveCommand#createFromAsyncProgressFunction(AsyncProgressFunction)}
 * {@link ReactiveCommand#createFromAsyncProgressFunction(Observable, AsyncProgressFunction)}
 *
 * @author dohnal
 */
public interface ProgressCommandFromAsyncProgressFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromAsyncProgressFunctionSpecification extends WhenCreateSpecification<Integer, Integer>
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
        @DisplayName("When command execution started")
        class WhenExecutionStarted extends WhenExecutionStartedSpecification<Integer, Integer>
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

            @Test
            @Override
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> getCommand().execute(getInput()))
                        .assertValues(0.25f, 0.5f, 0.75f, 1.0f);
            }

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));
            }

            @Nested
            @DisplayName("When command execution finished")
            class WhenExecutionFinished extends WhenExecutionFinishedSpecification<Integer, Integer>
            {
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

                @Test
                @Override
                @DisplayName("Progress observable should reset back to 0")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(1.0f)
                            .perform(this::finishExecution)
                            .assertValues(0.0f);
                }
            }

            @Nested
            @DisplayName("When command execution finished with error")
            class WhenExecutionFinishedWithError extends WhenExecutionFinishedWithErrorSpecification<Integer, Integer>
            {
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

                @Test
                @DisplayName("Progress observable should reset back to 0")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(1.0f)
                            .perform(this::finishExecution)
                            .assertValues(0.0f);
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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));

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

    abstract class WhenCreateFromAsyncProgressFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer>
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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));

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
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return executionResult;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.eq(INPUT));

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
