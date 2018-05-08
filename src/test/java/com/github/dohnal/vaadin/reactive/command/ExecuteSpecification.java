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
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Specification for
 * {@link ReactiveCommand#execute()}
 * {@link ReactiveCommand#execute(Object)}
 *
 * @author dohnal
 */
public interface ExecuteSpecification
{
    /**
     * Specification that tests behavior of command during execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        protected abstract void execute();

        protected abstract void executeWithError();

        @Nullable
        protected abstract R getResult();

        @Nullable
        protected abstract Throwable getError();

        protected abstract void verifyExecution();

        @Nonnull
        protected Float[] getProgress()
        {
            return new Float[]{0.0f, 1.0f};
        }

        @Nonnull
        protected Float[] getErrorProgress()
        {
            return new Float[]{0.0f, 1.0f};
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute
        {
            @Test
            @DisplayName("Result observable should emit correct value")
            public void testResult()
            {
                final TestObserver<R> testObserver = getCommand().getResult().test();

                execute();

                if (getResult() == null)
                {
                    testObserver.assertNoValues();
                }
                else
                {
                    testObserver.assertValue(getResult());
                }
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
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                execute();

                testObserver.assertValues(getProgress());
            }

            @Test
            @DisplayName("Execution should be run")
            public void testExecution()
            {
                execute();

                verifyExecution();
            }

            @Nested
            @DisplayName("After command is executed")
            class AfterExecute
            {
                @BeforeEach
                void before()
                {
                    execute();
                }

                @Nested
                @DisplayName("When command is subscribed")
                class WhenSubscribe extends AbstractSubscribeAfterExecuteSpecification<T, R>
                {
                    @Nonnull
                    @Override
                    public ReactiveCommand<T, R> getCommand()
                    {
                        return AbstractExecuteSpecification.this.getCommand();
                    }
                }
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError
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

                executeWithError();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Error observable should emit correct error")
            public void testError()
            {
                final TestObserver<Throwable> testObserver = getCommand().getError().test();

                executeWithError();

                testObserver.assertValue(getError());
            }

            @Test
            @DisplayName("CanExecute observable should emit false and then true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                executeWithError();

                testObserver.assertValues(true, false, true);
            }

            @Test
            @DisplayName("IsExecuting observable should emit true and then false")
            public void testIsExecuting()
            {
                final TestObserver<Boolean> testObserver = getCommand().isExecuting().test();

                testObserver.assertValue(false);

                executeWithError();

                testObserver.assertValues(false, true, false);
            }

            @Test
            @DisplayName("ExecutionCount observable should emit 1")
            public void testExecutionCount()
            {
                final TestObserver<Integer> testObserver = getCommand().getExecutionCount().test();

                testObserver.assertValue(0);

                executeWithError();

                testObserver.assertValues(0, 1);
            }

            @Test
            @DisplayName("HasBeenExecuted observable should emit true")
            public void testHasBeenExecuted()
            {
                final TestObserver<Boolean> testObserver = getCommand().hasBeenExecuted().test();

                testObserver.assertValue(false);

                executeWithError();

                testObserver.assertValues(false, true);
            }

            @Test
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                executeWithError();

                testObserver.assertValues(getErrorProgress());
            }

            @Test
            @DisplayName("Execution should be run")
            public void testExecution()
            {
                executeWithError();

                verifyExecution();
            }

            @Nested
            @DisplayName("After command is executed with error")
            class AfterExecute
            {
                @BeforeEach
                void before()
                {
                    executeWithError();
                }

                @Nested
                @DisplayName("When command is subscribed")
                class WhenSubscribe extends AbstractSubscribeAfterExecuteSpecification<T, R>
                {
                    @Nonnull
                    @Override
                    public ReactiveCommand<T, R> getCommand()
                    {
                        return AbstractExecuteSpecification.this.getCommand();
                    }
                }
            }
        }
    }

    /**
     * Specification that tests behavior of command when it is subscribed after execution
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractSubscribeAfterExecuteSpecification<T, R> implements RequireCommand<T, R>
    {
        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test().assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test().assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test().assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test().assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 1")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test().assertValue(1);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit true")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test().assertValue(true);
        }

        @Test
        @DisplayName("Progress observable should emit 1.0")
        public void testProgress()
        {
            getCommand().getProgress().test().assertValue(1.0f);
        }
    }
}
