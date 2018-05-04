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
import java.util.function.Function;

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
 * {@link ReactiveCommandFactory#createProgressCommand(Function)}
 * {@link ReactiveCommandFactory#createProgressCommand(Function, Executor)}
 *
 * @author dohnal
 */
public interface ProgressCommandFromFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromFunctionSpecification extends WhenCreateSpecification<Void, Integer>
            implements ReactiveCommandFactory

    {
        private Function<ProgressContext, Integer> execution;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Function.class);
            command = createProgressCommand(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Function should not be run")
        public void testFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 5;

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

                    return RESULT;
                }).when(execution).apply(Mockito.any(ProgressContext.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Integer>
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

                }).when(execution).apply(Mockito.any(ProgressContext.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class));
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
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class))).thenReturn(RESULT);

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
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class))).thenThrow(ERROR);

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

    abstract class WhenCreateFromFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Integer> implements ReactiveCommandFactory
    {
        private Function<ProgressContext, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution);
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
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }

    abstract class WhenCreateFromFunctionWithExecutorSpecification extends WhenCreateSpecification<Void, Integer>
            implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private Function<ProgressContext, Integer> execution;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            command = createProgressCommand(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Function should not be run")
        public void testFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Integer>
        {
            protected final Integer RESULT = 5;

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

                    return RESULT;
                }).when(execution).apply(Mockito.any(ProgressContext.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class));
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Integer>
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

                }).when(execution).apply(Mockito.any(ProgressContext.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class));
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
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class))).thenReturn(RESULT);

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
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class))).thenThrow(ERROR);

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

    abstract class WhenCreateFromFunctionWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Integer> implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private Function<ProgressContext, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution, testExecutor);
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
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
