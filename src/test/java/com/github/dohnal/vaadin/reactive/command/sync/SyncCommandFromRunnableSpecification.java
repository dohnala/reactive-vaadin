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
 * {@link ReactiveCommandFactory#createCommand(Runnable)}
 * {@link ReactiveCommandFactory#createCommand(Observable, Runnable)}
 *
 * @author dohnal
 */
public interface SyncCommandFromRunnableSpecification extends BaseCommandSpecification
{
    abstract class WhenCreateFromRunnableSpecification extends WhenCreateSpecification<Void, Void>
            implements ReactiveCommandFactory
    {
        private Runnable execution;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        protected void create()
        {
            execution = Mockito.mock(Runnable.class);
            command = createCommand(execution);
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
        @DisplayName("When command is executed")
        class WhenExecute extends WhenExecuteSpecification<Void, Void>
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

            @Override
            protected void execute()
            {
                command.execute();
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
            @DisplayName("Runnable should be run")
            public void testRunnable()
            {
                command.execute();

                Mockito.verify(execution).run();
            }
        }

        @Nested
        @DisplayName("When command is executed with error")
        class WhenExecuteWithError extends WhenExecuteWithErrorSpecification<Void, Void>
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

            @Override
            protected void execute()
            {
                command.execute();
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
            @DisplayName("Runnable should be run")
            public void testRunnable()
            {
                assertThrows(getError().getClass(), this::execute);

                Mockito.verify(execution).run();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution")
        class WhenSubscribeAfterExecute extends WhenSubscribeAfterExecuteSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }
        }

        @Nested
        @DisplayName("When command is subscribed after execution with error")
        class WhenSubscribeAfterExecuteWithError extends WhenSubscribeAfterExecuteWithErrorSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                command.execute();
            }
        }
    }

    abstract class WhenCreateFromRunnableWithCanExecuteSpecification extends
            WhenCreateWithCanExecuteSpecification<Void, Void> implements ReactiveCommandFactory
    {
        private Runnable execution;
        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, Void> command;

        @BeforeEach
        protected void create()
        {
            execution = Mockito.mock(Runnable.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommand(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("When CanExecute observable emits true")
        class WhenCanExecuteEmitsTrue extends WhenCanExecuteEmitsTrueSpecification<Void, Void>
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
        @DisplayName("When CanExecute observable emits false")
        class WhenCanExecuteEmitsFalse extends WhenCanExecuteEmitsFalseSpecification<Void, Void>
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
        @DisplayName("When command is executed while disabled")
        class WhenExecuteWhileDisabled extends WhenExecuteWhileDisabledSpecification<Void, Void>
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
            protected void execute()
            {
                command.execute();
            }
        }
    }
}
