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
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ObservableBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Decorator for {@link ObservableBinder} which can be extended to provide behavior extensions
 * to given binder
 *
 * @param <T> type of value
 * @author dohnal
 */
public class ObservableBinderDecorator<T> implements ObservableBinder<T>
{
    private final ObservableBinder<T> binder;

    /**
     * Creates new decorator for given binder to extend its behavior
     *
     * @param binder binder to extend
     */
    public ObservableBinderDecorator(final @Nonnull ObservableBinder<T> binder)
    {
        Objects.requireNonNull(binder, "Binder cannot be null");

        this.binder = binder;
    }

    @Nonnull
    @Override
    public Observable<T> getObservable()
    {
        return binder.getObservable();
    }

    @Nonnull
    @Override
    public Disposable then(final @Nonnull Runnable action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return binder.then(action);
    }

    @Nonnull
    @Override
    public Disposable then(final @Nonnull Consumer<? super T> action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return binder.then(action);
    }

    @Nonnull
    @Override
    public Disposable then(final @Nonnull Supplier<Observable<?>> action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return binder.then(action);
    }

    @Nonnull
    @Override
    public Disposable then(final @Nonnull Function<? super T, Observable<?>> action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        return binder.then(action);
    }
}
