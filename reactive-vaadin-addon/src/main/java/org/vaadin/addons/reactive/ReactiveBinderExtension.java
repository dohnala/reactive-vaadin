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

package org.vaadin.addons.reactive;

import javax.annotation.Nonnull;
import java.util.Objects;

import io.reactivex.Observable;
import org.vaadin.addons.reactive.binder.DefaultObservableBinder;
import org.vaadin.addons.reactive.binder.DefaultObservablePropertyBinder;
import org.vaadin.addons.reactive.binder.DefaultPropertyBinder;

/**
 * Extension for binding observables, properties, events and actions
 *
 * @author dohnal
 */
public interface ReactiveBinderExtension extends EventExtension, ActionExtension
{
    /**
     * Handles all unhandled errors caught by binders created by this extension
     *
     * @param error error
     */
    void handleError(final @Nonnull Throwable error);

    /**
     * Returns binder for given property
     *
     * @param property property
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    default <T> PropertyBinder<T> bind(final @Nonnull Property<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        return new DefaultPropertyBinder<>(property, this::handleError);
    }

    /**
     * Returns binder for given observable property
     *
     * @param property property
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    default <T> ObservablePropertyBinder<T> bind(final @Nonnull ObservableProperty<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        return new DefaultObservablePropertyBinder<>(property, this::handleError);
    }

    /**
     * Returns binder for given observable
     *
     * @param observable observable
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    default <T> ObservableBinder<T> when(final @Nonnull Observable<T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return new DefaultObservableBinder<>(observable, this::handleError);
    }

    /**
     * Returns binder for given observable
     *
     * @param isObservable observable
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    default <T> ObservableBinder<T> when(final @Nonnull IsObservable<T> isObservable)
    {
        Objects.requireNonNull(isObservable, "IsObservable cannot be null");

        return when(isObservable.asObservable());
    }
}
