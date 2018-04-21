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

package com.github.dohnal.vaadin.reactive.command.async;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import com.github.dohnal.vaadin.reactive.AsyncSupplier;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
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
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createFromAsyncSupplier(AsyncSupplier)}
 * {@link ReactiveCommand#createFromAsyncSupplier(Observable, AsyncSupplier)}
 *
 * @author dohnal
 */
public interface AsyncCommandFromAsyncSupplierSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromAsyncSupplierSpecification extends WhenCreateSpecification<Void, Integer>
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
        @DisplayName("When command execution started")
        class WhenExecutionStarted extends WhenExecutionStartedSpecification<Void, Integer>
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
            @DisplayName("When command execution finished")
            class WhenExecutionFinished extends WhenExecutionFinishedSpecification<Void, Integer>
            {
                protected final Integer RESULT = 5;

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
                protected Integer getResult()
                {
                    return RESULT;
                }
            }

            @Nested
            @DisplayName("When command execution finished with error")
            class WhenExecutionFinishedWithError extends WhenExecutionFinishedWithErrorSpecification<Void, Integer>
            {
                protected final Throwable ERROR = new RuntimeException("Error");

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
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 5;

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(execution.get()).thenReturn(executionResult);
                executionResult.complete(RESULT);

                super.execute();
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
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Integer>
        {
            protected final Throwable ERROR = new RuntimeException("Error");

            @Override
            @BeforeEach
            protected void execute()
            {
                Mockito.when(execution.get()).thenReturn(executionResult);
                executionResult.completeExceptionally(ERROR);

                super.execute();
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

    abstract class WhenCreateFromAsyncSupplierWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Integer>
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
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Void, Integer>
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
        @DisplayName("When CanExecute observable emits false")
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Void, Integer>
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
        @DisplayName("When CanExecute observable emits true during execution")
        class WhenCanExecuteEmitsTrueDuringExecution extends WhenCanExecuteEmitsTrueDuringExecutionSpecification<Void, Integer>
        {
            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(execution.get()).thenReturn(executionResult);

                super.startExecution();
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
        @DisplayName("When CanExecute observable emits false during execution")
        class WhenCanExecuteEmitsFalseDuringExecution extends WhenCanExecuteEmitsFalseDuringExecutionSpecification<Void, Integer>
        {
            @Override
            @BeforeEach
            protected void startExecution()
            {
                Mockito.when(execution.get()).thenReturn(executionResult);

                super.startExecution();
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
        @DisplayName("When command is executed while disabled")
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Void, Integer>
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
