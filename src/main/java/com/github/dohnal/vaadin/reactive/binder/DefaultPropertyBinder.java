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

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import rx.Observable;
import rx.Subscription;

/**
 * Default implementation of {@link PropertyBinder)}
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class DefaultPropertyBinder<T> implements PropertyBinder<T>
{
    private final Property<T> property;

    public DefaultPropertyBinder(final @Nonnull Property<T> property)
    {
        this.property = property;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        final Subscription subscription = observable.subscribe(property::setValue);

        return subscription::unsubscribe;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        return to(isObservable.asObservable());
    }
}
