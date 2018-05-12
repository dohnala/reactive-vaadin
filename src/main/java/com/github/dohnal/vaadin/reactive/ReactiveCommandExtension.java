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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.command.Command;
import com.github.dohnal.vaadin.reactive.command.CompositeCommand;
import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Extension to create instances of {@link ReactiveCommand}
 *
 * @author dohnal
 */
public interface ReactiveCommandExtension
{
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
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand(final @Nonnull Runnable execution,
                                                      final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommand(Observable.just(true), execution, scheduler);
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

        return createCommand(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given runnable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                      final @Nonnull Runnable execution,
                                                      final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(canExecute, () ->
                Completable.fromRunnable(execution).toObservable(), scheduler);
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
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommand(final @Nonnull Supplier<R> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommand(Observable.just(true), execution, scheduler);
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

        return createCommand(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given supplier
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Supplier<R> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(canExecute, () -> Observable.fromCallable(() ->
                Objects.requireNonNull(execution.get(), "Result cannot be null")), scheduler);
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
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createCommand(final @Nonnull Consumer<T> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommand(Observable.just(true), execution, scheduler);
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

        return createCommand(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Consumer<T> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(canExecute, input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Completable.fromRunnable(() -> execution.accept(input)).toObservable();
        }, scheduler);
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
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommand(final @Nonnull Function<T, R> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommand(Observable.just(true), execution, scheduler);
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

        return createCommand(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommand(final @Nonnull Observable<Boolean> canExecute,
                                                       final @Nonnull Function<T, R> execution,
                                                       final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(canExecute, input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Observable.fromCallable(() ->
                    Objects.requireNonNull(execution.apply(input), "Result cannot be null"));
        }, scheduler);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommandFromObservable(final @Nonnull Supplier<Observable<R>> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommandFromObservable(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommandFromObservable(final @Nonnull Supplier<Observable<R>> execution,
                                                                     final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommandFromObservable(final @Nonnull Observable<Boolean> canExecute,
                                                                     final @Nonnull Supplier<Observable<R>> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommandFromObservable(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createCommandFromObservable(final @Nonnull Observable<Boolean> canExecute,
                                                                     final @Nonnull Supplier<Observable<R>> execution,
                                                                     final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return new Command<>(canExecute, input ->
                Objects.requireNonNull(execution.get(), "Observable cannot be null"), scheduler);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommandFromObservable(final @Nonnull Function<T, Observable<R>> execution)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommandFromObservable(Observable.just(true), execution);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommandFromObservable(final @Nonnull Function<T, Observable<R>> execution,
                                                                     final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCommandFromObservable(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommandFromObservable(final @Nonnull Observable<Boolean> canExecute,
                                                                     final @Nonnull Function<T, Observable<R>> execution)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        return createCommandFromObservable(canExecute, execution, Schedulers.trampoline());
    }

    /**
     * Creates a new synchronous reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createCommandFromObservable(final @Nonnull Observable<Boolean> canExecute,
                                                                     final @Nonnull Function<T, Observable<R>> execution,
                                                                     final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return new Command<>(canExecute, input -> {
            Objects.requireNonNull(input, "Input cannot be null");

            return Objects.requireNonNull(execution.apply(input), "Observable cannot be null");
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Consumer<ProgressContext> execution,
                                                              final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommand(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @return created reactive command
     */
    @Nonnull
    default ReactiveCommand<Void, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                              final @Nonnull Consumer<ProgressContext> execution,
                                                              final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(canExecute, progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            return Completable.fromRunnable(() -> execution.accept(progressContext)).toObservable();
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Function<ProgressContext, R> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommand(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull Function<ProgressContext, R> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(canExecute, progressContext -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            return Observable.fromCallable(() ->
                    Objects.requireNonNull(execution.apply(progressContext), "Result cannot be null"));
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull BiConsumer<ProgressContext, T> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommand(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given consumer
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @return created reactive command
     */
    @Nonnull
    default <T> ReactiveCommand<T, Void> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiConsumer<ProgressContext, T> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(canExecute, (progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input context cannot be null");

            return Completable.fromRunnable(() -> execution.accept(progressContext, input)).toObservable();
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull BiFunction<ProgressContext, T, R> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommand(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given function
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                                                               final @Nonnull BiFunction<ProgressContext, T, R> execution,
                                                               final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(canExecute, (progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input context cannot be null");

            return Observable.fromCallable(() ->
                    Objects.requireNonNull(execution.apply(progressContext, input), "Result cannot be null"));
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createProgressCommandFromObservable(
            final @Nonnull Function<ProgressContext, Observable<R>> execution,
            final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <R> ReactiveCommand<Void, R> createProgressCommandFromObservable(
            final @Nonnull Observable<Boolean> canExecute,
            final @Nonnull Function<ProgressContext, Observable<R>> execution,
            final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return new ProgressCommand<>(canExecute, (progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext), "Observable cannot be null");
        }, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given observable
     *
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createProgressCommandFromObservable(
            final @Nonnull BiFunction<ProgressContext, T, Observable<R>> execution,
            final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createProgressCommandFromObservable(Observable.just(true), execution, scheduler);
    }

    /**
     * Creates a new asynchronous progress reactive command from given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution which will be executed
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of command input
     * @param <R> type of command result
     * @return created reactive command
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, R> createProgressCommandFromObservable(
            final @Nonnull Observable<Boolean> canExecute,
            final @Nonnull BiFunction<ProgressContext, T, Observable<R>> execution,
            final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return new ProgressCommand<>(canExecute, (progressContext, input) -> {
            Objects.requireNonNull(progressContext, "Progress context cannot be null");
            Objects.requireNonNull(input, "Input context cannot be null");

            return Objects.requireNonNull(execution.apply(progressContext, input), "Observable cannot be null");
        }, scheduler);
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
     * @param commands commands to compose
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of commands input
     * @param <R> type of commands result
     * @return created composite reactive commands
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, List<R>> createCompositeCommand(final @Nonnull List<ReactiveCommand<T, R>> commands,
                                                                      final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(commands, "Commands cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return createCompositeCommand(Observable.just(true), commands, scheduler);
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

        return createCompositeCommand(canExecute, commands, Schedulers.trampoline());
    }

    /**
     * Creates a new composite command composed of given commands
     *
     * @param canExecute observable which controls command executability
     * @param commands commands to compose
     * @param scheduler scheduler used to schedule execution
     * @param <T> type of commands input
     * @param <R> type of commands result
     * @return created composite reactive commands
     */
    @Nonnull
    default <T, R> ReactiveCommand<T, List<R>> createCompositeCommand(final @Nonnull Observable<Boolean> canExecute,
                                                                      final @Nonnull List<ReactiveCommand<T, R>> commands,
                                                                      final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(commands, "Commands cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        return new CompositeCommand<>(canExecute, commands, scheduler);
    }
}
