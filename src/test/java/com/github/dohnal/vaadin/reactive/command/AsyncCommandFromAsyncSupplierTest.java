package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncSupplier;
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
 * {@link ReactiveCommand#createFromAsyncSupplier(AsyncSupplier)}
 * {@link ReactiveCommand#createFromAsyncSupplier(Observable, AsyncSupplier)}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous command from asynchronous supplier")
public class AsyncCommandFromAsyncSupplierTest extends AbstractAsyncCommandTest
{
    @Nested
    @DisplayName("After create command from supplier")
    class AfterCreateCommandFromSupplier extends AfterCreateCommand<Void, Integer>
    {
        private AsyncSupplier<Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionResult = new CompletableFuture<>();
            execution = Mockito.mock(AsyncSupplier.class);
            command = ReactiveCommand.createFromAsyncSupplier(execution);
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
            Mockito.verify(execution, Mockito.never()).get();
        }

        @Nested
        @DisplayName("After command execution started")
        class AfterExecuteStarted extends AfterExecuteCommandStarted<Void, Integer>
        {
            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.get()).thenReturn(executionResult);
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

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                command.execute(getInput());

                Mockito.verify(execution).get();
            }

            @Nested
            @DisplayName("After command execution finished")
            class AfterExecuteFinished extends AfterExecuteCommandFinished<Void, Integer>
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
            class AfterExecuteFinishedWithError extends AfterExecuteCommandFinishedWithError<Void, Integer>
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
                Mockito.when(execution.get()).thenReturn(executionResult);
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
                Mockito.when(execution.get()).thenReturn(executionResult);
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
    class AfterCreateCommandFromAsyncSupplierWitObservable extends AfterCreateCommandWithObservable<Void, Integer>
    {
        private AsyncSupplier<Integer> execution;
        private CompletableFuture<Integer> executionResult;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(AsyncSupplier.class);
            executionResult = new CompletableFuture<>();
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createFromAsyncSupplier(testSubject, execution);
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
                Mockito.when(execution.get()).thenReturn(executionResult);
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
                Mockito.when(execution.get()).thenReturn(executionResult);
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

                Mockito.verify(execution, Mockito.never()).get();
            }
        }
    }
}
