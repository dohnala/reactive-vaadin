package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.mvvm.command.SyncCommand;
import rx.Observable;

/**
 * Reactive command encapsulates an execution and provides a reactive way how to listen for its
 * execution result, errors or state
 *
 * @param <R> type of command result
 *
 * @author dohnal
 */
public abstract class ReactiveCommand<R>
{
    protected final Supplier<R> execution;

    protected final ReactiveProperty<R> value;

    /**
     * Executes this reactive command
     */
    public abstract void execute();

    /**
     * Returns an observable of execution result
     *
     * @return observable of execution result
     */
    @Nonnull
    public Observable<R> getResult()
    {
        return value.asObservable();
    }

    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    public static ReactiveCommand<Void> create()
    {
        return create(() -> null);
    }

    /**
     * Creates a new synchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    public static <T> ReactiveCommand<T> create(final @Nonnull Supplier<T> execution)
    {
        return new SyncCommand<>(execution);
    }

    protected ReactiveCommand(final @Nonnull Supplier<R> execution)
    {
        this.execution = execution;
        this.value = ReactiveProperty.empty();
    }
}
