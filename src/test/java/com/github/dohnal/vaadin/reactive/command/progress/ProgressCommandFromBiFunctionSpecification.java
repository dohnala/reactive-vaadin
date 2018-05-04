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
import java.util.function.BiFunction;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createProgressCommand(BiFunction)}
 * {@link ReactiveCommandFactory#createProgressCommand(BiFunction, Executor)}
 *
 * @author dohnal
 */
public interface ProgressCommandFromBiFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromBiFunctionSpecification extends WhenCreateSpecification<Integer, Integer>
            implements ReactiveCommandFactory
    {
        private BiFunction<ProgressContext, Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(BiFunction.class);
            command = createProgressCommand(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("BiFunction should not be run")
        public void testBiFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.any());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class WhenExecuteWithInput
        {
            @Test
            @DisplayName("IllegalArgumentException should be thrown")
            public void testExecute()
            {
                assertThrows(IllegalArgumentException.class, () -> command.execute().blockingAwait());
            }
        }

        @Nested
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

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
                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
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
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
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

                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
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
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class), Mockito.anyInt())).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class), Mockito.anyInt())).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }
        }
    }

    abstract class WhenCreateFromBiFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer> implements ReactiveCommandFactory
    {
        private BiFunction<ProgressContext, Integer, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(BiFunction.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Integer>
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
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT).blockingAwait();
            }

            @Test
            @DisplayName("BiFunction should not be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.anyInt());
            }
        }
    }

    abstract class WhenCreateFromBiFunctionWithExecutorSpecification extends WhenCreateSpecification<Integer, Integer>
            implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private BiFunction<ProgressContext, Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(BiFunction.class);
            command = createProgressCommand(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("BiFunction should not be run")
        public void testBiFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.any());
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
        class WhenExecute extends WhenExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

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
                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
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
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
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

                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class), Mockito.anyInt());
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class), Mockito.anyInt())).thenReturn(RESULT);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            private final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void executeCommand()
            {
                Mockito.when(execution.apply(Mockito.any(ProgressContext.class), Mockito.anyInt())).thenThrow(ERROR);

                super.executeCommand();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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

    abstract class WhenCreateFromBiFunctionWithCanExecuteAndExecutorSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer> implements ReactiveCommandFactory
    {
        private TestExecutor testExecutor;
        private BiFunction<ProgressContext, Integer, Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(BiFunction.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Integer>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
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
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Integer>
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
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("BiFunction should not be run")
            public void testBiFunction()
            {
                execute();

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.anyInt());
            }
        }
    }
}
