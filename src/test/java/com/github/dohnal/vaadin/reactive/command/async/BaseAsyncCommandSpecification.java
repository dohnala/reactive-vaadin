package com.github.dohnal.vaadin.reactive.command.async;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Base specifications for asynchronous {@link ReactiveCommand}
 *
 * @author dohnal
 */
public interface BaseAsyncCommandSpecification extends BaseCommandSpecification
{
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
     * Specification that tests behavior of asynchronous command after execution is started,
     * but it is not finished yet
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionStartedSpecification<T, R> implements RequireCommand<T, R>
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
        @DisplayName("CanExecute observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true);
        }

        @Test
        @DisplayName("ExecutionCount observable should not emit any value")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("HasBeenExecuted observable should not emit any value")
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
     * Specification that tests behavior of asynchronous command after execution is successfully finished
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionFinishedSpecification<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nullable
        protected abstract R getResult();

        protected abstract void finishExecution();

        @BeforeEach
        protected void startExecution()
        {
            getCommand().execute(getInput());
        }

        @Test
        @DisplayName("Result observable should emit correct result")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(this::finishExecution)
                    .assertValue(getResult());
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
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(true)
                    .perform(this::finishExecution)
                    .assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(this::finishExecution)
                    .assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
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
     * Specification that tests behavior of asynchronous command after execution is finished with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenExecutionFinishedWithErrorSpecification<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nonnull
        protected abstract Throwable getError();

        protected abstract void finishExecution();

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
                    .perform(this::finishExecution)
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should emit correct error")
        public void testError()
        {
            getCommand().getError().test()
                    .perform(this::finishExecution)
                    .assertValue(getError());
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::finishExecution)
                    .assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(true)
                    .perform(this::finishExecution)
                    .assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(this::finishExecution)
                    .assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
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
     * Specification that tests behavior of command after CanExecute observable which controls command executability
     * emits true during command execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class WhenCanExecuteEmitsTrueDuringExecutionSpecification<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        protected abstract void emitsTrue();

        @BeforeEach
        protected void startExecution()
        {
            getCommand().execute(getInput());
        }

        @Test
        @DisplayName("CanExecuted observable should not emit any value")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::emitsTrue)
                    .assertNoValues();
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
        @Nullable
        protected abstract T getInput();

        protected abstract void emitsFalse();

        @BeforeEach
        protected void startExecution()
        {
            getCommand().execute(getInput());
        }

        @Test
        @DisplayName("CanExecute observable should not emit any value")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(this::emitsFalse)
                    .assertNoValues();
        }
    }
}
