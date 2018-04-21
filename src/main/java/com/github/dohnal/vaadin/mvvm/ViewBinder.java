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

package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.binder.ViewPropertyBinder;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.binder.ObservableActionBinder;

/**
 * Specialization of {@link ReactiveBinder} for {@link AbstractView}
 *
 * @author dohnal
 */
public interface ViewBinder extends ReactiveBinder
{
    /**
     * Return binder for given property which is also observable
     *
     * @param property property
     * @param <T> type of value of property
     * @param <U> type of property
     * @return binder
     */
    @Nonnull
    default <T, U extends Property<T> & IsObservable<T>> ViewPropertyBinder<T, U> bind(final U property)
    {
        return new ViewPropertyBinder<>(property);
    }

    /**
     * Creates binder which can call action as a reaction to some event
     *
     * @param event event
     * @param <T> type of values
     * @return binder
     */
    @Nonnull
    default <T> ObservableActionBinder<T> when(final @Nonnull Event<T> event)
    {
        return new ObservableActionBinder<>(event.asObservable());
    }
}
