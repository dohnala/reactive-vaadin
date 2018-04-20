package com.github.dohnal.vaadin.reactive.command.composite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncFunction;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author dohnal
 */
public interface CompositeCommandSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromNoCommandsSpecification
    {
        @Test
        @DisplayName("Exception should be thrown")
        public void testCreate()
        {
            assertThrows(IllegalArgumentException.class, () -> ReactiveCommand.createComposite(Lists.newArrayList()));
        }
    }

    abstract class WhenCreateFromCommandsSpecification extends WhenCreateSpecification<Integer, List<Integer>>
    {
        private AsyncFunction<Integer, Integer> executionA;
        private CompletableFuture<Integer> executionResultA;
        private ReactiveCommand<Integer, Integer> commandA;

        private AsyncFunction<Integer, Integer> executionB;
        private CompletableFuture<Integer> executionResultB;
        private ReactiveCommand<Integer, Integer> commandB;

        private ReactiveCommand<Integer, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionA = Mockito.mock(AsyncFunction.class);
            executionResultA = new CompletableFuture<>();
            commandA = ReactiveCommand.createFromAsyncFunction(executionA);

            executionB = Mockito.mock(AsyncFunction.class);
            executionResultB = new CompletableFuture<>();
            commandB = ReactiveCommand.createFromAsyncFunction(executionB);

            command = ReactiveCommand.createComposite(Lists.newArrayList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, List<Integer>> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Child commands should not be executed")
        public void testChildCommands()
        {
            Mockito.verify(executionA, Mockito.never()).apply(Mockito.any());
            Mockito.verify(executionB, Mockito.never()).apply(Mockito.any());
        }

        @Nested
        @DisplayName("When child command is executed")
        class WhenExecuteChild
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT_A = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                getCommand().getResult().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                getCommand().getError().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                getCommand().canExecute().test()
                        .assertValuesAndClear(true)
                        .perform(() -> commandA.execute(INPUT))
                        .assertValues(false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                getCommand().isExecuting().test()
                        .assertValuesAndClear(false)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                getCommand().getExecutionCount().test()
                        .assertValuesAndClear(0)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                getCommand().hasBeenExecuted().test()
                        .assertValuesAndClear(false)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("When command execution started")
        class WhenExecuteStarted extends WhenExecutionStartedSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }

            @Test
            @DisplayName("Child commands should be executed")
            public void testChildCommands()
            {
                getCommand().execute(getInput());

                Mockito.verify(executionA).apply(INPUT);
                Mockito.verify(executionB).apply(INPUT);
            }

            @Nested
            @DisplayName("When child command execution is finished")
            class WhenExecuteChildFinished
            {
                protected final Integer RESULT_A = 7;

                @BeforeEach
                protected void startExecution()
                {
                    getCommand().execute(getInput());
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    getCommand().getResult().test()
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    getCommand().getError().test()
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    getCommand().canExecute().test()
                            .assertValuesAndClear(false)
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    getCommand().isExecuting().test()
                            .assertValuesAndClear(true)
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    getCommand().getExecutionCount().test()
                            .assertValuesAndClear(0)
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    getCommand().hasBeenExecuted().test()
                            .assertValuesAndClear(false)
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.0f)
                            .perform(() -> executionResultA.complete(RESULT_A))
                            .assertValue(0.5f);
                }
            }

            @Nested
            @DisplayName("When child command execution is finished with error")
            class WhenExecuteChildFinishedWithError
            {
                protected final Throwable ERROR_A = new RuntimeException("Error A");

                @BeforeEach
                protected void startExecution()
                {
                    getCommand().execute(getInput());
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    getCommand().getResult().test()
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Error observable should emit correct error")
                public void testError()
                {
                    getCommand().getError().test()
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertValue(ERROR_A);
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    getCommand().canExecute().test()
                            .assertValuesAndClear(false)
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    getCommand().isExecuting().test()
                            .assertValuesAndClear(true)
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    getCommand().getExecutionCount().test()
                            .assertValuesAndClear(0)
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    getCommand().hasBeenExecuted().test()
                            .assertValuesAndClear(false)
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.0f)
                            .perform(() -> executionResultA.completeExceptionally(ERROR_A))
                            .assertValue(0.5f);
                }
            }

            @Nested
            @DisplayName("When child command is executed again after it finished")
            class WhenExecuteChildAgain
            {
                protected final Integer RESULT_A = 7;

                @BeforeEach
                protected void startExecution()
                {
                    getCommand().execute(getInput());
                    executionResultA.complete(RESULT_A);
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    getCommand().getResult().test()
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    getCommand().getError().test()
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    getCommand().canExecute().test()
                            .assertValuesAndClear(false)
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    getCommand().isExecuting().test()
                            .assertValuesAndClear(true)
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    getCommand().getExecutionCount().test()
                            .assertValuesAndClear(0)
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    getCommand().hasBeenExecuted().test()
                            .assertValuesAndClear(false)
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }

                @Test
                @DisplayName("Progress observable should not emit any value")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.5f)
                            .perform(() -> commandA.execute(INPUT))
                            .assertNoValues();
                }
            }

            @Nested
            @DisplayName("When command execution is finished")
            class WhenExecuteFinished extends WhenExecutionFinishedSpecification<Integer, List<Integer>>
            {
                protected final Integer RESULT_A = 7;
                protected final Integer RESULT_B = 9;
                protected final List<Integer> RESULT = Lists.newArrayList(RESULT_A, RESULT_B);

                @Override
                protected void finishExecution()
                {
                    executionResultA.complete(RESULT_A);
                    executionResultB.complete(RESULT_B);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Integer, List<Integer>> getCommand()
                {
                    return command;
                }

                @Nullable
                @Override
                protected Integer getInput()
                {
                    return INPUT;
                }

                @Nullable
                @Override
                protected List<Integer> getResult()
                {
                    return RESULT;
                }

                @Test
                @DisplayName("Result observable should emit correct result")
                public void testResult()
                {
                    final List<Integer> result = getCommand().getResult().test()
                            .perform(this::finishExecution)
                            .getOnNextEvents().get(0);

                    assertIterableEquals(RESULT, result);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.0f)
                            .perform(this::finishExecution)
                            .assertValues(0.5f, 1.0f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished with one error")
            class WhenExecuteFinishedWithOneError extends WhenExecutionFinishedWithErrorSpecification<Integer, List<Integer>>
            {
                protected final Throwable ERROR_A = new RuntimeException("Error A");
                protected final Integer RESULT_B = 9;

                @Override
                protected void finishExecution()
                {
                    executionResultA.completeExceptionally(ERROR_A);
                    executionResultB.complete(RESULT_B);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Integer, List<Integer>> getCommand()
                {
                    return command;
                }

                @Nullable
                @Override
                protected Integer getInput()
                {
                    return INPUT;
                }

                @Nonnull
                @Override
                protected Throwable getError()
                {
                    return ERROR_A;
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.0f)
                            .perform(this::finishExecution)
                            .assertValues(0.5f, 1.0f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished with multiple errors")
            class WhenExecuteFinishedWithMultipleErrors extends WhenExecutionFinishedWithErrorSpecification<Integer, List<Integer>>
            {
                protected final Throwable ERROR_A = new RuntimeException("Error A");
                protected final Throwable ERROR_B = new RuntimeException("Error B");

                @Override
                protected void finishExecution()
                {
                    executionResultA.completeExceptionally(ERROR_A);
                    executionResultB.completeExceptionally(ERROR_B);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Integer, List<Integer>> getCommand()
                {
                    return command;
                }

                @Nullable
                @Override
                protected Integer getInput()
                {
                    return INPUT;
                }

                @Nonnull
                @Override
                protected Throwable getError()
                {
                    return ERROR_A;
                }

                @Test
                @DisplayName("Error observable should emit correct errors")
                public void testError()
                {
                    getCommand().getError().test()
                            .perform(this::finishExecution)
                            .assertValues(ERROR_A, ERROR_B);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    getCommand().getProgress().test()
                            .assertValuesAndClear(0.0f)
                            .perform(this::finishExecution)
                            .assertValues(0.5f, 1.0f);
                }
            }
        }

        @Nested
        @DisplayName("When child command is executed after execution")
        class WhenExecuteChildAfterExecute
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT_A = 7;
            protected final Integer RESULT_B = 9;

            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.complete(RESULT_B);

                getCommand().execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                getCommand().getResult().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                getCommand().getError().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                getCommand().canExecute().test()
                        .assertValuesAndClear(true)
                        .perform(() -> commandA.execute(INPUT))
                        .assertValues(false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                getCommand().isExecuting().test()
                        .assertValuesAndClear(false)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                getCommand().getExecutionCount().test()
                        .assertValuesAndClear(1)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                getCommand().hasBeenExecuted().test()
                        .assertValuesAndClear(true)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(1.0f)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("When child command is executed after execution with error")
        class WhenExecuteChildAfterExecuteWithError
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR_A = new RuntimeException("Error A");
            protected final Throwable ERROR_B = new RuntimeException("Error B");

            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.completeExceptionally(ERROR_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.completeExceptionally(ERROR_B);

                getCommand().execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                getCommand().getResult().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                getCommand().getError().test()
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                getCommand().canExecute().test()
                        .assertValuesAndClear(true)
                        .perform(() -> commandA.execute(INPUT))
                        .assertValues(false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                getCommand().isExecuting().test()
                        .assertValuesAndClear(false)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                getCommand().getExecutionCount().test()
                        .assertValuesAndClear(1)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                getCommand().hasBeenExecuted().test()
                        .assertValuesAndClear(true)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(1.0f)
                        .perform(() -> commandA.execute(INPUT))
                        .assertNoValues();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT_A = 7;
            protected final Integer RESULT_B = 9;

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.complete(RESULT_B);

                super.execute();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR_A = new RuntimeException("Error A");
            protected final Throwable ERROR_B = new RuntimeException("Error B");

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.completeExceptionally(ERROR_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.completeExceptionally(ERROR_B);

                super.execute();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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

    abstract class WhenCreateFromCommandsWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, List<Integer>>
    {
        private AsyncFunction<Integer, Integer> executionA;
        private CompletableFuture<Integer> executionResultA;
        private ReactiveCommand<Integer, Integer> commandA;

        private AsyncFunction<Integer, Integer> executionB;
        private CompletableFuture<Integer> executionResultB;
        private ReactiveCommand<Integer, Integer> commandB;

        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionA = Mockito.mock(AsyncFunction.class);
            executionResultA = new CompletableFuture<>();
            commandA = ReactiveCommand.createFromAsyncFunction(executionA);

            executionB = Mockito.mock(AsyncFunction.class);
            executionResultB = new CompletableFuture<>();
            commandB = ReactiveCommand.createFromAsyncFunction(executionB);

            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createComposite(testSubject, Lists.newArrayList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, List<Integer>> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, List<Integer>>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, List<Integer>>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        class WhenCanExecuteEmitsTrueDuringExecution extends WhenCanExecuteEmitsTrueDuringExecutionSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;

            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);

                super.startExecution();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        class WhenCanExecuteEmitsFalseDuringExecution extends WhenCanExecuteEmitsFalseDuringExecutionSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;

            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);

                super.startExecution();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, List<Integer>>
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
            public ReactiveCommand<Integer, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected Integer getInput()
            {
                return null;
            }

            @Test
            @DisplayName("Child commands should not be executed")
            public void testChildCommands()
            {
                Mockito.verify(executionA, Mockito.never()).apply(Mockito.any());
                Mockito.verify(executionB, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
