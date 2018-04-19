package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
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
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(this::emitsTrue)
                    .assertNoValues();
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
        @DisplayName("CanExecuted observable should emit false")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(this::emitsFalse)
                    .assertValue(false);
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
        @Nullable
        protected abstract T getInput();

        @Nullable
        protected abstract R getResult();

        @Test
        @DisplayName("Result observable should emit correct result")
        public void testResult()
        {
            getCommand().getResult().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(getResult());
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
        @DisplayName("CanExecute observable should emit false and then true")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true and then false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(1.0f, 0.0f);
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
        @Nullable
        protected abstract T getInput();

        @Nonnull
        protected abstract Throwable getError();

        @Test
        @DisplayName("Error observable should emit correct error")
        public void testError()
        {
            getCommand().getError().test();

            getCommand().getError().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(getError());
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
        @DisplayName("CanExecute observable should emit false and then true")
        public void testCanExecute()
        {
            getCommand().getError().test();

            getCommand().canExecute().test()
                    .assertValuesAndClear(true)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit true and then false")
        public void testIsExecuting()
        {
            getCommand().getError().test();

            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(true, false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getError().test();

            getCommand().getExecutionCount().test()
                    .assertValuesAndClear(0)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().getError().test();

            getCommand().hasBeenExecuted().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getError().test();

            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(1.0f, 0.0f);
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
        @DisplayName("CanExecute observable should not emit any value")
        public void testCanExecute()
        {
            getCommand().canExecute().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
        }

        @Test
        @DisplayName("IsExecuting observable should not emit any value")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test()
                    .assertValuesAndClear(false)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
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
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValuesAndClear(0.0f)
                    .perform(() -> getCommand().execute(getInput()))
                    .assertNoValues();
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
        @Nullable
        protected abstract T getInput();

        @BeforeEach
        void execute()
        {
            getCommand().execute(getInput());
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
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValue(0.0f);
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
        @Nullable
        protected abstract T getInput();

        @BeforeEach
        void execute()
        {
            getCommand().getError().test();

            getCommand().execute(getInput());
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
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getProgress().test()
                    .assertValue(0.0f);
        }
    }
}
