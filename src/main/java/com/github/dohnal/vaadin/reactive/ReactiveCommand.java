package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
import com.github.dohnal.vaadin.reactive.command.AsyncSupplier;
import com.github.dohnal.vaadin.reactive.command.SyncCommand;
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
    protected final ReactiveProperty<R> result;

    protected final ReactiveProperty<Throwable> error;

    protected final ReactiveProperty<Boolean> isExecuting;

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
        return result.asObservable();
    }

    /**
     * Returns an observable of errors produces during execution
     *
     * @return observable of errors
     */
    @Nonnull
    public Observable<Throwable> getError()
    {
        return error.asObservable();
    }

    /**
     * Returns an observable of whether command is executing right now
     *
     * @return an observable of whether command is executing right now
     */
    @Nonnull
    public Observable<Boolean> isExecuting()
    {
        return isExecuting.asObservable();
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
     * @return created reactive command
     */
    @Nonnull
    public static ReactiveCommand<Void> create(final @Nonnull Runnable execution)
    {
        return create(() -> {
            execution.run();

            return null;
        });
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

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    public static ReactiveCommand<Void> createAsync(final @Nonnull Runnable execution)
    {
        return createAsync(() -> CompletableFuture.supplyAsync(() -> {
            execution.run();

            return null;
        }));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    public static ReactiveCommand<Void> createAsync(final @Nonnull Runnable execution,
                                                    final @Nonnull Executor executor)
    {
        return createAsync(() -> CompletableFuture.supplyAsync(() -> {
            execution.run();

            return null;
        }, executor));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution)
    {
        return createAsync(() -> CompletableFuture.supplyAsync(execution));
    }

    /**
     * Creates a new asynchronous reactive command with given execution and executor
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution,
                                                     final @Nonnull Executor executor)
    {
        return createAsync(() -> CompletableFuture.supplyAsync(execution, executor));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution)
    {
        return new AsyncCommand<>(execution);
    }

    /**
     * Creates a new asynchronous reactive command with given execution and executor
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution,
                                                     final @Nonnull Executor executor)
    {
        return new AsyncCommand<>(execution, executor);
    }

    protected ReactiveCommand()
    {
        this.result = ReactiveProperty.empty();
        this.error = ReactiveProperty.empty();
        this.isExecuting = ReactiveProperty.withValue(false);
    }

    protected void handleResult(final @Nullable R result, final @Nullable Throwable error)
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
    }
}
