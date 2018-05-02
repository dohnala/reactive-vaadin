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

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Decorator for {@link PropertyBinder} which can be extended to provide behavior extensions
 * to given binder
 *
 * @param <T> type of value
 * @author dohnal
 */
public class PropertyBinderDecorator<T> implements PropertyBinder<T>
{
    private final PropertyBinder<T> binder;

    /**
     * Creates new decorator for given binder to extend its behavior
     *
     * @param binder binder to extend
     */
    public PropertyBinderDecorator(final @Nonnull PropertyBinder<T> binder)
    {
        Objects.requireNonNull(binder, "Binder cannot be null");

        this.binder = binder;
    }

    @Nonnull
    @Override
    public Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return binder.to(observable);
    }

    @Nonnull
    @Override
    public Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        Objects.requireNonNull(isObservable, "IsObservable cannot be null");

        return binder.to(isObservable);
    }
}
