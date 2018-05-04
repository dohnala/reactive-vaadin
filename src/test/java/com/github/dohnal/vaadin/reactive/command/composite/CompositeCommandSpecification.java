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

package com.github.dohnal.vaadin.reactive.command.composite;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncFunction;
import com.github.dohnal.vaadin.reactive.AsyncSupplier;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Specification for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createCompositeCommand(List)}
 * {@link ReactiveCommandFactory#createCompositeCommand(Observable, List)}
 *
 * @author dohnal
 */
public interface CompositeCommandSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromNoCommandsSpecification implements ReactiveCommandFactory
    {
        @Test
        @DisplayName("Exception should be thrown")
        public void testCreate()
        {
            assertThrows(IllegalArgumentException.class, () -> createCompositeCommand(new ArrayList<>()));
        }
    }

    abstract class WhenCreateFromCommandsWithNoInputSpecification extends WhenCreateSpecification<Void, List<Integer>>
            implements ReactiveCommandFactory
    {
        private AsyncSupplier<Integer> executionA;
        private CompletableFuture<Integer> executionResultA;
        private ReactiveCommand<Void, Integer> commandA;

        private AsyncSupplier<Integer> executionB;
        private CompletableFuture<Integer> executionResultB;
        private ReactiveCommand<Void, Integer> commandB;

        private ReactiveCommand<Void, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionA = Mockito.mock(AsyncSupplier.class);
            executionResultA = new CompletableFuture<>();
            commandA = new AsyncCommand<>(Observable.just(true), executionA);

            executionB = Mockito.mock(AsyncSupplier.class);
            executionResultB = new CompletableFuture<>();
            commandB = new AsyncCommand<>(Observable.just(true), executionB);

            command = createCompositeCommand(Arrays.asList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, List<Integer>> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Child commands should not be executed")
        public void testChildCommands()
        {
            Mockito.verify(executionA, Mockito.never()).get();
            Mockito.verify(executionB, Mockito.never()).get();
        }

        @Nested
        @DisplayName("When child command is executed")
        class WhenExecuteChild
        {
            protected final Integer RESULT_A = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute();

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute();

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(0);

                commandA.execute();

                testObserver.assertValue(0);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(false);

                commandA.execute();

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                commandA.execute();

                testObserver.assertValue(0.0f);
            }
        }

        @Nested
        @DisplayName("When command execution started")
        class WhenExecuteStarted extends WhenExecutionStartedSpecification<Void, List<Integer>>
        {
            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                Mockito.when(executionB.get()).thenReturn(executionResultB);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }

            @Test
            @DisplayName("Child commands should be executed")
            public void testChildCommands()
            {
                execute();

                Mockito.verify(executionA).get();
                Mockito.verify(executionB).get();
            }

            @Nested
            @DisplayName("When child command execution is finished")
            class WhenExecuteChildFinished
            {
                protected final Integer RESULT_A = 7;

                @BeforeEach
                protected void startExecution()
                {
                    execute();
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    executionResultA.complete(RESULT_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    executionResultA.complete(RESULT_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValues(0.0f, 0.5f);

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
                    execute();
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should emit correct error")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(ERROR_A);
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValues(0.0f, 0.5f);
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
                    execute();
                    executionResultA.complete(RESULT_A);
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    commandA.execute();

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    commandA.execute();

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    commandA.execute();

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    commandA.execute();

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    commandA.execute();

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    commandA.execute();

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should not emit any value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.5f);

                    commandA.execute();

                    testObserver.assertValue(0.5f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished")
            class WhenExecuteFinished extends WhenExecutionFinishedSpecification<Void, List<Integer>>
            {
                protected final Integer RESULT_A = 7;
                protected final Integer RESULT_B = 9;
                protected final List<Integer> RESULT = Arrays.asList(RESULT_A, RESULT_B);

                @Override
                protected void finishExecution()
                {
                    executionResultA.complete(RESULT_A);
                    executionResultB.complete(RESULT_B);
                }

                @Nonnull
                @Override
                public ReactiveCommand<Void, List<Integer>> getCommand()
                {
                    return command;
                }

                @Override
                protected void execute()
                {
                    command.execute();
                }

                @Test
                @SuppressWarnings("unchecked")
                @DisplayName("Result observable should emit correct result")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    finishExecution();

                    final List<Integer> result = (List) testObserver.getEvents().get(0).get(0);

                    assertIterableEquals(RESULT, result);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished with one error")
            class WhenExecuteFinishedWithOneError extends WhenExecutionFinishedWithErrorSpecification<Void, List<Integer>>
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
                public ReactiveCommand<Void, List<Integer>> getCommand()
                {
                    return command;
                }

                @Override
                protected void execute()
                {
                    command.execute();
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
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished with multiple errors")
            class WhenExecuteFinishedWithMultipleErrors extends WhenExecutionFinishedWithErrorSpecification<Void, List<Integer>>
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
                public ReactiveCommand<Void, List<Integer>> getCommand()
                {
                    return command;
                }

                @Override
                protected void execute()
                {
                    command.execute();
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
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    finishExecution();

                    testObserver.assertValues(ERROR_A, ERROR_B);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
                }
            }
        }

        @Nested
        @DisplayName("When child command is executed after execution")
        class WhenExecuteChildAfterExecute
        {
            protected final Integer RESULT_A = 7;
            protected final Integer RESULT_B = 9;

            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);

                Mockito.when(executionB.get()).thenReturn(executionResultB);
                executionResultB.complete(RESULT_B);

                command.execute();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute();

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute();

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(1);

                commandA.execute();

                testObserver.assertValue(1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(true);

                commandA.execute();

                testObserver.assertValue(true);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(1.0f);

                commandA.execute();

                testObserver.assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("When child command is executed after execution with error")
        class WhenExecuteChildAfterExecuteWithError
        {
            protected final Throwable ERROR_A = new RuntimeException("Error A");
            protected final Throwable ERROR_B = new RuntimeException("Error B");

            @BeforeEach
            protected void execute()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                executionResultA.completeExceptionally(ERROR_A);

                Mockito.when(executionB.get()).thenReturn(executionResultB);
                executionResultB.completeExceptionally(ERROR_B);

                command.execute();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute();

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute();

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(1);

                commandA.execute();

                testObserver.assertValue(1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(true);

                commandA.execute();

                testObserver.assertValue(true);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(1.0f);

                commandA.execute();

                testObserver.assertValue(1.0f);
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, List<Integer>>
        {
            protected final Integer RESULT_A = 7;
            protected final Integer RESULT_B = 9;

            @Override
            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);

                Mockito.when(executionB.get()).thenReturn(executionResultB);
                executionResultB.complete(RESULT_B);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, List<Integer>>
        {
            protected final Throwable ERROR_A = new RuntimeException("Error A");
            protected final Throwable ERROR_B = new RuntimeException("Error B");

            @Override
            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(executionA.get()).thenReturn(executionResultA);
                executionResultA.completeExceptionally(ERROR_A);

                Mockito.when(executionB.get()).thenReturn(executionResultB);
                executionResultB.completeExceptionally(ERROR_B);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }
        }
    }

    abstract class WhenCreateFromCommandsWithInputSpecification extends WhenCreateSpecification<Integer, List<Integer>>
            implements ReactiveCommandFactory
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
            commandA = new AsyncCommand<>(Observable.just(true), executionA);

            executionB = Mockito.mock(AsyncFunction.class);
            executionResultB = new CompletableFuture<>();
            commandB = new AsyncCommand<>(Observable.just(true), executionB);

            command = createCompositeCommand(Arrays.asList(commandA, commandB));
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
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute(INPUT);

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute(INPUT);

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(0);

                commandA.execute(INPUT);

                testObserver.assertValue(0);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(false);

                commandA.execute(INPUT);

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                commandA.execute(INPUT);

                testObserver.assertValue(0.0f);
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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Child commands should be executed")
            public void testChildCommands()
            {
                execute();

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
                    execute();
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    executionResultA.complete(RESULT_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    executionResultA.complete(RESULT_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    executionResultA.complete(RESULT_A);

                    testObserver.assertValues(0.0f, 0.5f);

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
                    execute();
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should emit correct error")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(ERROR_A);
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should emit correct value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    executionResultA.completeExceptionally(ERROR_A);

                    testObserver.assertValues(0.0f, 0.5f);
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
                    execute();

                    executionResultA.complete(RESULT_A);
                }

                @Test
                @DisplayName("Result observable should not emit any value")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    commandA.execute(INPUT);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("Error observable should not emit any value")
                public void testError()
                {
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    commandA.execute(INPUT);

                    testObserver.assertNoValues();
                }

                @Test
                @DisplayName("CanExecute observable should not emit any value")
                public void testCanExecute()
                {
                    final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                    testObserver.assertValue(false);

                    commandA.execute(INPUT);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("IsExecuting observable should not emit any value")
                public void testIsExecuting()
                {
                    final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                    testObserver.assertValue(true);

                    commandA.execute(INPUT);

                    testObserver.assertValue(true);
                }

                @Test
                @DisplayName("ExecutionCount observable should not emit any value")
                public void testExecutionCount()
                {
                    final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                    testObserver.assertValue(0);

                    commandA.execute(INPUT);

                    testObserver.assertValue(0);
                }

                @Test
                @DisplayName("HasBeenExecuted observable should not emit any value")
                public void testHasBeenExecuted()
                {
                    final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                    testObserver.assertValue(false);

                    commandA.execute(INPUT);

                    testObserver.assertValue(false);
                }

                @Test
                @DisplayName("Progress observable should not emit any value")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.5f);

                    commandA.execute(INPUT);

                    testObserver.assertValue(0.5f);
                }
            }

            @Nested
            @DisplayName("When command execution is finished")
            class WhenExecuteFinished extends WhenExecutionFinishedSpecification<Integer, List<Integer>>
            {
                protected final Integer RESULT_A = 7;
                protected final Integer RESULT_B = 9;
                protected final List<Integer> RESULT = Arrays.asList(RESULT_A, RESULT_B);

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

                @Override
                protected void execute()
                {
                    command.execute(INPUT);
                }

                @Test
                @SuppressWarnings("unchecked")
                @DisplayName("Result observable should emit correct result")
                public void testResult()
                {
                    final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                    finishExecution();

                    final List<Integer> result = (List) testObserver.getEvents().get(0).get(0);

                    assertIterableEquals(RESULT, result);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
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

                @Override
                protected void execute()
                {
                    command.execute(INPUT);
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
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
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

                @Override
                protected void execute()
                {
                    command.execute(INPUT);
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
                    final TestObserver<Throwable> testObserver = getCommand().getError().test();

                    finishExecution();

                    testObserver.assertValues(ERROR_A, ERROR_B);
                }

                @Test
                @DisplayName("Progress observable should emit correct values")
                public void testProgress()
                {
                    final TestObserver<Float> testObserver = getCommand().getProgress().test();

                    testObserver.assertValue(0.0f);

                    finishExecution();

                    testObserver.assertValues(0.0f, 0.5f, 1.0f);
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

                command.execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute(INPUT);

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute(INPUT);

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(1);

                commandA.execute(INPUT);

                testObserver.assertValue(1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(true);

                commandA.execute(INPUT);

                testObserver.assertValue(true);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(1.0f);

                commandA.execute(INPUT);

                testObserver.assertValue(1.0f);
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

                command.execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<List<Integer>> testObserver = getCommand().getResult().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should not emit any value")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                commandA.execute(INPUT);

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("CanExecute observable should emit false and true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                commandA.execute(INPUT);

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should not emit any value")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                commandA.execute(INPUT);

                testObserver.assertValue(false);
            }

            @Test
            @DisplayName("ExecutionCount observable should not emit any value")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(1);

                commandA.execute(INPUT);

                testObserver.assertValue(1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should not emit any value")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(true);

                commandA.execute(INPUT);

                testObserver.assertValue(true);
            }

            @Test
            @DisplayName("Progress observable should not emit any value")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(1.0f);

                commandA.execute(INPUT);

                testObserver.assertValue(1.0f);
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
            protected void executeCommand()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.complete(RESULT_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.complete(RESULT_B);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, List<Integer>>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR_A = new RuntimeException("Error A");
            protected final Throwable ERROR_B = new RuntimeException("Error B");

            @Override
            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(executionResultA);
                executionResultA.completeExceptionally(ERROR_A);

                Mockito.when(executionB.apply(INPUT)).thenReturn(executionResultB);
                executionResultB.completeExceptionally(ERROR_B);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
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

    abstract class WhenCreateFromCommandsWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, List<Integer>> implements ReactiveCommandFactory
    {
        private AsyncFunction<Integer, Integer> executionA;
        private CompletableFuture<Integer> executionResultA;
        private ReactiveCommand<Integer, Integer> commandA;

        private AsyncFunction<Integer, Integer> executionB;
        private CompletableFuture<Integer> executionResultB;
        private ReactiveCommand<Integer, Integer> commandB;

        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            executionA = Mockito.mock(AsyncFunction.class);
            executionResultA = new CompletableFuture<>();
            commandA = new AsyncCommand<>(Observable.just(true), executionA);

            executionB = Mockito.mock(AsyncFunction.class);
            executionResultB = new CompletableFuture<>();
            commandB = new AsyncCommand<>(Observable.just(true), executionB);

            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCompositeCommand(testSubject, Arrays.asList(commandA, commandB));
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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
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
            protected void execute()
            {
                command.execute(INPUT);
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
