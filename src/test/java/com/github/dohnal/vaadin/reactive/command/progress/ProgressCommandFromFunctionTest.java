package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.Function;

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
 * {@link ReactiveCommand#createProgress(Function)}
 * {@link ReactiveCommand#createProgress(Function, Executor)}
 *
 * @author dohnal
 */
@DisplayName("Progress command from function")
public class ProgressCommandFromFunctionTest extends AbstractProgressCommandTest
{
    @Nested
    @DisplayName("After create command from function")
    class AfterCreateCommandFromFunction extends AfterCreateCommand<Void, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Progress, Integer> execution;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            command = ReactiveCommand.createProgress(execution, testExecutor);
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
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteProgressCommand<Void, Integer>
        {
            protected final Integer RESULT = 5;

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
                }).when(execution).apply(Mockito.any(Progress.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class));
            }
        }

        @Nested
        @DisplayName("After execute")
        class AfterExecute extends AfterExecuteCommand<Void, Integer>
        {
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
        @DisplayName("During execute with error")
        class DuringExecuteWithError extends DuringExecuteProgressCommandWithError<Void, Integer>
        {
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

                }).when(execution).apply(Mockito.any(Progress.class));
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(Mockito.any(Progress.class));
            }
        }

        @Nested
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Void, Integer>
        {
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

    @Nested
    @DisplayName("After create command from function with observable")
    class AfterCreateCommandFromFunctionWithObservable extends AfterCreateCommandWithObservable<Void, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Progress, Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createProgress(testSubject, execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Void, Integer>
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
        @DisplayName("After observable emits false")
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Void, Integer>
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
        @DisplayName("After execute disabled command")
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Void, Integer>
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
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
