package com.github.dohnal.vaadin.reactive.command.sync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
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
 * Specification for {@link ReactiveCommand} created by
 * {@link ReactiveCommand#create(Consumer)}
 * {@link ReactiveCommand#create(Observable, Consumer)}
 *
 * @author dohnal
 */
public interface SyncCommandFromConsumerSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromConsumerSpecification extends WhenCreateSpecification<Integer, Void>
    {
        private Consumer<Integer> execution;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            command = ReactiveCommand.create(execution);
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
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Integer, Void>
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
            protected Void getResult()
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
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Integer, Void>
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
            @DisplayName("Error should be thrown if no one is subscribed to Error observable")
            public void testUnhandledError()
            {
                assertThrows(getError().getClass(), () -> getCommand().execute(getInput()));
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                assertThrows(getError().getClass(), () -> command.execute(getInput()));

                Mockito.verify(execution).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Integer, Void>
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
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Integer, Void>
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

    abstract class WhenCreateFromConsumerWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Void>
    {
        private Consumer<Integer> execution;
        private TestScheduler testScheduler;
        private TestSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            command = ReactiveCommand.create(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Integer, Void>
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
        @DisplayName("When CanExecute observable emits false")
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Integer, Void>
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
        @DisplayName("When command is executed while disabled")
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Integer, Void>
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
        }
    }
}