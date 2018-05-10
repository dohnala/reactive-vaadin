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

package com.github.dohnal.vaadin.reactive.command.factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import com.github.dohnal.vaadin.reactive.command.CanExecuteEmitsValueSpecification;
import com.github.dohnal.vaadin.reactive.command.CreateSpecification;
import com.github.dohnal.vaadin.reactive.command.ExecuteSpecification;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
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
 * {@link ReactiveCommandFactory#createCommandFromObservable(Supplier)}
 * {@link ReactiveCommandFactory#createCommandFromObservable(Observable, Supplier)}
 * {@link ReactiveCommandFactory#createCommandFromObservable(Supplier, Scheduler)}
 * {@link ReactiveCommandFactory#createCommandFromObservable(Observable, Supplier, Scheduler)}
 *
 * @author dohnal
 */
public interface FromObservableSupplierSpecification extends
        CreateSpecification,
        ExecuteSpecification,
        CanExecuteEmitsValueSpecification
{
    abstract class AbstractFromObservableSupplierSpecification extends AbstractCreateSpecification<Void, Integer>
            implements ReactiveCommandFactory
    {
        protected Supplier<Observable<Integer>> execution;
        protected ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Supplier.class);
            command = createCommandFromObservable(execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Execution should not be run")
        public void testExecution()
        {
            Mockito.verify(execution, Mockito.never()).get();
        }

        @Nested
        @DisplayName("Execute specification")
        class Execute extends AbstractExecuteSpecification<Void, Integer>
        {
            private final Integer RESULT = 7;
            private final Throwable ERROR = new RuntimeException("Error");

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                Mockito.when(execution.get()).thenReturn(Observable.just(RESULT));
                command.execute();
            }

            @Override
            protected void executeWithError()
            {
                Mockito.when(execution.get()).thenReturn(Observable.error(ERROR));
                command.execute();
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
            protected void verifyExecution()
            {
                Mockito.verify(execution).get();
            }
        }
    }

    abstract class AbstractFromObservableSupplierWithSchedulerSpecification extends AbstractFromObservableSupplierSpecification
    {
        @Override
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Supplier.class);
            command = createCommandFromObservable(execution, Schedulers.from(Runnable::run));
        }
    }

    abstract class AbstractFromObservableSupplierWithCanExecuteSpecification
            extends AbstractCreateSpecification<Void, Integer> implements ReactiveCommandFactory
    {
        protected Supplier<Observable<Integer>> execution;
        protected TestScheduler testScheduler;
        protected PublishSubject<Boolean> testSubject;
        protected ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Supplier.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommandFromObservable(testSubject, execution);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Integer> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("CanExecute emits value specification")
        class CanExecuteEmitsValue extends AbstractCanExecuteEmitsValueSpecification<Void, Integer>
        {
            private final Integer RESULT = 7;

            @Nonnull
            @Override
            public ReactiveCommand<Void, Integer> getCommand()
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
            protected void execute()
            {
                Mockito.when(execution.get()).thenReturn(Observable.just(RESULT));
                command.execute();
            }
        }
    }

    abstract class AbstractFromObservableSupplierWithCanExecuteAndSchedulerSpecification
            extends AbstractFromObservableSupplierWithCanExecuteSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Supplier.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCommandFromObservable(testSubject, execution, Schedulers.from(Runnable::run));
        }
    }
}

