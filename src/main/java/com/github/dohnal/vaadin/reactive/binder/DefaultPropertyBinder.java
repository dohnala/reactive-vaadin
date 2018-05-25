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

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Default implementation of {@link PropertyBinder)}
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class DefaultPropertyBinder<T> extends AbstractBinder implements PropertyBinder<T>
{
    private final Property<T> property;

    public DefaultPropertyBinder(final @Nonnull Property<T> property,
                                 final @Nonnull Consumer<? super Throwable> errorHandler)
    {
        super(errorHandler);

        Objects.requireNonNull(property, "Property cannot be null");

        this.property = property;
    }

    @Nonnull
    @Override
    public Property<T> getProperty()
    {
        return property;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return subscribeWithErrorHandler(observable, property::setValue);
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        Objects.requireNonNull(isObservable, "IsObservable cannot be null");

        return to(isObservable.asObservable());
    }
}
