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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.binder.ObservableActionBinder;
import com.github.dohnal.vaadin.reactive.binder.ObservablePropertyBinder;
import rx.Observable;

/**
 * Base interface for binders which can bind observables to properties or call actions as a reaction
 * when any observable is changed
 *
 * @author dohnal
 */
public interface ReactiveBinder
{
    /**
     * Creates binder for binding observable of values
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservablePropertyBinder<T> bind(final @Nonnull Observable<T> observable)
    {
        return new ObservablePropertyBinder<>(observable);
    }

    /**
     * Creates binder for binding something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservablePropertyBinder<T> bind(final @Nonnull IsObservable<T> isObservable)
    {
        return new ObservablePropertyBinder<>(isObservable.asObservable());
    }

    /**
     * Creates binder which can call action as a reaction when given observable is changed
     *
     * @param observable observable
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableActionBinder<T> whenChanged(final @Nonnull Observable<T> observable)
    {
        return new ObservableActionBinder<>(observable);
    }

    /**
     * Creates binder which can call action as a reaction to change of something what can be observed
     *
     * @param isObservable something what can be observed
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableActionBinder<T> whenChanged(final @Nonnull IsObservable<T> isObservable)
    {
        return whenChanged(isObservable.asObservable());
    }
}
