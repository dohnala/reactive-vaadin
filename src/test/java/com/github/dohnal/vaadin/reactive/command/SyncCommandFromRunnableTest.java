package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link SyncCommand} created by
 * {@link ReactiveCommand#create(Runnable)}
 * {@link ReactiveCommand#create(Observable, Runnable)}
 *
 * @author dohnal
 */
@DisplayName("Synchronous command from runnable")
public class SyncCommandFromRunnableTest extends AbstractSyncCommandTest
{
    @Nested
    @DisplayName("After create command from runnable")
    class AfterCreateCommandFromRunnable extends AfterCreateCommand<Void, Void>
    {
        private Runnable execution;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        protected void create()
        {
            execution = Mockito.mock(Runnable.class);
            command = ReactiveCommand.create(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Runnable should not be run")
        public void testRunnable()
        {
            Mockito.verify(execution, Mockito.never()).run();
        }

        @Nested
        @DisplayName("During execute")
        class DuringExecute extends DuringExecuteCommand<Void, Void>
        {
            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doNothing().when(execution).run();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
            protected Void getCorrectResult()
            {
                return null;
            }

            @Test
            @DisplayName("Runnable should be run")
            public void testRunnable()
            {
                command.execute(getInput());

                Mockito.verify(execution).run();
            }
        }

        @Nested
        @DisplayName("After execute")
        class AfterExecute extends AfterExecuteCommand<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
        class DuringExecuteWithError extends DuringExecuteCommandWithError<Void, Void>
        {
            private Throwable ERROR = new RuntimeException("Error");

            @BeforeEach
            protected void mockExecution()
            {
                Mockito.doThrow(ERROR).when(execution).run();
            }

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
            @DisplayName("Error should be thrown if no one is subscribed")
            public void testUnhandledError()
            {
                assertThrows(getError().getClass(), () -> getCommand().execute(getInput()));
            }

            @Test
            @DisplayName("Runnable should be run")
            public void testRunnable()
            {
                assertThrows(getError().getClass(), () -> command.execute(getInput()));

                Mockito.verify(execution).run();
            }
        }

        @Nested
        @DisplayName("After execute with error")
        class AfterExecuteWithError extends AfterExecuteCommandWithError<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
    @DisplayName("After create command from runnable with observable")
    class AfterCreateCommandFromRunnableWithObservable extends AfterCreateCommandWithObservable<Void, Void>
    {
        private Runnable execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        protected void create()
        {
            execution = Mockito.mock(Runnable.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.create(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("After observable emits true")
        class AfterEmitsTrue extends AfterObservableEmitsTrue<Void, Void>
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
        @DisplayName("After observable emits false")
        class AfterEmitsFalse extends AfterObservableEmitsFalse<Void, Void>
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
        @DisplayName("After execute disabled command")
        class AfterExecuteDisabled extends AfterExecuteDisabledCommand<Void, Void>
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
            protected Void getInput()
            {
                return null;
            }

            @Test
            @DisplayName("Runnable should not be run")
            public void testRunnable()
            {
                command.execute(getInput());

                Mockito.verify(execution, Mockito.never()).run();
            }
        }
    }
}
