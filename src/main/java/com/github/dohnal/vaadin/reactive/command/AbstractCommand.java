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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyFactory;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Abstract implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public abstract class AbstractCommand<T, R> implements ReactiveCommand<T, R>, ReactivePropertyFactory
{
    protected final PublishSubject<R> result;

    protected final PublishSubject<Throwable> error;

    protected final ReactiveProperty<Boolean> isExecuting;

    protected final ReactiveProperty<Integer> executionCount;

    protected final ReactiveProperty<Boolean> canExecute;

    protected final ReactiveProperty<Float> progress;

    /**
     * Creates new command reactive command
     *
     * @param canExecute observable which controls command executability
     */
    public AbstractCommand(final @Nonnull Observable<Boolean> canExecute)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");

        this.result = PublishSubject.create();
        this.error = PublishSubject.create();
        this.isExecuting = createProperty(false);
        this.executionCount = createProperty(0);

        // By default, command cannot be executed while it is executing
        final Observable<Boolean> defaultCanExecute = this.isExecuting.asObservable().map(value -> !value);

        // Combine default command executability with custom one by performing logical and
        this.canExecute = createPropertyFrom(
                Observable.combineLatest(
                        defaultCanExecute,
                        canExecute.startWith(true),
                        (x, y) -> x && y));

        this.progress = createProperty(0.0f);
    }

    /**
     * Internally executes this command
     */
    protected abstract void executeInternal(final @Nullable T input);

    @Nonnull
    @Override
    public final Observable<R> getResult()
    {
        return result;
    }

    @Nonnull
    @Override
    public final Observable<Throwable> getError()
    {
        return error;
    }

    @Nonnull
    @Override
    public final Observable<Boolean> isExecuting()
    {
        return isExecuting.asObservable();
    }

    @Nonnull
    @Override
    public final Observable<Integer> getExecutionCount()
    {
        return executionCount.asObservable();
    }

    @Nonnull
    @Override
    public final Observable<Boolean> hasBeenExecuted()
    {
        return getExecutionCount()
                .map(count -> count > 0)
                .distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<Boolean> canExecute()
    {
        return canExecute.asObservable();
    }

    @Nonnull
    @Override
    public final Observable<Float> getProgress()
    {
        return progress.asObservable();
    }

    @Override
    public final void execute()
    {
        if (Boolean.TRUE.equals(canExecute.getValue()))
        {
            executeInternal(null);
        }
    }

    @Override
    public final void execute(final @Nonnull T input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        if (Boolean.TRUE.equals(canExecute.getValue()))
        {
            executeInternal(input);
        }
    }

    /**
     * Handles start of command execution
     */
    protected final void handleStart()
    {
        this.progress.setValue(0.0f);
        this.isExecuting.setValue(true);
    }

    /**
     * Handles result of command execution
     *
     * @param result result
     */
    protected final void handleResult(final @Nonnull R result)
    {
        this.result.onNext(result);
    }

    /**
     * Handle unhandled error produced during command execution
     *
     * @param throwable error
     */
    protected final void handleError(final @Nonnull Throwable throwable)
    {
        Objects.requireNonNull(throwable, "Throwable cannot be null");

        if (this.error.hasObservers())
        {
            this.error.onNext(throwable);
        }
        else
        {
            // TODO: log
            // TODO: handle unhandled error
        }
    }

    /**
     * Handles completion of command execution
     */
    protected final void handleComplete()
    {
        this.progress.setValue(1.0f);
        this.isExecuting.setValue(false);
        this.executionCount.updateValue(count -> count + 1);
    }
}
