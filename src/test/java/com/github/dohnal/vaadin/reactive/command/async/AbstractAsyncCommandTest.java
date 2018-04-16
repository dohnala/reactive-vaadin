package com.github.dohnal.vaadin.reactive.command.async;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.command.AbstractCommandTest;
import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Base tests for {@link AsyncCommand}
 *
 * @author dohnal
 */
public abstract class AbstractAsyncCommandTest extends AbstractCommandTest
{
    /**
     * Synchronized implementation of {@link Executor} for test purposes
     */
    public class TestExecutor implements Executor
    {
        @Override
        public void execute(final @Nonnull Runnable runnable)
        {
            runnable.run();
        }
    }

    /**
     * Tests which verify behavior of command after execution is started, but it is not finished yet
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteCommandStarted<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false);
        }

        @Test
        @DisplayName("Is executing observable should emit true")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true);
        }

        @Test
        @DisplayName("Execution count observable should not emit any value")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Has been executed observable should not emit any value")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(0.0f);
        }
    }

    /**
     * Tests which verify behavior of command which execution finished successfully
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteCommandFinished<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nullable
        protected abstract R getCorrectResult();

        protected abstract void finishExecution();

        @Test
        @DisplayName("Result observable should emit result")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(this::finishExecution)
                    .assertValue(getCorrectResult());
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test()
                    .perform(this::finishExecution)
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("Is executing observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(true)
                    .perform(this::finishExecution)
                    .assertValue(false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(this::finishExecution)
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(this::finishExecution)
                    .assertValues(1.0f, 0.0f);
        }
    }

    /**
     * Tests which verify behavior of command which execution finished with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteCommandFinishedWithError<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nonnull
        protected abstract Throwable getError();

        protected abstract void finishExecution();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(this::finishExecution)
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should emit error")
        public void testError()
        {
            getCommand().getError().test()
                    .perform(this::finishExecution)
                    .assertValue(getError());
        }

        @Test
        @DisplayName("Can execute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("Is executing observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(true)
                    .perform(this::finishExecution)
                    .assertValue(false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(this::finishExecution)
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(this::finishExecution)
                    .assertValues(1.0f, 0.0f);
        }
    }

    /**
     * Tests which verify behavior of command after observable which controls command executability
     * emits true during command execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterObservableEmitsTrueDuringExecution<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        protected abstract void emitsTrue();

        @Test
        @DisplayName("Can be executed observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValuesAndClear(false)
                    .perform(this::emitsTrue)
                    .assertValue(false);
        }
    }

    /**
     * Tests which verify behavior of command after observable which controls command executability
     * emits false during command execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterObservableEmitsFalseDuringExecution<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        protected abstract void emitsFalse();

        @Test
        @DisplayName("Can be executed observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValuesAndClear(false)
                    .perform(this::emitsFalse)
                    .assertValue(false);
        }
    }
}
