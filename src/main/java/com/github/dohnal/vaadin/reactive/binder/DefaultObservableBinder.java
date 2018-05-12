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

package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ObservableBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Default implementation of {@link ObservableBinder)}
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class DefaultObservableBinder<T> extends AbstractBinder implements ObservableBinder<T>
{
    private final Observable<T> observable;

    public DefaultObservableBinder(final @Nonnull Observable<T> observable,
                                   final @Nonnull Consumer<? super Throwable> errorHandler)
    {
        super(errorHandler);

        Objects.requireNonNull(observable, "Observable cannot be null");

        this.observable = observable;
    }

    @Nonnull
    @Override
    public final Disposable then(final @Nonnull Consumer<? super T> action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return subscribeWithErrorHandler(observable, action);
    }

    @Nonnull
    @Override
    public final Disposable then(final @Nonnull Runnable action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return subscribeWithErrorHandler(observable, value -> {
            action.run();
        });
    }

    @Nonnull
    @Override
    public Disposable then(final @Nonnull Function<? super T, Observable<?>> action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return subscribeWithErrorHandler(observable, action);
    }
}
