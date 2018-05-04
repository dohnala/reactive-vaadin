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

package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ProgressContext;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
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
 * {@link ReactiveCommandFactory#createProgressCommand(Consumer)}
 * {@link ReactiveCommandFactory#createProgressCommand(Consumer, Executor)}
 *
 * @author dohnal
 */
public interface ProgressCommandFromConsumerSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromConsumerSpecification extends WhenCreateSpecification<Void, Void>
            implements ReactiveCommandFactory
    {
        private Consumer<ProgressContext> execution;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            command = createProgressCommand(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Consumer should not be run")
        public void testConsumer()
        {
            Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Void>
        {
            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);
                    progressContext.set(0.75f);
                    progressContext.set(1.0f);

                    return null;
                }).when(execution).accept(Mockito.any(ProgressContext.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<Void> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertNoValues();
            }

            @Test
            @Override
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                execute();

                testObserver.assertValues(0.0f, 0.25f, 0.5f, 0.75f, 1.0f);
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Void>
        {
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);

                    throw ERROR;

                }).when(execution).accept(Mockito.any(ProgressContext.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
            @Override
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                execute();

                testObserver.assertValues(0.0f, 0.25f, 0.5f, 1.0f);
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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

    abstract class WhenCreateFromConsumerWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Void> implements ReactiveCommandFactory
    {
        private Consumer<ProgressContext> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Void, Void>
        {
            @BeforeEach
            public void disableCommand()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute().blockingAwait();
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

    abstract class WhenCreateFromConsumerWithExecutorSpecification extends WhenCreateSpecification<Void, Void>
            implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private Consumer<ProgressContext> execution;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Consumer.class);
            command = createProgressCommand(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Consumer should not be run")
        public void testConsumer()
        {
            Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Void>
        {
            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);
                    progressContext.set(0.75f);
                    progressContext.set(1.0f);

                    return null;
                }).when(execution).accept(Mockito.any(ProgressContext.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<Void> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertNoValues();
            }

            @Test
            @Override
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                execute();

                testObserver.assertValues(0.0f, 0.25f, 0.5f, 0.75f, 1.0f);
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Void>
        {
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);

                    throw ERROR;

                }).when(execution).accept(Mockito.any(ProgressContext.class));
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
            @Override
            @DisplayName("Progress observable should emit correct values")
            public void testProgress()
            {
                final TestObserver<Float> testObserver = getCommand().getProgress().test();

                testObserver.assertValue(0.0f);

                execute();

                testObserver.assertValues(0.0f, 0.25f, 0.5f, 1.0f);
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

                Mockito.verify(execution).accept(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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

    abstract class WhenCreateFromConsumerWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Void> implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private Consumer<ProgressContext> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Void, Void>
        {
            @BeforeEach
            public void disableCommand()
            {
                testSubject.onNext(false);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
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
