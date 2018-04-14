package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author dohnal
 */
@DisplayName("Asynchronous command from function")
public class AsyncCommandFromFunctionTest extends AbstractAsyncCommandTest
{
    @Nested
    @DisplayName("After create command from function")
    class AfterCreateCommandFromFunction extends AfterCreateCommand<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Integer, Integer> execution;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            command = ReactiveCommand.createAsync(execution, testExecutor);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Function should not be run")
        public void testFunction()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.anyInt());
        }

        @Nested
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteCommand<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);
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

            @Test
            @DisplayName("Function should be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution).apply(INPUT);
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
        @DisplayName("After execute with error")
        class DuringExecuteWithError extends DuringExecuteCommandWithError<Integer, Integer>
        {
            protected final Integer INPUT = 5;
            protected final Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);
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
            @DisplayName("Function should be run")
            public void testFunction()
            {
                assertThrows(getError().getClass(), () -> command.execute(getInput()));

                Mockito.verify(execution).apply(INPUT);
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
                return INPUT;
            }
        }
    }

    @Nested
    @DisplayName("After create command from function with observable")
    class AfterCreateCommandFromRunnableWithCanExecute extends AfterCreateCommandWithObservable<Integer, Integer>
    {
        private TestExecutor testExecutor;
        private Function<Integer, Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Function.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution, testExecutor);
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
            @DisplayName("Function should not be run")
            public void testFunction()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
            }
        }
    }
}
