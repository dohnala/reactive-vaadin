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
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ProgressContext;
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
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(Function)}
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(Function, Scheduler)}
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(Observable, Function)}
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(Observable, Function, Scheduler)}
 *
 * @author dohnal
 */
public interface ProgressFromObservableFunctionSpecification extends
        CreateSpecification,
        ExecuteSpecification,
        CanExecuteEmitsValueSpecification
{
    abstract class AbstractProgressFromObservableFunctionSpecification extends AbstractCreateSpecification<Void, Integer>
            implements ReactiveCommandFactory
    {
        protected Function<ProgressContext, Observable<Integer>> execution;
        protected ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            command = createProgressCommandFromObservable(execution);
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
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any());
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

            @Nonnull
            @Override
            protected Observable<Integer> execute()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    return Observable.just(0.0f, 0.25f, 0.5f, 0.75f, 1.0f)
                            .doOnNext(progressContext::set)
                            .ignoreElements()
                            .toObservable()
                            .concatWith(Observable.just(RESULT));
                }).when(execution).apply(Mockito.any(ProgressContext.class));

                return command.execute();
            }

            @Nonnull
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
            }

            @Nonnull
            @Override
            protected Observable<Integer> executeWithError()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    return Observable.just(0.0f, 0.25f, 0.5f)
                            .doOnNext(progressContext::set)
                            .ignoreElements()
                            .toObservable()
                            .concatWith(Observable.error(ERROR));
                }).when(execution).apply(Mockito.any(ProgressContext.class));

                return command.execute();
            }

            @Nonnull
            protected Float[] getErrorProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 1.0f};
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
                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class));
            }
        }
    }

    abstract class AbstractProgressFromObservableFunctionWithSchedulerSpecification
            extends AbstractProgressFromObservableFunctionSpecification
    {
        @Override
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            command = createProgressCommandFromObservable(execution, Schedulers.from(Runnable::run));
        }
    }

    abstract class AbstractProgressFromObservableFunctionWithCanExecuteSpecification
            extends AbstractCreateSpecification<Void, Integer> implements ReactiveCommandFactory
    {
        protected Function<ProgressContext, Integer> execution;
        protected TestScheduler testScheduler;
        protected PublishSubject<Boolean> testSubject;
        protected ReactiveCommand<Void, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution);
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

            @Nonnull
            @Override
            protected Observable<Integer> execute()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    return Observable.just(0.0f, 0.25f, 0.5f, 0.75f, 1.0f)
                            .doOnNext(progressContext::set)
                            .ignoreElements()
                            .toObservable()
                            .concatWith(Observable.just(RESULT));
                }).when(execution).apply(Mockito.any(ProgressContext.class));

                return command.execute();
            }
        }
    }

    abstract class AbstractProgressFromObservableFunctionWithCanExecuteAndSchedulerSpecification
            extends AbstractProgressFromObservableFunctionWithCanExecuteSpecification
    {
        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(Function.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution, Schedulers.from(Runnable::run));
        }
    }
}
