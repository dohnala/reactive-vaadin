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

package com.github.dohnal.vaadin.mvvm.binder;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import com.github.dohnal.vaadin.reactive.activables.CompositeActivable;
import com.github.dohnal.vaadin.reactive.activables.SerialActivable;
import com.github.dohnal.vaadin.reactive.binder.PropertyBinderDecorator;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Decorator which adds all bindings to given composite activable
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class ActivablePropertyBinder<T> extends PropertyBinderDecorator<T>
{
    private CompositeActivable compositeActivable;

    public ActivablePropertyBinder(final @Nonnull PropertyBinder<T> binder,
                                   final @Nonnull CompositeActivable compositeActivable)
    {
        super(binder);

        Objects.requireNonNull(compositeActivable, "Composite activable cannot be null");

        this.compositeActivable = compositeActivable;
    }

    @Nonnull
    @Override
    public Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        final SerialActivable activable = new SerialActivable(() -> super.to(observable));

        compositeActivable.add(activable);

        return activable.asDisposable();
    }

    @Nonnull
    @Override
    public Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        final SerialActivable activable = new SerialActivable(() -> super.to(isObservable));

        compositeActivable.add(activable);

        return activable.asDisposable();
    }
}
