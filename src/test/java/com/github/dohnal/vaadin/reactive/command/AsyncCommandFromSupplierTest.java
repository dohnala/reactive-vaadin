package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

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
@DisplayName("Asynchronous command from supplier")
public class AsyncCommandFromSupplierTest extends AbstractAsyncCommandTest
{
    @Nested
    @DisplayName("After create command from supplier")
    class AfterCreateCommandFromSupplier extends AfterCreateCommand<Void, Integer>
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
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteCommand<Void, Integer>
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

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                command.execute(getInput());

                Mockito.verify(execution).get();
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
        @DisplayName("After execute with error")
        class DuringExecuteWithError extends DuringExecuteCommandWithError<Void, Integer>
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

            @Test
            @DisplayName("Supplier should be run")
            public void testSupplier()
            {
                assertThrows(getError().getClass(), () -> command.execute(getInput()));

                Mockito.verify(execution).get();
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
    @DisplayName("After create command from supplier with observable")
    class AfterCreateCommandFromSupplierWithCanExecute extends AfterCreateCommandWithObservable<Void, Integer>
    {
        private TestExecutor testExecutor;
        private Supplier<Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            testExecutor = new TestExecutor();
            execution = Mockito.mock(Supplier.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.createAsync(testSubject, execution, testExecutor);
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
            @DisplayName("Supplier should not be run")
            public void testSupplier()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).get();
            }
        }
    }
}
