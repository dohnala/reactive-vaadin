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

package org.vaadin.addons.reactive.mvvm.binder;

import javax.annotation.Nonnull;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.vaadin.addons.reactive.IsObservable;
import org.vaadin.addons.reactive.PropertyBinder;
import org.vaadin.addons.reactive.activable.CompositeActivable;
import org.vaadin.addons.reactive.activable.SerialActivable;
import org.vaadin.addons.reactive.binder.PropertyBinderDecorator;

/**
 * Decorator which adds all bindings to given composite activable
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class ActivablePropertyBinder<T> extends PropertyBinderDecorator<T>
{
    private final CompositeActivable compositeActivable;

    public ActivablePropertyBinder(final @Nonnull CompositeActivable compositeActivable,
                                   final @Nonnull PropertyBinder<T> binder)
    {
        super(binder);

        Objects.requireNonNull(compositeActivable, "Composite activable cannot be null");

        this.compositeActivable = compositeActivable;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        final SerialActivable activable = new SerialActivable(() -> super.to(observable));

        compositeActivable.add(activable);

        return activable.asDisposable();
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        final SerialActivable activable = new SerialActivable(() -> super.to(isObservable));

        compositeActivable.add(activable);

        return activable.asDisposable();
    }
}
