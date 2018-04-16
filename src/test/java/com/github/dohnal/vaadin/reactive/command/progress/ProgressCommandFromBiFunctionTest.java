package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

import com.github.dohnal.vaadin.reactive.Progress;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Tests for {@link ProgressCommand} created by
 * {@link ReactiveCommand#createProgress(BiFunction)}
 * {@link ReactiveCommand#createProgress(BiFunction, Executor)}
 *
 * @author dohnal
 */
@DisplayName("Progress command from bi-function")
public class ProgressCommandFromBiFunctionTest extends AbstractProgressCommandTest
{
    @Nested
    @DisplayName("After create command from bi-function")
    class AfterCreateCommandFromBiFunction extends AfterCreateCommand<Integer, Integer>
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
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteProgressCommand<Integer, Integer>
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
            protected Integer getCorrectResult()
            {
                return RESULT;
            }

            @Nonnull
            @Override
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
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
        @DisplayName("After execute")
        class AfterExecute extends AfterExecuteCommand<Integer, Integer>
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
        @DisplayName("During execute with error")
        class DuringExecuteWithError extends DuringExecuteProgressCommandWithError<Integer, Integer>
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

            @Nonnull
            @Override
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f};
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
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Integer, Integer>
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

    @Nested
    @DisplayName("After create command from bi-function with observable")
    class AfterCreateCommandFromBiFunctionWithObservable extends AfterCreateCommandWithObservable<Integer, Integer>
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
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Integer, Integer>
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
        @DisplayName("After observable emits false")
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Integer, Integer>
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
        @DisplayName("After execute disabled command")
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Integer, Integer>
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
