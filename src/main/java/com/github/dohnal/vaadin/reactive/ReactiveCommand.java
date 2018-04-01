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

    protected final ReactiveProperty<Integer> executionCount;

    protected final ReactiveProperty<Boolean> canExecute;

    /**
     * Internally executes this command
     */
    protected abstract void executeInternal();

    /**
     * Returns an observable of execution result
     *
     * @return observable of execution result
     */
    @Nonnull
    public final Observable<R> getResult()
    {
        return result.asObservable();
    }

    /**
     * Returns an observable of errors produces during execution
     *
     * @return observable of errors
     */
    @Nonnull
    public final Observable<Throwable> getError()
    {
        return error.asObservable();
    }

    /**
     * Returns an observable of whether command is executing right now
     *
     * @return an observable of whether command is executing right now
     */
    @Nonnull
    public final Observable<Boolean> isExecuting()
    {
        return isExecuting.asObservable();
    }

    /**
     * Returns an observable of number of executions for this command
     *
     * @return an observable of number of executions for this command
     */
    @Nonnull
    public final Observable<Integer> getExecutionCount()
    {
        return executionCount.asObservable();
    }

    /**
     * Returns an observable of whether command has been executed
     *
     * @return an observable of whether command has been executed
     */
    @Nonnull
    public final Observable<Boolean> hasBeenExecuted()
    {
        return getExecutionCount()
                .map(count -> count > 0)
                .distinctUntilChanged();
    }

    /**
     * Returns an observable of whether command can be executed right now
     *
     * @return an observable of whether command can be executed right now
     */
    @Nonnull
    public final Observable<Boolean> canExecute()
    {
        return canExecute.asObservable();
    }

    /**
     * Executes this reactive command
     */
    public final void execute()
    {
        if (Boolean.TRUE.equals(canExecute.getValue()))
        {
            executeInternal();
        }
    }

    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    public static ReactiveCommand<Void> create()
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
    public static ReactiveCommand<Void> create(final @Nonnull Observable<Boolean> canExecute)
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
    public static ReactiveCommand<Void> create(final @Nonnull Runnable execution)
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
    public static ReactiveCommand<Void> create(final @Nonnull Observable<Boolean> canExecute,
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
    public static <T> ReactiveCommand<T> create(final @Nonnull Supplier<T> execution)
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
    public static <T> ReactiveCommand<T> create(final @Nonnull Observable<Boolean> canExecute,
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
    public static ReactiveCommand<Void> createAsync()
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
    public static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute)
    {
        return createAsync(canExecute, () -> {});
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
    public static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
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
    public static ReactiveCommand<Void> createAsync(final @Nonnull Runnable execution,
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
    public static ReactiveCommand<Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution)
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Supplier<T> execution,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution)
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull AsyncSupplier<T> execution,
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
    public static <T> ReactiveCommand<T> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                     final @Nonnull AsyncSupplier<T> execution,
                                                     final @Nonnull Executor executor)
    {
        return new AsyncCommand<>(canExecute, execution, executor);
    }


    protected ReactiveCommand(final @Nonnull Observable<Boolean> canExecute)
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
