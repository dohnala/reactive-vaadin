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

package org.vaadin.addons.reactive.command;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;
import io.reactivex.subjects.PublishSubject;
import org.vaadin.addons.reactive.ReactiveCommand;
import org.vaadin.addons.reactive.ReactiveProperty;
import org.vaadin.addons.reactive.ReactivePropertyExtension;
import org.vaadin.addons.reactive.exceptions.CannotExecuteCommandException;

/**
 * Abstract implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public abstract class AbstractCommand<T, R> implements ReactiveCommand<T, R>, ReactivePropertyExtension
{
    protected final PublishSubject<R> result;

    protected final PublishSubject<Throwable> error;

    protected final ReactiveProperty<Boolean> isExecuting;

    protected final ReactiveProperty<Integer> executionCount;

    protected final ReactiveProperty<Boolean> canExecute;

    protected final ReactiveProperty<Float> progress;

    protected final Observable<Float> customProgress;

    protected SerialDisposable progressDisposable;

    /**
     * Creates new command reactive command
     *
     * @param canExecute observable which controls command executability
     */
    public AbstractCommand(final @Nonnull Observable<Boolean> canExecute,
                           final @Nonnull Observable<Float> customProgress)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(customProgress, "Custom progress cannot be null");

        this.result = PublishSubject.create();
        this.error = PublishSubject.create();
        this.isExecuting = createProperty(false);
        this.executionCount = createProperty(0);

        // By default, command cannot be executed while it is executing
        final Observable<Boolean> defaultCanExecute = this.isExecuting.asObservable().map(value -> !value);

        // Combine default command executability with custom one by performing logical and
        this.canExecute = createPropertyFrom(Observable
                .combineLatest(defaultCanExecute, canExecute.startWith(true), (x, y) -> x && y));

        this.customProgress = customProgress;

        this.progress = createProperty(0.0f);

        this.progressDisposable = new SerialDisposable();
    }

    /**
     * Internally executes this command
     *
     * @return execution pipeline which will be executed after its subscribed
     */
    @Nonnull
    protected abstract Observable<R> executeInternal(final @Nonnull Optional<T> input);

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
        return isExecuting.asObservable().distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<Integer> getExecutionCount()
    {
        return executionCount.asObservable().distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<Boolean> hasBeenExecuted()
    {
        return getExecutionCount().map(count -> count > 0).distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<Boolean> canExecute()
    {
        return canExecute.asObservable().distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<Float> getProgress()
    {
        return progress.asObservable().distinctUntilChanged();
    }

    @Nonnull
    @Override
    public final Observable<R> execute()
    {
        return executeInternal(Optional.empty());
    }

    @Nonnull
    @Override
    public final Observable<R> execute(final @Nonnull T input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        return executeInternal(Optional.of(input));
    }

    /**
     * Checks whether command can be executed
     *
     * @param input input for command execution
     * @return input stage of execution pipeline or error if command cannot be executed
     * @throws CannotExecuteCommandException if command cannot be executed
     */
    @Nonnull
    protected final Observable<Optional<T>> checkCanExecute(final @Nonnull Optional<T> input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        if (Boolean.TRUE.equals(canExecute.getValue()))
        {
            return Observable.just(input);
        }

        return Observable.error(new CannotExecuteCommandException(this));
    }

    /**
     * Handles start of command execution
     *
     * @param input input for command execution
     */
    protected final void handleStart(final @Nonnull Optional<T> input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        progress.setValue(0.0f);
        isExecuting.setValue(true);

        progressDisposable.set(customProgress.subscribe(progress::setValue));
    }

    /**
     * Handles result of command execution
     *
     * @param result result
     */
    protected final void handleResult(final @Nonnull R result)
    {
        Objects.requireNonNull(result, "Result cannot be null");

        this.result.onNext(result);
    }

    /**
     * Handle error produced during command execution
     *
     * @param throwable error
     */
    protected final Observable<? extends R> handleError(final @Nonnull Throwable throwable)
    {
        Objects.requireNonNull(throwable, "Throwable cannot be null");

        if (this.error.hasObservers())
        {
            error.onNext(throwable);

            return Observable.empty();
        }
        else
        {
            return Observable.error(throwable);
        }
    }

    /**
     * Handles completion of command execution
     */
    protected final void handleComplete()
    {
        Optional.ofNullable(progressDisposable.get()).ifPresent(Disposable::dispose);

        this.progress.setValue(1.0f);
        this.isExecuting.setValue(false);
        this.executionCount.updateValue(count -> count + 1);
    }
}
