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
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
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

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createAsync(Supplier, Executor)}
 * {@link ReactiveCommand#createAsync(Observable, Supplier, Executor)}
 *
 * @author dohnal
 */
public interface AsyncCommandFromSupplierSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromSupplierSpecification extends WhenCreateSpecification<Void, Integer>
    {
        private Supplier<Integer> execution;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Supplier.class);
            command = ReactiveCommand.createAsync(execution);
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
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.get()).thenReturn(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
            }

            @Test
            @DisplayName("Result observable should emit correct result")
            public void testResult()
            {
                final TestObserver<Integer> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertValue(RESULT);
            }

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution).get();
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Integer>
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.get()).thenThrow(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
            }

            @Nonnull
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution).get();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.get()).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Integer>
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.get()).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
            }
        }
    }

    abstract class WhenCreateFromSupplierWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Integer>
    {
        private Supplier<Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Supplier.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution);
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
        @DisplayName("When CanExecute observable emits true")
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
            protected void execute()
            {
                command.execute().blockingAwait();
            }

            @Test
            @DisplayName("Supplier should not be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).get();
            }
        }
    }

    abstract class WhenCreateFromSupplierWithExecutorSpecification extends WhenCreateSpecification<Void, Integer>
    {
        private TestExecutor testExecutor;
        private Supplier<Integer> execution;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Supplier.class);
            command = ReactiveCommand.createAsync(execution, testExecutor);
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
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.get()).thenReturn(RESULT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }

            @Test
            @DisplayName("Result observable should emit correct result")
            public void testResult()
            {
                final TestObserver<Integer> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertValue(RESULT);
            }

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution).get();
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Integer>
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.get()).thenThrow(ERROR);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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
                return ERROR;
            }

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution).get();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.get()).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Integer>
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.get()).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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

    abstract class WhenCreateFromSupplierWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Integer>
    {
        private TestExecutor testExecutor;
        private Supplier<Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Supplier.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution, testExecutor);
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
        @DisplayName("When CanExecute observable emits true")
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
            protected void execute()
            {
                command.execute();
            }

            @Test
            @DisplayName("Supplier should not be run")
            public void testSupplier()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).get();
            }
        }
    }
}
