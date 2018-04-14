package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
import com.github.dohnal.vaadin.reactive.command.SyncCommand;
import rx.Observable;

/**
 * Reactive command encapsulates an execution and provides a reactive way how to listen for its
 * execution result, errors or state
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public interface ReactiveCommand<T, R>
{
    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> create()
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
    static ReactiveCommand<Void, Void> create(final @Nonnull Observable<Boolean> canExecute)
    {
        return create(canExecute, () -> null);
    }

    /**
     * Creates a new synchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> create(final @Nonnull Runnable execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> create(final @Nonnull Observable<Boolean> canExecute,
                                              final @Nonnull Runnable execution)
    {
        return create(canExecute, () -> {
            execution.run();

            return null;
        });
    }

    /**
     * Creates a new synchronous reactive command from given supplier
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> create(final @Nonnull Supplier<R> execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> create(final @Nonnull Observable<Boolean> canExecute,
                                               final @Nonnull Supplier<R> execution)
    {
        return create(canExecute, input -> {
            return execution.get();
        });
    }

    /**
     * Creates a new synchronous reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> create(final @Nonnull Consumer<T> execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> create(final @Nonnull Observable<Boolean> canExecute,
                                               final @Nonnull Consumer<T> execution)
    {
        return create(canExecute, input -> {
            execution.accept(input);

            return null;
        });
    }

    /**
     * Creates a new synchronous reactive command from given function
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> create(final @Nonnull Function<T, R> execution)
    {
        return create(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> create(final @Nonnull Observable<Boolean> canExecute,
                                               final @Nonnull Function<T, R> execution)
    {
        return new SyncCommand<>(canExecute, execution);
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync()
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
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Observable<Boolean> canExecute)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create());
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), executor);
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                   final @Nonnull Executor executor)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create(executor), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Runnable execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                   final @Nonnull Runnable execution)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Runnable execution,
                                                   final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                   final @Nonnull Runnable execution,
                                                   final @Nonnull Executor executor)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given supplier
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createAsync(final @Nonnull Supplier<R> execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Supplier<R> execution)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given supplier
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createAsync(final @Nonnull Supplier<R> execution,
                                                    final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Supplier<R> execution,
                                                    final @Nonnull Executor executor)
    {
        return createFromAsyncSupplier(canExecute, AsyncSupplier.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createAsync(final @Nonnull Consumer<T> execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Consumer<T> execution)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createAsync(final @Nonnull Consumer<T> execution,
                                                    final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Consumer<T> execution,
                                                    final @Nonnull Executor executor)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given function
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createAsync(final @Nonnull Function<T, R> execution)
    {
        return createAsync(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Function<T, R> execution)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given function
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createAsync(final @Nonnull Function<T, R> execution,
                                                    final @Nonnull Executor executor)
    {
        return createAsync(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createAsync(final @Nonnull Observable<Boolean> canExecute,
                                                    final @Nonnull Function<T, R> execution,
                                                    final @Nonnull Executor executor)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createProgress(final @Nonnull Consumer<Progress> execution)
    {
        return createProgress(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                      final @Nonnull Consumer<Progress> execution)
    {
        return createFromAsyncProgressSupplier(canExecute, AsyncProgressSupplier.create(execution));
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createProgress(final @Nonnull Consumer<Progress> execution,
                                                      final @Nonnull Executor executor)
    {
        return createProgress(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    static ReactiveCommand<Void, Void> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                      final @Nonnull Consumer<Progress> execution,
                                                      final @Nonnull Executor executor)
    {
        return createFromAsyncProgressSupplier(canExecute, AsyncProgressSupplier.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createProgress(final @Nonnull Function<Progress, R> execution)
    {
        return createProgress(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Function<Progress, R> execution)
    {
        return createFromAsyncProgressSupplier(canExecute, AsyncProgressSupplier.create(execution));
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createProgress(final @Nonnull Function<Progress, R> execution,
                                                       final @Nonnull Executor executor)
    {
        return createProgress(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Function<Progress, R> execution,
                                                       final @Nonnull Executor executor)
    {
        return createFromAsyncProgressSupplier(canExecute, AsyncProgressSupplier.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createProgress(final @Nonnull BiConsumer<Progress, T> execution)
    {
        return createProgress(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull BiConsumer<Progress, T> execution)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution));
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createProgress(final @Nonnull BiConsumer<Progress, T> execution,
                                                       final @Nonnull Executor executor)
    {
        return createProgress(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    static <T> ReactiveCommand<T, Void> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull BiConsumer<Progress, T> execution,
                                                       final @Nonnull Executor executor)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createProgress(final @Nonnull BiFunction<Progress, T, R> execution)
    {
        return createProgress(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull BiFunction<Progress, T, R> execution)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution));
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createProgress(final @Nonnull BiFunction<Progress, T, R> execution,
                                                       final @Nonnull Executor executor)
    {
        return createProgress(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createProgress(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull BiFunction<Progress, T, R> execution,
                                                       final @Nonnull Executor executor)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution, executor), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous supplier
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncSupplier(final @Nonnull AsyncSupplier<R> execution)
    {
        return createFromAsyncSupplier(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncSupplier(final @Nonnull Observable<Boolean> canExecute,
                                                                final @Nonnull AsyncSupplier<R> execution)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous supplier
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncSupplier(final @Nonnull AsyncSupplier<R> execution,
                                                                final @Nonnull Executor executor)
    {
        return createFromAsyncSupplier(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncSupplier(final @Nonnull Observable<Boolean> canExecute,
                                                                final @Nonnull AsyncSupplier<R> execution,
                                                                final @Nonnull Executor executor)
    {
        return createFromAsyncFunction(canExecute, AsyncFunction.create(execution), executor);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous function
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncFunction(final @Nonnull AsyncFunction<T, R> execution)
    {
        return createFromAsyncFunction(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncFunction(final @Nonnull Observable<Boolean> canExecute,
                                                                final @Nonnull AsyncFunction<T, R> execution)
    {
        return new AsyncCommand<>(canExecute, execution);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous function
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncFunction(final @Nonnull AsyncFunction<T, R> execution,
                                                                final @Nonnull Executor executor)
    {
        return createFromAsyncFunction(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous reactive command from given asynchronous function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncFunction(final @Nonnull Observable<Boolean> canExecute,
                                                                final @Nonnull AsyncFunction<T, R> execution,
                                                                final @Nonnull Executor executor)
    {
        return new AsyncCommand<>(canExecute, execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress supplier
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncProgressSupplier(final @Nonnull AsyncProgressSupplier<R> execution)
    {
        return createFromAsyncProgressSupplier(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncProgressSupplier(final @Nonnull Observable<Boolean> canExecute,
                                                                        final @Nonnull AsyncProgressSupplier<R> execution)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution));
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress supplier
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncProgressSupplier(final @Nonnull AsyncProgressSupplier<R> execution,
                                                                        final @Nonnull Executor executor)
    {
        return createFromAsyncProgressSupplier(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <R> ReactiveCommand<Void, R> createFromAsyncProgressSupplier(final @Nonnull Observable<Boolean> canExecute,
                                                                        final @Nonnull AsyncProgressSupplier<R> execution,
                                                                        final @Nonnull Executor executor)
    {
        return createFromAsyncProgressFunction(canExecute, AsyncProgressFunction.create(execution), executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress function
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncProgressFunction(final @Nonnull AsyncProgressFunction<T, R> execution)
    {
        return createFromAsyncProgressFunction(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncProgressFunction(final @Nonnull Observable<Boolean> canExecute,
                                                                        final @Nonnull AsyncProgressFunction<T, R> execution)
    {
        return new ProgressCommand<>(canExecute, execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress function
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncProgressFunction(final @Nonnull AsyncProgressFunction<T, R> execution,
                                                                        final @Nonnull Executor executor)
    {
        return createFromAsyncProgressFunction(Observable.just(true), execution, executor);
    }

    /**
     * Creates a new asynchronous progress reactive command from given asynchronous progress function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    static <T, R> ReactiveCommand<T, R> createFromAsyncProgressFunction(final @Nonnull Observable<Boolean> canExecute,
                                                                        final @Nonnull AsyncProgressFunction<T, R> execution,
                                                                        final @Nonnull Executor executor)
    {
        return new ProgressCommand<>(canExecute, execution, executor);
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
     * Returns an observable of command execution progress if it is currently executing
     *
     * @return an observable of command execution progress if it is currently executing
     */
    @Nonnull
    Observable<Float> getProgress();

    /**
     * Executes this reactive command
     */
    void execute(final @Nullable T input);
}
