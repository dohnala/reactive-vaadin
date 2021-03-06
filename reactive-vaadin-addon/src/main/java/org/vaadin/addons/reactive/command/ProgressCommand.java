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
import java.util.function.BiFunction;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import org.vaadin.addons.reactive.ProgressContext;
import org.vaadin.addons.reactive.ReactiveCommand;

/**
 * Asynchronous implementation of {@link ReactiveCommand} with support of controlling progress
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class ProgressCommand<T, R> extends AbstractCommand<T, R>
{
    private final BiFunction<ProgressContext, T, Observable<R>> execution;

    private final Scheduler scheduler;

    /**
     * Creates new progress reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public ProgressCommand(final @Nonnull Observable<Boolean> canExecute,
                           final @Nonnull BiFunction<ProgressContext, T, Observable<R>> execution,
                           final @Nonnull Scheduler scheduler)
    {
        super(canExecute, Observable.empty());

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        this.execution = execution;
        this.scheduler = scheduler;
    }

    @Nonnull
    @Override
    protected Observable<R> executeInternal(final @Nonnull Optional<T> input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        return Observable.just(input)
                .subscribeOn(scheduler)
                .flatMap(this::checkCanExecute)
                .flatMap(value -> execution.apply(new ReactiveProgressContext(progress), value.orElse(null))
                        .doOnSubscribe(disposable -> handleStart(value))
                        .doOnNext(this::handleResult)
                        .doFinally(this::handleComplete))
                .onErrorResumeNext(this::handleError)
                .replay()
                .refCount();
    }
}
