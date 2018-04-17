package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncProgressSupplier;
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
 * {@link ReactiveCommand#createFromAsyncProgressSupplier(AsyncProgressSupplier)}
 * {@link ReactiveCommand#createFromAsyncProgressSupplier(Observable, AsyncProgressSupplier)}
 *
 * @author dohnal
 */
@DisplayName("Progress command from asynchronous progress supplier")
public class ProgressCommandFromAsyncProgressSupplierTest extends AbstractProgressCommandTest
{
    @Nested
    @DisplayName("After create command from supplier")
    class AfterCreateCommandFromSupplier extends AfterCreateCommand<Void, Integer>
    {
        private AsyncProgressSupplier<Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionResult = new CompletableFuture<>();
            execution = Mockito.mock(AsyncProgressSupplier.class);
            command = ReactiveCommand.createFromAsyncProgressSupplier(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Supplier should not be run")
        public void testSupplier()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
        }

        @Nested
        @DisplayName("After command execution started")
        class AfterExecuteStarted extends AfterExecuteProgressCommandStarted<Void, Integer>
        {
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
                }).when(execution).apply(Mockito.any(Progress.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Void getInput()
            {
                return null;
            }

            @Nonnull
            @Override
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
            }

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class));
            }

            @Nested
            @DisplayName("After command execution finished")
            class AfterExecuteFinished extends AfterExecuteProgressCommandFinished<Void, Integer>
            {
                protected final Integer RESULT = 5;

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
                public ReactiveCommand<Void, Integer> getCommand()
                {
                    return command;
                }

                @Nullable
                protected Void getInput()
                {
                    return null;
                }

                @Nullable
                protected Integer getCorrectResult()
                {
                    return RESULT;
                }
            }

            @Nested
            @DisplayName("After command execution finished with error")
            class AfterExecuteFinishedWithError extends AfterExecuteProgressCommandFinishedWithError<Void, Integer>
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
                public ReactiveCommand<Void, Integer> getCommand()
                {
                    return command;
                }

                @Nullable
                protected Void getInput()
                {
                    return null;
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
        class AfterExecute extends AfterExecuteCommand<Void, Integer>
        {
            protected final Integer RESULT = 5;

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
                }).when(execution).apply(Mockito.any(Progress.class));

                executionResult.complete(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Void getInput()
            {
                return null;
            }
        }

        @Nested
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Void, Integer>
        {
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
                }).when(execution).apply(Mockito.any(Progress.class));

                executionResult.completeExceptionally(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Void getInput()
            {
                return null;
            }
        }
    }

    @Nested
    @DisplayName("After create command from asynchronous supplier with observable")
    class AfterCreateCommandFromAsyncProgressSupplierWitObservable extends AfterCreateCommandWithObservable<Void, Integer>
    {
        private AsyncProgressSupplier<Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(AsyncProgressSupplier.class);
            executionResult = new CompletableFuture<>();
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createFromAsyncProgressSupplier(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Void, Integer>
        {

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Void, Integer>
        {

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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
        class AfterEmitsTrueDuringExecution extends AfterObservableEmitsTrueDuringExecution<Void, Integer>
        {

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
                }).when(execution).apply(Mockito.any(Progress.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Void getInput()
            {
                return null;
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
        class AfterEmitsFalseDuringExecution extends AfterObservableEmitsFalseDuringExecution<Void, Integer>
        {

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
                }).when(execution).apply(Mockito.any(Progress.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            protected Void getInput()
            {
                return null;
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
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Void, Integer>
        {
            @BeforeEach
            public void disableCommand()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected Void getInput()
            {
                return null;
            }

            @Test
            @DisplayName("Supplier should not be run")
            public void testSupplier()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
