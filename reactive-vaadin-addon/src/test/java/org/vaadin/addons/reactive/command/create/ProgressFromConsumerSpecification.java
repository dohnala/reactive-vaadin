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

package org.vaadin.addons.reactive.command.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vaadin.addons.reactive.ProgressContext;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveCommandExtension;
import org.vaadin.addons.reactive.command.CanExecuteEmitsValueSpecification;
import org.vaadin.addons.reactive.command.CreateSpecification;
import org.vaadin.addons.reactive.command.ExecuteSpecification;

/**
 * Tests for {@link ReactiveCommand} created by
 * {@link ReactiveCommandExtension#createProgressCommand(Consumer, Scheduler)}
 * {@link ReactiveCommandExtension#createProgressCommand(Observable, Consumer, Scheduler)}
 *
 * @author dohnal
 */
public interface ProgressFromConsumerSpecification extends
        CreateSpecification,
        ExecuteSpecification,
        CanExecuteEmitsValueSpecification
{
    abstract class AbstractProgressFromConsumerWithSchedulerSpecification extends
            AbstractCreateSpecification<Void, Void> implements ReactiveCommandExtension
    {
        protected ReplaySubject<ReactiveCommand<?, ?>> capturedCommands;
        protected Consumer<ProgressContext> execution;
        protected ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            capturedCommands = ReplaySubject.create();
            execution = Mockito.mock(Consumer.class);
            command = createProgressCommand(execution, Schedulers.from(Runnable::run));
        }

        @Nonnull
        @Override
        public <T, R> ReactiveCommand<T, R> onCreateCommand(final @Nonnull ReactiveCommand<T, R> command)
        {
            final ReactiveCommand<T, R> created = ReactiveCommandExtension.super.onCreateCommand(command);

            capturedCommands.onNext(created);

            return created;
        }

        @Test
        @DisplayName("Created command should be captured")
        public void testCreatedCommand()
        {
            capturedCommands.test().assertValue(command);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Execution should not be run")
        public void testExecution()
        {
            Mockito.verify(execution, Mockito.never()).accept(Mockito.any());
        }

        @Nested
        @DisplayName("Execute specification")
        class Execute extends AbstractExecuteSpecification<Void, Void>
        {
            private final Throwable ERROR = new RuntimeException("Error");

            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
            {
                return command;
            }

            @Nonnull
            @Override
            protected Observable<Void> execute()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);
                    progressContext.set(0.75f);
                    progressContext.set(1.0f);

                    return null;
                }).when(execution).accept(Mockito.any(ProgressContext.class));

                return command.execute();
            }

            @Nonnull
            protected Float[] getProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f};
            }

            @Nonnull
            @Override
            protected Observable<Void> executeWithError()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);

                    throw ERROR;
                }).when(execution).accept(Mockito.any(ProgressContext.class));

                return command.execute();
            }

            @Nonnull
            protected Float[] getErrorProgress()
            {
                return new Float[]{0.0f, 0.25f, 0.5f, 1.0f};
            }

            @Nullable
            @Override
            protected Void getResult()
            {
                return null;
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
                Mockito.verify(execution).accept(Mockito.any(ProgressContext.class));
            }
        }
    }

    abstract class AbstractProgressFromConsumerWithCanExecuteAndSchedulerSpecification
            extends AbstractCreateSpecification<Void, Void> implements ReactiveCommandExtension
    {
        protected ReplaySubject<ReactiveCommand<?, ?>> capturedCommands;
        protected Consumer<ProgressContext> execution;
        protected TestScheduler testScheduler;
        protected PublishSubject<Boolean> testSubject;
        protected ReactiveCommand<Void, Void> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            capturedCommands = ReplaySubject.create();
            execution = Mockito.mock(Consumer.class);
            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createProgressCommand(testSubject, execution, Schedulers.from(Runnable::run));
        }

        @Nonnull
        @Override
        public <T, R> ReactiveCommand<T, R> onCreateCommand(final @Nonnull ReactiveCommand<T, R> command)
        {
            final ReactiveCommand<T, R> created = ReactiveCommandExtension.super.onCreateCommand(command);

            capturedCommands.onNext(created);

            return created;
        }

        @Test
        @DisplayName("Created command should be captured")
        public void testCreatedCommand()
        {
            capturedCommands.test().assertValue(command);
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, Void> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("CanExecute emits value specification")
        class CanExecuteEmitsValue extends AbstractCanExecuteEmitsValueSpecification<Void, Void>
        {
            @Nonnull
            @Override
            public ReactiveCommand<Void, Void> getCommand()
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
            protected Observable<Void> execute()
            {
                Mockito.doAnswer(invocation -> {
                    final ProgressContext progressContext = invocation.getArgument(0);

                    progressContext.set(0.0f);
                    progressContext.set(0.25f);
                    progressContext.set(0.5f);
                    progressContext.set(0.75f);
                    progressContext.set(1.0f);

                    return null;
                }).when(execution).accept(Mockito.any(ProgressContext.class));

                return command.execute();
            }
        }
    }
}

