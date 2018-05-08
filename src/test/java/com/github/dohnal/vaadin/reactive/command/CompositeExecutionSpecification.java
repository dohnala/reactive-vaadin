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
import java.util.List;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Specification for {@link ReactiveCommand#execute()}, {@link ReactiveCommand#execute(Object)} created by
 * {@link ReactiveCommandFactory#createCompositeCommand(List)}
 * {@link ReactiveCommandFactory#createCompositeCommand(Observable, List)}
 *
 * @author dohnal
 */
public interface CompositeExecutionSpecification extends ExecuteSpecification
{
    /**
     * Specification that tests behavior of composite command during execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractCompositeExecuteSpecification<T, R> extends AbstractExecuteSpecification<T, R>
    {
        protected abstract void executeChild();

        protected abstract void executeChildWithError();

        protected abstract void executeWithMultipleErrors();

        @Nonnull
        protected abstract Throwable[] getErrors();

        @Nonnull
        @Override
        protected Float[] getProgress()
        {
            return new Float[]{0.0f, 0.5f, 1.0f};
        }

        @Nonnull
        @Override
        protected Float[] getErrorProgress()
        {
            return new Float[]{0.0f, 0.5f, 1.0f};
        }

        @Nested
        @DisplayName("Before command is executed")
        class BeforeExecute
        {
            @Nested
            @DisplayName("When child command is executed")
            class WhenExecuteChild extends AbstractExecuteChildBeforeExecuteSpecification<T, R>
            {
                @Override
                protected void executeChild()
                {
                    AbstractCompositeExecuteSpecification.this.executeChild();
                }

                @Nonnull
                @Override
                public ReactiveCommand<T, R> getCommand()
                {
                    return AbstractCompositeExecuteSpecification.this.getCommand();
                }
            }

            @Nested
            @DisplayName("When child command is executed with error")
            class WhenExecuteChildWithError extends AbstractExecuteChildBeforeExecuteSpecification<T, R>
            {
                @Override
                protected void executeChild()
                {
                    AbstractCompositeExecuteSpecification.this.executeChildWithError();
                }

                @Nonnull
                @Override
                public ReactiveCommand<T, R> getCommand()
                {
                    return AbstractCompositeExecuteSpecification.this.getCommand();
                }
            }
        }

        @Nested
        @DisplayName("When command is executed with multiple errors")
        class WhenExecuteWithMultipleError
        {
            @BeforeEach
            void before()
            {
                getCommand().getError().test();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<R> testObserver = getCommand().getResult().test();

                executeWithMultipleErrors();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should emit correct errors")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                executeWithMultipleErrors();

                testObserver.assertValues(getErrors());
            }

            @Test
            @DisplayName("CanExecute observable should emit false and then true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                executeWithMultipleErrors();

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should emit true and then false")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                executeWithMultipleErrors();

                testObserver.assertValues(false, true, false);
            }

            @Test
            @DisplayName("ExecutionCount observable should emit 1")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(0);

                executeWithMultipleErrors();

                testObserver.assertValues(0, 1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should emit true")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(false);

                executeWithMultipleErrors();

                testObserver.assertValues(false, true);
            }

            @Test
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                executeWithMultipleErrors();

                testObserver.assertValues(getErrorProgress());
            }

            @Test
            @DisplayName("Execution should be run")
            public void testExecution()
            {
                executeWithMultipleErrors();

                verifyExecution();
            }

            @Nested
            @DisplayName("After command is executed with multiple errors")
            class AfterExecute
            {
                @BeforeEach
                void before()
                {
                    executeWithMultipleErrors();
                }

                @Nested
                @DisplayName("When command is subscribed")
                class WhenSubscribe extends AbstractSubscribeAfterExecuteSpecification<T, R>
                {
                    @Nonnull
                    @Override
                    public ReactiveCommand<T, R> getCommand()
                    {
                        return AbstractCompositeExecuteSpecification.this.getCommand();
                    }
                }

                @Nested
                @DisplayName("When child command is executed")
                class WhenExecuteChild extends AbstractExecuteChildAfterExecuteSpecification<T, R>
                {
                    @Override
                    protected void executeChild()
                    {
                        AbstractCompositeExecuteSpecification.this.executeChild();
                    }

                    @Nonnull
                    @Override
                    public ReactiveCommand<T, R> getCommand()
                    {
                        return AbstractCompositeExecuteSpecification.this.getCommand();
                    }
                }

                @Nested
                @DisplayName("When child command is executed with error")
                class WhenExecuteChildWithError extends AbstractExecuteChildAfterExecuteSpecification<T, R>
                {
                    @Override
                    protected void executeChild()
                    {
                        AbstractCompositeExecuteSpecification.this.executeChildWithError();
                    }

                    @Nonnull
                    @Override
                    public ReactiveCommand<T, R> getCommand()
                    {
                        return AbstractCompositeExecuteSpecification.this.getCommand();
                    }
                }
            }
        }
    }

    /**
     * Specification that tests behavior of composite command when its child command is executed before
     * the composite command is executed
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractExecuteChildBeforeExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void executeChild();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            final TestObserver<R> testObserver = getCommand().getResult().test();

            executeChild();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            executeChild();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit false and true")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            executeChild();

            testObserver.assertValues(true, false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should not emit any value")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            executeChild();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should not emit any value")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(0);

            executeChild();

            testObserver.assertValue(0);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should not emit any value")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(false);

            executeChild();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(0.0f);

            executeChild();

            testObserver.assertValue(0.0f);
        }
    }

    /**
     * Specification that tests behavior of composite command when its child command is executed after
     * the composite command is executed
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractExecuteChildAfterExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void executeChild();

        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            final TestObserver<R> testObserver = getCommand().getResult().test();

            executeChild();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            final TestObserver<Throwable> testObserver = getCommand().getError().test();

            executeChild();

            testObserver.assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit false and true")
        public void testCanExecute()
        {
            final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

            testObserver.assertValue(true);

            executeChild();

            testObserver.assertValues(true, false, true);
        }

        @Test
        @DisplayName("IsExecuting observable should not emit any value")
        public void testIsExecuting()
        {
            final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

            testObserver.assertValue(false);

            executeChild();

            testObserver.assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should not emit any value")
        public void testExecutionCount()
        {
            final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

            testObserver.assertValue(1);

            executeChild();

            testObserver.assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should not emit any value")
        public void testHasBeenExecuted()
        {
            final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

            testObserver.assertValue(true);

            executeChild();

            testObserver.assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should not emit any value")
        public void testProgress()
        {
            final TestObserver<Float> testObserver = getCommand().getProgress().test();

            testObserver.assertValue(1.0f);

            executeChild();

            testObserver.assertValue(1.0f);
        }
    }
}
