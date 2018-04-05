package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import rx.Observable;

/**
 * Abstract implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public abstract class AbstractCommand<T, R> implements ReactiveCommand<T, R>
{
    protected final ReactiveProperty<R> result;

    protected final ReactiveProperty<Throwable> error;

    protected final ReactiveProperty<Boolean> isExecuting;

    protected final ReactiveProperty<Integer> executionCount;

    protected final ReactiveProperty<Boolean> canExecute;

    /**
     * Creates new command reactive command
     *
     * @param canExecute observable which controls command executability
     */
    public AbstractCommand(final @Nonnull Observable<Boolean> canExecute)
    {
        this.result = ReactiveProperty.empty();
        this.error = ReactiveProperty.empty();
        this.isExecuting = ReactiveProperty.withValue(false);
        this.executionCount = ReactiveProperty.withValue(0);

        // By default, command cannot be executed while it is executing
        final Observable<Boolean> defaultCanExecute = this.isExecuting.asObservable().map(value -> !value);

        // Combine default command executability with custom one by performing logical and
        this.canExecute = ReactiveProperty.fromObservable(
                Observable.combineLatest(
                        defaultCanExecute,
                        canExecute,
                        (x, y) -> x && y));
    }

    /**
     * Internally executes this command
     */
    protected abstract void executeInternal(final @Nullable T input);

    @Nonnull
    @Override
    public final Observable<R> getResult()
    {
        return result.asObservable();
    }

    @Nonnull
    @Override
    public final Observable<Throwable> getError()
    {
        return error.asObservable();
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

    @Override
    public final void execute(final @Nullable T input)
    {
        if (Boolean.TRUE.equals(canExecute.getValue()))
        {
            executeInternal(input);
        }
    }

    /**
     * Handles result of command execution
     *
     * @param result result
     * @param error error
     */
    protected final void handleResult(final @Nullable R result, final @Nullable Throwable error)
    {
        this.result.setValue(result);

        if (error != null)
        {
            if (this.error.hasObservers())
            {
                this.error.setValue(error);
            }
            else
            {
                throw new RuntimeException("Unexpected error during command execution", error);
            }
        }

        this.executionCount.updateValue(count -> count + 1);
    }
}
