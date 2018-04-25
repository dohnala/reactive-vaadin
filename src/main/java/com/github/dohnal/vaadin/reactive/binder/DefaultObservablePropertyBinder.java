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
import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import rx.Observable;
import rx.Subscription;

/**
 * Default implementation of {@link ObservablePropertyBinder)}
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class DefaultObservablePropertyBinder<T> implements ObservablePropertyBinder<T>
{
    private final ObservableProperty<T> property;

    public DefaultObservablePropertyBinder(final @Nonnull ObservableProperty<T> property)
    {
        this.property = property;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull Observable<? extends T> observable)
    {
        @SuppressWarnings("Convert2MethodRef")
        final Subscription subscription = observable.subscribe(value -> property.setValue(value));

        return subscription::unsubscribe;
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull IsObservable<? extends T> isObservable)
    {
        return to(isObservable.asObservable());
    }

    @Nonnull
    @Override
    public final Disposable to(final @Nonnull ObservableProperty<T> anotherProperty)
    {
        final Subscription propertySubscription = property.asObservable()
                .subscribe(anotherProperty::setValue);

        @SuppressWarnings("Convert2MethodRef")
        final Subscription anotherPropertySubscription = anotherProperty.asObservable()
                .subscribe(value -> property.setValue(value));

        return () -> {
            propertySubscription.unsubscribe();
            anotherPropertySubscription.unsubscribe();
        };
    }
}
