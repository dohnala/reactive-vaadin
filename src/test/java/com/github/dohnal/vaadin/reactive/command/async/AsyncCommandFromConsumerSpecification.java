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
import java.util.function.Consumer;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createAsync(Consumer, Executor)}
 * {@link ReactiveCommand#createAsync(Observable, Consumer, Executor)}
 *
 * @author dohnal
 */
public interface AsyncCommandFromConsumerSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromConsumerSpecification extends WhenCreateSpecification<Integer, Void>
    {
        private Consumer<Integer> execution;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            command = ReactiveCommand.createAsync(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Consumer should not be run")
        public void testConsumer()
        {
            Mockito.verify(execution, Mockito.never()).accept(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class WhenExecuteWithInput
        {
            @Test
            @DisplayName("IllegalArgumentException should be thrown")
            public void testExecute()
            {
                assertThrows(IllegalArgumentException.class, () -> command.execute().await());
            }
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doNothing().when(execution).accept(INPUT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).await();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                getCommand().getResult().test()
                        .perform(this::execute)
                        .assertNoValues();
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doThrow(ERROR).when(execution).accept(INPUT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).await();
            }

            @Nonnull
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("When subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).await();
            }
        }

        @Nested
        @DisplayName("When subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).await();
            }
        }
    }

    abstract class WhenCreateFromConsumerWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Void>
    {
        private Consumer<Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Void>
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
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).await();
            }

            @Test
            @DisplayName("Consumer should not be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
            }
        }
    }

    abstract class WhenCreateFromConsumerWithExecutorSpecification extends WhenCreateSpecification<Integer, Void>
    {
        private TestExecutor testExecutor;
        private Consumer<Integer> execution;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Consumer.class);
            command = ReactiveCommand.createAsync(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Consumer should not be run")
        public void testConsumer()
        {
            Mockito.verify(execution, Mockito.never()).accept(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class WhenExecuteWithInput
        {
            @Test
            @DisplayName("IllegalArgumentException should be thrown")
            public void testExecute()
            {
                assertThrows(IllegalArgumentException.class, () -> command.execute());
            }
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doNothing().when(execution).accept(INPUT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                getCommand().getResult().test()
                        .perform(this::execute)
                        .assertNoValues();
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doThrow(ERROR).when(execution).accept(INPUT);
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
                return ERROR;
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("When subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        @DisplayName("When subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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

    abstract class WhenCreateFromConsumerWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Void>
    {
        private TestExecutor testExecutor;
        private Consumer<Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Consumer.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Void>
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
            public ReactiveCommand<Integer, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Consumer should not be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
            }
        }
    }
}
