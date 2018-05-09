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
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

import io.reactivex.Observable;

/**
 * @author dohnal
 */
public final class Command<T, R> extends AbstractCommand<T, R>
{
    private final Function<T, Observable<R>> execution;

    /**
     * Creates new reactive command with given observable
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public Command(final @Nonnull Observable<Boolean> canExecute,
                   final @Nonnull Function<T, Observable<R>> execution)
    {
        super(canExecute);

        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(execution, "Execution cannot be null");

        this.execution = execution;
    }

    @Override
    protected void executeInternal(final @Nullable T input)
    {
        execution.apply(input)
                .doOnSubscribe(disposable -> handleStart())
                .doFinally(this::handleComplete)
                .subscribe(this::handleResult, this::handleError);
    }
}
