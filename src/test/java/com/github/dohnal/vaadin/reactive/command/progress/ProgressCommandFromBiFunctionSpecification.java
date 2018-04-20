package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

import com.github.dohnal.vaadin.reactive.Progress;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#createProgress(BiFunction)}
 * {@link ReactiveCommand#createProgress(BiFunction, Executor)}
 *
 * @author dohnal
 */
public interface ProgressCommandFromBiFunctionSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromBiFunctionSpecification extends WhenCreateSpecification<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private BiFunction<Progress, Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(BiFunction.class);
            command = ReactiveCommand.createProgress(execution, testExecutor);
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
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doAnswer(invocation -> {
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);
                    progress.set(0.75f);
                    progress.set(1.0f);

                    return RESULT;
                }).when(execution).apply(Mockito.any(Progress.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }

            @Nullable
            @Override
            protected Integer getResult()
            {
                return RESULT;
            }

            @Test
            @Override
            @DisplayName("Progress observable should emit correct values and then reset back to 0")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> getCommand().execute(getInput()))
                        .assertValues(0.25f, 0.5f, 0.75f, 1.0f, 0.0f);
            }

            @Test
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class), Mockito.anyInt());
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
                    final Progress progress = invocation.getArgument(0);

                    progress.set(0.0f);
                    progress.set(0.25f);
                    progress.set(0.5f);

                    throw ERROR;

                }).when(execution).apply(Mockito.any(Progress.class), Mockito.anyInt());
            }

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }

            @Nonnull
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Test
            @Override
            @DisplayName("Progress observable should emit correct values and then reset back to 0")
            public void testProgress()
            {
                getCommand().getProgress().test()
                        .assertValuesAndClear(0.0f)
                        .perform(() -> getCommand().execute(getInput()))
                        .assertValues(0.25f, 0.5f, 1.0f, 0.0f);
            }

            @Test
            @DisplayName("BiFunction should be run")
            public void testBiFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class), Mockito.anyInt());
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Integer>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nullable
            @Override
            protected Integer getInput()
            {
                return null;
            }
        }
    }

    abstract class WhenCreateFromBiFunctionWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private BiFunction<Progress, Integer, Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(BiFunction.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createProgress(testSubject, execution, testExecutor);
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
            protected Integer getInput()
            {
                return INPUT;
            }

            @Test
            @DisplayName("BiFunction should not be run")
            public void testBiFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.anyInt());
            }
        }
    }
}
