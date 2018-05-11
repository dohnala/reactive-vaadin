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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * @author dohnal
 */
public final class Command<T, R> extends AbstractCommand<T, R>
{
    private final Function<T, Observable<R>> execution;

    private final Scheduler scheduler;

    /**
     * Creates new reactive command with given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public Command(final @Nonnull Observable<Boolean> canExecute,
                   final @Nonnull Function<T, Observable<R>> execution,
                   final @Nonnull Scheduler scheduler)
    {
        this(canExecute, Observable.empty(), execution, scheduler);
    }

    /**
     * Creates new reactive command with given observable
     *
     * @param canExecute observable which controls command executability
     * @param customProgress observable which controls command progress
     * @param execution execution
     */
    Command(final @Nonnull Observable<Boolean> canExecute,
            final @Nonnull Observable<Float> customProgress,
            final @Nonnull Function<T, Observable<R>> execution,
            final @Nonnull Scheduler scheduler)
    {
        super(canExecute, customProgress);

        Objects.requireNonNull(execution, "Execution cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        this.execution = execution;
        this.scheduler = scheduler;
    }

    @Nonnull
    @Override
    protected Observable<R> executeInternal(final @Nonnull Optional<T> input)
    {
        return Observable.just(input)
                .subscribeOn(scheduler)
                .flatMap(this::checkCanExecute)
                .doOnNext(this::handleStart)
                .flatMap(value -> execution.apply(value.orElse(null))
                        .doOnNext(this::handleResult)
                        .doFinally(this::handleComplete))
                .onErrorResumeNext(this::handleError)
                .replay()
                .refCount();
    }
}
