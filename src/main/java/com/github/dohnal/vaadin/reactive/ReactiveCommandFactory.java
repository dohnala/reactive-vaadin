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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.command.AsyncCommand;
import com.github.dohnal.vaadin.reactive.command.CompositeCommand;
import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
import com.github.dohnal.vaadin.reactive.command.SyncCommand;
import io.reactivex.Observable;

/**
 * Factory to create instances of {@link ReactiveCommand}
 *
 * @author dohnal
 */
public interface ReactiveCommandFactory
{
    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand()
    {
        return createCommand(Observable.just(true));
    }

    /**
     * Creates a new synchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand(final @Nonnull Observable<Boolean> canExecute)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");

        return createCommand(canExecute, () -> {});
    }

    /**
     * Creates a new synchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand(final @Nonnull Runnable execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommand(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                      final @Nonnull Runnable execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new SyncCommand<>(canExecute, () -> {
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
    default <R> ReactiveCommand<Void, R> createCommand(final @Nonnull Supplier<R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommand(Observable.just(true), execution);
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
    default <R> ReactiveCommand<Void, R> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Supplier<R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new SyncCommand<>(canExecute, () ->
                Objects.requireNonNull(execution.get(), "Result cannot be null"));
    }

    /**
     * Creates a new synchronous reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createCommand(final @Nonnull Consumer<T> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommand(Observable.just(true), execution);
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
    default <T> ReactiveCommand<T, Void> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Consumer<T> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new SyncCommand<>(canExecute, input -> {
            execution.accept(Objects.requireNonNull(input, "Input cannot be null"));

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
    default <T, R> ReactiveCommand<T, R> createCommand(final @Nonnull Function<T, R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommand(Observable.just(true), execution);
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
    default <T, R> ReactiveCommand<T, R> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Function<T, R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new SyncCommand<>(canExecute, input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(input), "Result cannot be null");
        });
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand()
    {
        return createAsyncCommand(Observable.just(true));
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create());
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Executor executor)
    {
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createAsyncCommand(Observable.just(true), executor);
    }

    /**
     * Creates a new asynchronous reactive command with no execution
     *
     * @param canExecute observable which controls command executability
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                           final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create(executor));
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Runnable execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createAsyncCommand(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                           final @Nonnull Runnable execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create(execution));
    }

    /**
     * Creates a new asynchronous reactive command from given runnable
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Runnable execution,
                                                           final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createAsyncCommand(Observable.just(true), execution, executor);
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
    default ReactiveCommand<Void, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                           final @Nonnull Runnable execution,
                                                           final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create(execution, executor));
    }

    /**
     * Creates a new asynchronous reactive command from given supplier
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createAsyncCommand(final @Nonnull Supplier<R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createAsyncCommand(Observable.just(true), execution);
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
    default <R> ReactiveCommand<Void, R> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Supplier<R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create(() ->
                Objects.requireNonNull(execution.get(), "Result cannot be null")));
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
    default <R> ReactiveCommand<Void, R> createAsyncCommand(final @Nonnull Supplier<R> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createAsyncCommand(Observable.just(true), execution, executor);
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
    default <R> ReactiveCommand<Void, R> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Supplier<R> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new AsyncCommand<>(canExecute, AsyncSupplier.create(() ->
                Objects.requireNonNull(execution.get(), "Result cannot be null"), executor));
    }

    /**
     * Creates a new asynchronous reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createAsyncCommand(final @Nonnull Consumer<T> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createAsyncCommand(Observable.just(true), execution);
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
    default <T> ReactiveCommand<T, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Consumer<T> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new AsyncCommand<>(canExecute, AsyncFunction.create(input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            execution.accept(input);
        }));
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
    default <T> ReactiveCommand<T, Void> createAsyncCommand(final @Nonnull Consumer<T> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createAsyncCommand(Observable.just(true), execution, executor);
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
    default <T> ReactiveCommand<T, Void> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Consumer<T> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new AsyncCommand<>(canExecute, AsyncFunction.create(input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            execution.accept(input);
        }, executor));
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
    default <T, R> ReactiveCommand<T, R> createAsyncCommand(final @Nonnull Function<T, R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createAsyncCommand(Observable.just(true), execution);
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
    default <T, R> ReactiveCommand<T, R> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Function<T, R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new AsyncCommand<>(canExecute, AsyncFunction.create(input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(input), "Result cannot be null");
        }));
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
    default <T, R> ReactiveCommand<T, R> createAsyncCommand(final @Nonnull Function<T, R> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createAsyncCommand(Observable.just(true), execution, executor);
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
    default <T, R> ReactiveCommand<T, R> createAsyncCommand(final @Nonnull Observable<Boolean> canExecute,
                                                            final @Nonnull Function<T, R> execution,
                                                            final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new AsyncCommand<>(canExecute, AsyncFunction.create(input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(input), "Result cannot be null");
        }, executor));
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Consumer<ProgressContext> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createProgressCommand(Observable.just(true), execution);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                              final @Nonnull Consumer<ProgressContext> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressSupplier.create(progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            execution.accept(progressContext);
        }));
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param executor executor where the execution will be executed
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Consumer<ProgressContext> execution,
                                                              final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createProgressCommand(Observable.just(true), execution, executor);
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
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                              final @Nonnull Consumer<ProgressContext> execution,
                                                              final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressSupplier.create(progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            execution.accept(progressContext);
        }, executor));
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Function<ProgressContext, R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createProgressCommand(Observable.just(true), execution);
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
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull Function<ProgressContext, R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressSupplier.create(progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext), "Result cannot be null");
        }));
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
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Function<ProgressContext, R> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createProgressCommand(Observable.just(true), execution, executor);
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
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull Function<ProgressContext, R> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressSupplier.create(progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext), "Result cannot be null");
        }, executor));
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull BiConsumer<ProgressContext, T> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createProgressCommand(Observable.just(true), execution);
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
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiConsumer<ProgressContext, T> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressFunction.create((progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input cannot be null");

            execution.accept(progressContext, input);
        }));
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
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull BiConsumer<ProgressContext, T> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createProgressCommand(Observable.just(true), execution, executor);
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
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiConsumer<ProgressContext, T> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressFunction.create((progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input cannot be null");

            execution.accept(progressContext, input);
        }, executor));
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
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull BiFunction<ProgressContext, T, R> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createProgressCommand(Observable.just(true), execution);
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
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiFunction<ProgressContext, T, R> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressFunction.create((progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext, input), "Result cannot be null");
        }));
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
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull BiFunction<ProgressContext, T, R> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return createProgressCommand(Observable.just(true), execution, executor);
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
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiFunction<ProgressContext, T, R> execution,
                                                               final @Nonnull Executor executor)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");

        return new ProgressCommand<>(canExecute, AsyncProgressFunction.create((progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext, input), "Result cannot be null");
        }, executor));
    }

    /**
     * Creates a new composite command composed of given commands
     *
     * @param commands commands to compose
     * @param <T> type of commands input
     * @param <R> type of commands result
     * @return created composite reactive commands
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, List<R>> createCompositeCommand(final @Nonnull List<ReactiveCommand<T, R>> commands)
    {
        Objects.requireNonNull(commands, "Commands cannot be null");

        return createCompositeCommand(Observable.just(true), commands);
    }

    /**
     * Creates a new composite command composed of given commands
     *
     * @param canExecute observable which controls command executability
     * @param commands commands to compose
     * @param <T> type of commands input
     * @param <R> type of commands result
     * @return created composite reactive commands
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, List<R>> createCompositeCommand(final @Nonnull Observable<Boolean> canExecute,
                                                                      final @Nonnull List<ReactiveCommand<T, R>> commands)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(commands, "Commands cannot be null");

        return new CompositeCommand<>(canExecute, commands);
    }
}
