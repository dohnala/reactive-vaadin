package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base tests for {@link ReactiveCommand}
 *
 * @author dohnal
 */
public abstract class AbstractCommandTest
{
    /**
     * Base interface for tests which needs command
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected interface RequireCommand<T, R>
    {
        @Nonnull
        ReactiveCommand<T, R> getCommand();
    }

    /**
     * Tests which verify behavior of command after it is created
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterCreateCommand<T, R> implements RequireCommand<T, R>
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
        @DisplayName("Execution count observable should emit 0")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValue(0);
        }

        @Test
        @DisplayName("Has been executed observable should emit false")
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
     * Tests which verify command behavior after it is create with custom observable
     * which controls command executability
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterCreateCommandWithObservable<T, R> implements RequireCommand<T, R>
    {
        @Test
        @DisplayName("Can be executed observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValue(true);
        }
    }

    /**
     * Tests which verify behavior of command after observable which controls command executability
     * emits true
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterObservableEmitsTrue<T, R> implements RequireCommand<T, R>
    {
        protected abstract void emitsTrue();

        @Test
        @DisplayName("Can be executed observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(this::emitsTrue)
                    .assertValue(true);
        }
    }

    /**
     * Tests which verify behavior of command after observable which controls command executability
     * emits false
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterObservableEmitsFalse<T, R> implements RequireCommand<T, R>
    {
        protected abstract void emitsFalse();

        @Test
        @DisplayName("Can be executed observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(this::emitsFalse)
                    .assertValue(false);
        }
    }

    /**
     * Tests which verify behavior of command during execution which finishes successfully
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class DuringExecuteCommand<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nullable
        protected abstract R getCorrectResult();

        @Test
        @DisplayName("Result observable should emit result")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(getCorrectResult());
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
        @DisplayName("Can execute observable should emit false and then true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false, true);
        }

        @Test
        @DisplayName("Is executing observable should emit true and then false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true, false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 0, then 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(0.0f, 1.0f, 0.0f);
        }
    }

    /**
     * Tests which verify behavior of command during execution which fails with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class DuringExecuteCommandWithError<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Nonnull
        protected abstract Throwable getError();

        @Test
        @DisplayName("Error observable should emit error")
        public void testError()
        {
            getCommand().getError().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(getError());
        }

        @Test
        @DisplayName("Error should be thrown if no one is subscribed")
        public void testUnhandledError()
        {
            assertThrows(getError().getClass(), () -> getCommand().execute(getInput()));
        }

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getError().test();

            getCommand().getResult().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit false and then true")
        public void testCanExecute()
        {
            getCommand().getError().test();

            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false, true);
        }

        @Test
        @DisplayName("Is executing observable should emit true and then false")
        public void testIsExecuting()
        {
            getCommand().getError().test();

            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true, false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getError().test();

            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().getError().test();

            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 0, then 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getError().test();

            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(0.0f, 1.0f, 0.0f);
        }
    }

    /**
     * Tests which verify behavior of command after successful execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteCommand<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().execute(getInput());

            getCommand().getResult().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().execute(getInput());

            getCommand().getError().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit true")
        public void testCanExecute()
        {
            getCommand().execute(getInput());

            getCommand().canExecute().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Is executing observable should emit false")
        public void testIsExecuting()
        {
            getCommand().execute(getInput());

            getCommand().isExecuting().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().execute(getInput());

            getCommand().getExecutionCount().test()
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().execute(getInput());

            getCommand().hasBeenExecuted().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().execute(getInput());

            getCommand().getProgress().test()
                    .assertValue(0.0f);
        }
    }

    /**
     * Tests which verify behavior of command after error execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteCommandWithError<T, R> implements RequireCommand<T, R>
    {
        @Nullable
        protected abstract T getInput();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().getResult().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().getError().test()
                    .assertNoValues();
        }

        @Test
        @DisplayName("Can execute observable should emit true")
        public void testCanExecute()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().canExecute().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Is executing observable should emit false")
        public void testIsExecuting()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().isExecuting().test()
                    .assertValue(false);
        }

        @Test
        @DisplayName("Execution count observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().getExecutionCount().test()
                    .assertValue(1);
        }

        @Test
        @DisplayName("Has been executed observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().hasBeenExecuted().test()
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());

            getCommand().getProgress().test()
                    .assertValue(0.0f);
        }
    }

    /**
     * Tests which verify behavior of command after execution of command which cannot be executed
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class AfterExecuteDisabledCommand<T, R> implements RequireCommand<T, R>
    {
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
        @DisplayName("Can execute observable should not emit any value")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("Is executing observable should not emit any value")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
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
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }
    }
}
