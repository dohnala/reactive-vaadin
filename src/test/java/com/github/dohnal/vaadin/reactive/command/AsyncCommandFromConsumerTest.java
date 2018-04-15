package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Tests for {@link AsyncCommand} created by
 * {@link ReactiveCommand#createAsync(Consumer, Executor)}
 * {@link ReactiveCommand#createAsync(Observable, Consumer, Executor)}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous command from consumer")
public class AsyncCommandFromConsumerTest extends AbstractAsyncCommandTest
{
    @Nested
    @DisplayName("After create command from consumer")
    class AfterCreateCommandFromConsumer extends AfterCreateCommand<Integer, Void>
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
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteCommand<Integer, Void>
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

            @Nullable
            @Override
            protected Integer getInput()
            {
                return INPUT;
            }

            @Nullable
            @Override
            protected Void getCorrectResult()
            {
                return null;
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                command.execute(getInput());

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("After execute")
        class AfterExecute extends AfterExecuteCommand<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
        class DuringExecuteWithError extends DuringExecuteCommandWithError<Integer, Void>
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
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                command.execute(getInput());

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Integer, Void>
        {
            protected final Integer INPUT = 5;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Void> getCommand()
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
    @DisplayName("After create command from consumer with observable")
    class AfterCreateCommandFromRunnableWithObservable extends AfterCreateCommandWithObservable<Integer, Void>
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
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Integer, Void>
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
        @DisplayName("After observable emits false")
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Integer, Void>
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
        @DisplayName("After execute disabled command")
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Integer, Void>
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
            protected Integer getInput()
            {
                return INPUT;
            }

            @Test
            @DisplayName("Consumer should not be run")
            public void testConsumer()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
            }
        }
    }
}
