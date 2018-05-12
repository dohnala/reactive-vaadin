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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import io.reactivex.Observable;

/**
 * Extension with component events
 *
 * @author dohnal
 */
public interface ComponentEventExtension
{
    /**
     * Returns an event which will happen when user click on given button
     *
     * @param button button
     * @return event
     */
    @Nonnull
    default Observable<Button.ClickEvent> clickedOn(final @Nonnull Button button)
    {
        Objects.requireNonNull(button, "Button cannot be null");

        return toObservable(consumer -> consumer::accept, button::addClickListener);
    }

    /**
     * Returns an event which will happen when user changes value of given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return event
     */
    @Nonnull
    default <T> Observable<HasValue.ValueChangeEvent<T>> valueChangedOf(final @Nonnull HasValue<T> field)
    {
        Objects.requireNonNull(field, "Field cannot be null");

        return toObservable(consumer -> consumer::accept, field::addValueChangeListener);
    }

    /**
     * Converts given functions to create and register listener to observable of values given listener
     * produces
     *
     * @param createListener function to create listener
     * @param registerListener function to register listener
     * @param <T> type of value given listener produces
     * @param <L> type of listener
     * @return observable of values given listener produces
     */
    @Nonnull
    default <T, L> Observable<T> toObservable(final @Nonnull Function<Consumer<T>, L> createListener,
                                              final @Nonnull Function<L, Registration> registerListener)
    {
        Objects.requireNonNull(createListener, "Create listener cannot be null");
        Objects.requireNonNull(registerListener, "Register listener cannot be null");

        return Observable.create(eventEmitter -> {
            final L listener = createListener.apply(eventEmitter::onNext);

            final Registration registration = registerListener.apply(listener);

            eventEmitter.setCancellable(registration::remove);
        });
    }
}
