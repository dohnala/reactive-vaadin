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

package com.github.dohnal.vaadin.reactive.command.sync;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import com.github.dohnal.vaadin.reactive.command.BaseCommandSpecification;
import io.reactivex.Observable;
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
 * Specification for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createCommand(Consumer)}
 * {@link ReactiveCommandFactory#createCommand(Observable, Consumer)}
 *
 * @author dohnal
 */
public interface SyncCommandFromConsumerSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromConsumerSpecification extends WhenCreateSpecification<Integer, Void>
            implements ReactiveCommandFactory
    {
        private Consumer<Integer> execution;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            command = createCommand(execution);
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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }

            @Test
            @DisplayName("Result observable should not emit any value")
            public void testResult()
            {
                final TestObserver<Void> testObserver = getCommand().getResult().test();

                execute();

                testObserver.assertNoValues();
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                execute();

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
            @DisplayName("Error should be thrown if no one is subscribed to Error observable")
            public void testUnhandledError()
            {
                assertThrows(getError().getClass(), this::execute);
            }

            @Test
            @DisplayName("Consumer should be run")
            public void testConsumer()
            {
                assertThrows(getError().getClass(), this::execute);

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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
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

            @Override
            protected void execute()
            {
                command.execute(INPUT);
            }
        }
    }

    abstract class WhenCreateFromConsumerWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Integer, Void> implements ReactiveCommandFactory
    {
        private Consumer<Integer> execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Integer, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        protected void create()
        {
            execution = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommand(testSubject, execution);
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
            protected void execute()
            {
                command.execute(INPUT);
            }
        }
    }
}
