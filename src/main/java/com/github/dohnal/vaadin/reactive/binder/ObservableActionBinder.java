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
import java.util.ArrayList;
import java.util.List;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import rx.Observable;
import rx.Subscription;

/**
 * Implementation of {@link ReactiveBinder} for binding actions as a reaction to observable changes
 *
 * @param <T> type of value in the observable
 * @author dohnal
 */
public class ObservableActionBinder<T> implements Disposable<ObservableActionBinder<T>>
{
    private final Observable<T> observable;

    private List<Subscription> subscriptions;

    public ObservableActionBinder(final @Nonnull Observable<T> observable)
    {
        this.observable = observable;
        this.subscriptions = new ArrayList<>();
    }

    /**
     * Binds given action which will be called whenever this observable is changed
     *
     * @param action action
     * @param <A> type of action
     * @return this binder
     */
    @Nonnull
    public <A extends Action<? super T>> ObservableActionBinder<T> then(final @Nonnull A action)
    {
        subscriptions.add(observable.subscribe(action::call));

        return this;
    }

    @Nonnull
    @Override
    public ObservableActionBinder<T> unbind()
    {
        subscriptions.stream()
                .filter(subscription -> !subscription.isUnsubscribed())
                .forEach(Subscription::unsubscribe);

        subscriptions.clear();

        return this;
    }
}
