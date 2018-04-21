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
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.mvvm.ViewBinder;
import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import rx.Subscription;

/**
 * Implementation of {@link ViewBinder} for two way binding of properties which are also observables
 *
 * @param <T> type of value in the event
 * @author dohnal
 */
public class ViewPropertyBinder<T, U extends Property<T> & IsObservable<T>>
        implements Disposable<ViewPropertyBinder<T, U>>
{
    private final U property;

    private List<Subscription> subscriptions;

    public ViewPropertyBinder(final @Nonnull U property)
    {
        this.property = property;
        this.subscriptions = new ArrayList<>();
    }

    /**
     * Binds this property to given property in two way manner
     *
     * @param anotherProperty another property
     * @param <V> type of property
     * @return this binder
     */
    @Nonnull
    public <V extends Property<T> & IsObservable<T>> ViewPropertyBinder<T, U> to(final @Nonnull V anotherProperty)
    {
        subscriptions.add(property.asObservable().subscribe(anotherProperty::setValue));
        subscriptions.add(anotherProperty.asObservable().subscribe(property::setValue));

        return this;
    }

    @Nonnull
    @Override
    public ViewPropertyBinder<T, U> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
