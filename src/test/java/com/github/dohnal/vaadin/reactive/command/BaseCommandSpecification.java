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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Base specifications for {@link ReactiveCommand}
 *
 * @author dohnal
 */
public interface BaseCommandSpecification
{
    /**
     * Base interface for tests which needs command
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    interface RequireCommand<T, R>
    {
        @Nonnull
        ReactiveCommand<T, R> getCommand();
    }

    /**
     * Synchronized implementation of {@link Executor} for testing asynchronous commands
     */
    class TestExecutor implements Executor
    {
        @Override
        public void execute(final @Nonnull Runnable runnable)
        {
            runnable.run();
        }
    }

    /**
     * Specification that tests behavior of command after it is created
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCreateSpecification<T, R> implements RequireCommand<T, R>
    {
        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 0")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValue(0);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit false")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValue(0.0f);
        }
    }

    /**
     * Specification that tests behavior of command after it is created with custom CanExecute
     * which controls command executability
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCreateWithCanExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValue(true);
        }
    }

    /**
     * Specification that tests behavior of command after CanExecute observable which controls
     * command executability emits true
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCanExecuteEmitsTrueSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void emitsTrue();

        @Test
        @DisplayName("CanExecute observable should not emit any value")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            emitsTrue();

            testObserver.assertValue(true);
        }
    }

    /**
     * Specification that tests behavior of command after CanExecute observable which controls
     * command executability emits false
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCanExecuteEmitsFalseSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void emitsFalse();

        @Test
        @DisplayName("CanExecute observable should emit false")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            emitsFalse();

            testObserver.assertValues(true, false);
        }
    }

    /**
     * Specification that tests behavior of command after CanExecute observable which controls command executability
     * emits true during command execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCanExecuteEmitsTrueDuringExecutionSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        protected abstract void emitsTrue();

        @BeforeEach
        protected void startExecution()
        {
            execute();
        }

        @Test
        @DisplayName("CanExecuted observable should not emit any value")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(false);

            emitsTrue();

            testObserver.assertValue(false);
        }
    }

    /**
     * Specification that tests behavior of command after CanExecute observable which controls command executability
     * emits false during command execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCanExecuteEmitsFalseDuringExecutionSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        protected abstract void emitsFalse();

        @BeforeEach
        protected void startExecution()
        {
            execute();
        }

        @Test
        @DisplayName("CanExecute observable should not emit any value")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(false);

            emitsFalse();

            testObserver.assertValue(false);
        }
    }

    /**
     * Specification that tests behavior of asynchronous command after execution is started,
     * but it is not finished yet
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionStartedSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            final TestObserver<R> testObserver = getCommand().getResult().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit false")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            execute();

            testObserver.assertValues(true, false);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("ExecutionCount observable should not emit any value")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            execute();

            testObserver.assertValue(0);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should not emit any value")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            execute();

            testObserver.assertValue(0.0f);
        }
    }

    /**
     * Specification that tests behavior of command during execution which finishes successfully
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit false and then true")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            execute();

            testObserver.assertValues(true, false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true and then false")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValues(false, true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            execute();

            testObserver.assertValues(0, 1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("Progress observable should emit 1")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            execute();

            testObserver.assertValues(0.0f, 1.0f);
        }
    }

    /**
     * Specification that tests behavior of command during execution which fails with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecuteWithErrorSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @Nonnull
        protected abstract Throwable getError();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getError().test();

            final TestObserver<R> testObserver = getCommand().getResult().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should emit correct error")
        public void testError()
        {
            getCommand().getError().test();

            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            execute();

            testObserver.assertValue(getError());
        }

        @Test
        @DisplayName("CanExecute observable should emit false and then true")
        public void testCanExecute()
        {
            getCommand().getError().test();

            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            execute();

            testObserver.assertValues(true, false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true and then false")
        public void testIsExecuting()
        {
            getCommand().getError().test();

            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValues(false, true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getError().test();

            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            execute();

            testObserver.assertValues(0, 1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().getError().test();

            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("Progress observable should emit 1")
        public void testProgress()
        {
            getCommand().getError().test();

            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            execute();

            testObserver.assertValues(0.0f, 1.0f);
        }
    }

    /**
     * Specification that tests behavior of asynchronous command after execution is successfully finished
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionFinishedSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        protected abstract void finishExecution();

        @BeforeEach
        protected void startExecution()
        {
            execute();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            finishExecution();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(false);

            finishExecution();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(true);

            finishExecution();

            testObserver.assertValues(true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            finishExecution();

            testObserver.assertValues(0, 1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            finishExecution();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("Progress observable should emit 1")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            finishExecution();

            testObserver.assertValue(1.0f);
        }
    }

    /**
     * Specification that tests behavior of asynchronous command after execution is finished with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionFinishedWithErrorSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @Nonnull
        protected abstract Throwable getError();

        protected abstract void finishExecution();

        @BeforeEach
        protected void startExecution()
        {
            execute();
        }

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            final TestObserver<R> testObserver = getCommand().getResult().test();

            finishExecution();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should emit correct error")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            finishExecution();

            testObserver.assertValue(getError());
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(false);

            finishExecution();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(true);

            finishExecution();

            testObserver.assertValues(true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            finishExecution();

            testObserver.assertValues(0, 1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            finishExecution();

            testObserver.assertValues(false, true);
        }

        @Test
        @DisplayName("Progress observable should emit 1")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            finishExecution();

            testObserver.assertValue(1.0f);
        }
    }

    /**
     * Specification which tests behavior of command after it is executed in disabled state (CanExecute is false)
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecuteWhileDisabledSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            final TestObserver<R> testObserver = getCommand().getResult().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            execute();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should not emit any value")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("IsExecuting observable should not emit any value")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should not emit any value")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            execute();

            testObserver.assertValue(0);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should not emit any value")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            execute();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            execute();

            testObserver.assertValue(0.0f);
        }
    }

    /**
     * Specification that tests behavior of command when it is subscribed after successful execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenSubscribeAfterExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @BeforeEach
        protected void executeCommand()
        {
            execute();
        }

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1.0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValue(1.0f);
        }
    }

    /**
     * Specification that tests behavior of command when it is subscribed after failed execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenSubscribeAfterExecuteWithErrorSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        @BeforeEach
        protected void executeCommand()
        {
            getCommand().getError().test();

            execute();
        }

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Is executing observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1.0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValue(1.0f);
        }
    }
}
