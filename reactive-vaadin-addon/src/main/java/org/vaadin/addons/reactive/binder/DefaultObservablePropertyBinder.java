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

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import org.vaadin.addons.reactive.IsObservable;
import org.vaadin.addons.reactive.ObservableProperty;
import org.vaadin.addons.reactive.ObservablePropertyBinder;

/**
 * Default implementation of {@link ObservablePropertyBinder)}
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class DefaultObservablePropertyBinder<T> extends AbstractBinder implements ObservablePropertyBinder<T>
{
    private final ObservableProperty<T> property;

    public DefaultObservablePropertyBinder(final @Nonnull ObservableProperty<T> property,
                                           final @Nonnull Consumer<? super Throwable> errorHandler)
    {
        super(errorHandler);

        Objects.requireNonNull(property, "Property cannot be null");

        this.property = property;
    }

    @Nonnull
    @Override
    public ObservableProperty<T> getProperty()
    {
        return property;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return subscribeWithErrorHandler(observable, getProperty()::setValue);
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        Objects.requireNonNull(isObservable, "IsObservable cannot be null");

        return to(isObservable.asObservable());
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull ObservableProperty<T> anotherProperty)
    {
        Objects.requireNonNull(anotherProperty, "Another property cannot be null");

        return new ListCompositeDisposable(
                subscribeWithErrorHandler(getProperty().asObservable(), anotherProperty::setValue),
                subscribeWithErrorHandler(anotherProperty.asObservable(), getProperty()::setValue));
    }
}
