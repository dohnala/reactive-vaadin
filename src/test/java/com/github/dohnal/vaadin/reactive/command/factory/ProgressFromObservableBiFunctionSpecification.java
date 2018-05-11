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
import java.util.function.BiFunction;

import com.github.dohnal.vaadin.reactive.ProgressContext;
import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
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
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(BiFunction, Scheduler)}
 * {@link ReactiveCommandFactory#createProgressCommandFromObservable(Observable, BiFunction, Scheduler)}
 *
 * @author dohnal
 */
public interface ProgressFromObservableBiFunctionSpecification extends
        CreateSpecification,
        ExecuteSpecification,
        CanExecuteEmitsValueSpecification
{
    abstract class AbstractProgressFromObservableBiFunctionWithSchedulerSpecification extends
            AbstractCreateSpecification<Integer, Integer> implements ReactiveCommandFactory
    {
        protected BiFunction<ProgressContext, Integer, Observable<Integer>> execution;
        protected ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(BiFunction.class);
            command = createProgressCommandFromObservable(execution, Schedulers.from(Runnable::run));
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
            Mockito.verify(execution, Mockito.never()).apply(Mockito.any(), Mockito.anyInt());
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
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    return Observable.just(0.0f, 0.25f, 0.5f, 0.75f, 1.0f)
                            .doOnNext(progressContext::set)
                            .ignoreElements()
                            .toObservable()
                            .concatWith(Observable.just(RESULT));
                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.eq(INPUT));

                return command.execute(INPUT);
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
                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.eq(INPUT));

                return command.execute(INPUT);
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
                Mockito.verify(execution).apply(Mockito.any(ProgressContext.class), Mockito.eq(INPUT));
            }
        }
    }

    abstract class AbstractProgressFromObservableBiFunctionWithCanExecuteAndSchedulerSpecification
            extends AbstractCreateSpecification<Integer, Integer> implements ReactiveCommandFactory
    {
        protected BiFunction<ProgressContext, Integer, Observable<Integer>> execution;
        protected TestScheduler testScheduler;
        protected PublishSubject<Boolean> testSubject;
        protected ReactiveCommand<Integer, Integer> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            execution = Mockito.mock(BiFunction.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommandFromObservable(testSubject, execution, Schedulers.from(Runnable::run));
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
                }).when(execution).apply(Mockito.any(ProgressContext.class), Mockito.eq(INPUT));

                return command.execute(INPUT);
            }
        }
    }
}
