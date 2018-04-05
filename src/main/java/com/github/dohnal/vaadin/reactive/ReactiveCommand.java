package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
import com.github.dohnal.vaadin.reactive.command.SyncCommand;
import rx.Observable;

/**
 * Reactive command encapsulates an execution and provides a reactive way how to listen for its
 * execution result, errors or state
 *
 * @param <R> type of command result
 * @author dohnal
 */
public interface ReactiveCommand<R>
{
    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> create()
    {
        return create(Observable.just(true));
    }

    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> create(final @Nonnull Observable<Boolean> canExecute)
    {
        return create(canExecute, () -> null);
    }

    /**
     * Creates a new synchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> create(final @Nonnull Runnable execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> create(final @Nonnull Observable<Boolean> canExecute,
                                        final @Nonnull Runnable execution)
    {
        return create(canExecute, () -> {
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
    static <T> ReactiveCommand<T> create(final @Nonnull Supplier<T> execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> create(final @Nonnull Observable<Boolean> canExecute,
                                         final @Nonnull Supplier<T> execution)
    {
        return new SyncCommand<>(canExecute, execution);
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> createAsync()
    {
        return createAsync(Observable.just(true));
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute)
    {
        return createAsync(canExecute, () -> {
        });
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> createAsync(final @Nonnull Runnable execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                             final @Nonnull Runnable execution)
    {
        return createAsync(canExecute, () -> CompletableFuture.supplyAsync(() -> {
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
    static ReactiveCommand<Void> createAsync(final @Nonnull Runnable execution,
                                             final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), () -> CompletableFuture.supplyAsync(() -> {
            execution.run();

            return null;
        }, executor));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                             final @Nonnull Runnable execution,
                                             final @Nonnull Executor executor)
    {
        return createAsync(canExecute, () -> CompletableFuture.supplyAsync(() -> {
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
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution)
    {
        return createAsync(Observable.just(true), () -> CompletableFuture.supplyAsync(execution));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                              final @Nonnull Supplier<T> execution)
    {
        return createAsync(canExecute, () -> CompletableFuture.supplyAsync(execution));
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
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution,
                                              final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), () -> CompletableFuture.supplyAsync(execution, executor));
    }

    /**
     * Creates a new asynchronous reactive command with given execution and executor
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                              final @Nonnull Supplier<T> execution,
                                              final @Nonnull Executor executor)
    {
        return createAsync(canExecute, () -> CompletableFuture.supplyAsync(execution, executor));
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                              final @Nonnull AsyncSupplier<T> execution)
    {
        return new AsyncCommand<>(canExecute, execution);
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
    static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution,
                                              final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command with given execution and executor
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                              final @Nonnull AsyncSupplier<T> execution,
                                              final @Nonnull Executor executor)
    {
        return new AsyncCommand<>(canExecute, execution, executor);
    }

    /**
     * Returns an observable of execution result
     *
     * @return observable of execution result
     */
    @Nonnull
    Observable<R> getResult();

    /**
     * Returns an observable of errors produces during execution
     *
     * @return observable of errors
     */
    @Nonnull
    Observable<Throwable> getError();

    /**
     * Returns an observable of whether command is executing right now
     *
     * @return an observable of whether command is executing right now
     */
    @Nonnull
    Observable<Boolean> isExecuting();

    /**
     * Returns an observable of number of executions for this command
     *
     * @return an observable of number of executions for this command
     */
    @Nonnull
    Observable<Integer> getExecutionCount();

    /**
     * Returns an observable of whether command has been executed
     *
     * @return an observable of whether command has been executed
     */
    @Nonnull
    Observable<Boolean> hasBeenExecuted();

    /**
     * Returns an observable of whether command can be executed right now
     *
     * @return an observable of whether command can be executed right now
     */
    @Nonnull
    Observable<Boolean> canExecute();

    /**
     * Executes this reactive command
     */
    void execute();
}
