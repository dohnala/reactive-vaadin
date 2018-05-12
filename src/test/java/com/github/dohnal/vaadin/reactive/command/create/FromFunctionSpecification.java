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

package com.github.dohnal.vaadin.reactive.command.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandExtension;
import com.github.dohnal.vaadin.reactive.command.CanExecuteEmitsValueSpecification;
import com.github.dohnal.vaadin.reactive.command.CreateSpecification;
import com.github.dohnal.vaadin.reactive.command.ExecuteSpecification;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Specification for {@link ReactiveCommand} created by
 * {@link ReactiveCommandExtension#createCommand(Function)}
 * {@link ReactiveCommandExtension#createCommand(Observable, Function)}
 * {@link ReactiveCommandExtension#createCommand(Function, Scheduler)}
 * {@link ReactiveCommandExtension#createCommand(Observable, Function, Scheduler)}
 *
 * @author dohnal
 */
public interface FromFunctionSpecification extends
        CreateSpecification,
        ExecuteSpecification,
        CanExecuteEmitsValueSpecification
{
    abstract class AbstractFromFunctionSpecification extends AbstractCreateSpecification<Integer, Integer>
            implements ReactiveCommandExtension
    {
        protected Function<Integer, Integer> execution;
        protected ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            command = createCommand(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Execution should not be run")
        public void testExecution()
        {
            Mockito.verify(execution, Mockito.never()).apply(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class ExecuteWithNoInput
        {
            @Test
            @DisplayName("Error observable shout emit NullPointerException")
            public void testExecute()
            {
                final TestObserver<Throwable> testObserver = command.getError().test();

                command.execute().subscribe();

                testObserver.assertValue(error -> error.getClass().equals(NullPointerException.class));
            }
        }

        @Nested
        @DisplayName("Execute specification")
        class Execute extends AbstractExecuteSpecification<Integer, Integer>
        {
            private final Integer INPUT = 5;
            private final Integer RESULT = 7;
            private final Throwable ERROR = new RuntimeException("Error");

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Nonnull
            @Override
            protected Observable<Integer> execute()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);

                return command.execute(INPUT);
            }

            @Nonnull
            @Override
            protected Observable<Integer> executeWithError()
            {
                Mockito.when(execution.apply(INPUT)).thenThrow(ERROR);

                return command.execute(INPUT);
            }

            @Nullable
            @Override
            protected Integer getResult()
            {
                return RESULT;
            }

            @Nullable
            @Override
            protected Throwable getError()
            {
                return ERROR;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void clearExecution()
            {
                Mockito.clearInvocations(execution);
            }

            @Override
            protected void verifyExecution()
            {
                Mockito.verify(execution).apply(INPUT);
            }
        }
    }

    abstract class AbstractFromFunctionWithSchedulerSpecification extends AbstractFromFunctionSpecification
    {
        @Override
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            command = createCommand(execution, Schedulers.from(Runnable::run));
        }
    }

    abstract class AbstractFromFunctionWithCanExecuteSpecification
            extends AbstractCreateSpecification<Integer, Integer> implements ReactiveCommandExtension
    {
        protected Function<Integer, Integer> execution;
        protected TestScheduler testScheduler;
        protected PublishSubject<Boolean> testSubject;
        protected ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommand(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("CanExecute emits value specification")
        class CanExecuteEmitsValue extends AbstractCanExecuteEmitsValueSpecification<Integer, Integer>
        {
            private final Integer INPUT = 5;
            private final Integer RESULT = 7;

            @Nonnull
            @Override
            public ReactiveCommand<Integer, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void emitValue(final @Nonnull Boolean value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }

            @Override
            protected void emitError(final @Nonnull Throwable error)
            {
                testSubject.onError(error);
                testScheduler.triggerActions();
            }

            @Nonnull
            @Override
            protected Observable<Integer> execute()
            {
                Mockito.when(execution.apply(INPUT)).thenReturn(RESULT);

                return command.execute(INPUT);
            }
        }
    }

    abstract class AbstractFromFunctionWithCanExecuteAndSchedulerSpecification
            extends AbstractFromFunctionWithCanExecuteSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommand(testSubject, execution, Schedulers.from(Runnable::run));
        }
    }
}


