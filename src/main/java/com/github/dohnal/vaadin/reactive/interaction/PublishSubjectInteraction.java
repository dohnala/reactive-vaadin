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

package com.github.dohnal.vaadin.reactive.interaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.InteractionContext;
import com.github.dohnal.vaadin.reactive.ReactiveInteraction;
import com.github.dohnal.vaadin.reactive.exceptions.AlreadyHandledInteractionException;
import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Basic implementation of {@link ReactiveInteraction} based on {@link PublishSubject}
 *
 * @param <T> type of input
 * @param <R> type of result
 * @author dohnal
 */
public final class PublishSubjectInteraction<T, R> implements ReactiveInteraction<T, R>
{
    private final PublishSubject<InteractionContext<T, R>> subject;

    /**
     * Creates new interaction
     */
    public PublishSubjectInteraction()
    {
        this.subject = PublishSubject.create();
    }

    @Override
    public void invoke(final @Nullable T input, final @Nonnull Consumer<? super R> action)
    {
        if (!subject.hasObservers())
        {
            throw new UnhandledInteractionException("No handler is subscribed");
        }

        subject.onNext(new DefaultInteractionContext(input, action));
    }

    @Override
    public void invoke(final @Nonnull Consumer<? super R> action)
    {
        invoke(null, action);
    }

    @Override
    public void invoke(final @Nullable T input, final @Nonnull Runnable action)
    {
        invoke(input, result -> action.run());
    }

    @Override
    public void invoke(final @Nonnull Runnable action)
    {
        invoke(null, action);
    }

    @Nonnull
    @Override
    public Observable<InteractionContext<T, R>> asObservable()
    {
        return subject;
    }

    private class DefaultInteractionContext implements InteractionContext<T, R>
    {
        private final T input;

        private final Consumer<? super R> action;

        private final AtomicReference<Boolean> isHandled;

        public DefaultInteractionContext(final @Nullable T input,
                                         final @Nonnull Consumer<? super R> action)
        {
            this.input = input;
            this.action = action;
            this.isHandled = new AtomicReference<>(false);
        }

        @Nullable
        @Override
        public T getInput()
        {
            return input;
        }

        @Override
        public boolean isHandled()
        {
            return Boolean.TRUE.equals(isHandled.get());
        }

        @Override
        public void handle(final @Nullable R result)
        {
            if (isHandled.compareAndSet(false, true))
            {
                action.accept(result);
            }
            else
            {
                throw new AlreadyHandledInteractionException("Interaction is already handled");
            }
        }
    }
}
