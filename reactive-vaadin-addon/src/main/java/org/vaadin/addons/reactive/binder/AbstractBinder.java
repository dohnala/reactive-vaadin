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

package org.vaadin.addons.reactive.binder;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;

/**
 * Base class for binder implementations
 *
 * @author dohnal
 */
public abstract class AbstractBinder
{
    protected final Consumer<? super Throwable> errorHandler;

    /**
     * Creates new binder with given error handler
     *
     * @param errorHandler error handler
     */
    public AbstractBinder(final @Nonnull Consumer<? super Throwable> errorHandler)
    {
        Objects.requireNonNull(errorHandler, "Error handler cannot be null");

        this.errorHandler = errorHandler;
    }

    @Nonnull
    protected <T> Disposable subscribeWithErrorHandler(final @Nonnull Observable<? extends T> observable,
                                                       final @Nonnull Consumer<? super T> action)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");

        return observable
                .flatMap(value -> Completable
                        .fromRunnable(() -> action.accept(value))
                        .doOnError(errorHandler::accept)
                        .onErrorComplete()
                        .toObservable())
                .ignoreElements()
                .subscribe(Functions.EMPTY_ACTION, errorHandler::accept);
    }

    @Nonnull
    protected <T> Disposable subscribeWithErrorHandler(final @Nonnull Observable<? extends T> observable,
                                                       final @Nonnull Function<? super T, Observable<?>> action)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");

        return Observable.switchOnNext(observable
                .map(value -> action.apply(value)
                        .ignoreElements()
                        .doOnError(errorHandler::accept)
                        .onErrorComplete()
                        .toObservable()))
                .ignoreElements()
                .subscribe(Functions.EMPTY_ACTION, errorHandler::accept);
    }
}
