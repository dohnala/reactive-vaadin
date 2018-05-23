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

        return fromEvent(consumer -> button.addClickListener(consumer::accept));
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

        return fromEvent(consumer -> field.addValueChangeListener(consumer::accept));
    }

    /**
     * Returns events captured by given listener as observable
     *
     * @param registerListener function which create and register listener
     * @param <T> type of event
     * @return observable of events
     */
    @Nonnull
    default <T> Observable<T> fromEvent(final @Nonnull Function<Consumer<T>, Registration> registerListener)
    {
        Objects.requireNonNull(registerListener, "Register listener cannot be null");

        return Observable.create(eventEmitter -> {
            final Registration registration = registerListener.apply(eventEmitter::onNext);

            eventEmitter.setCancellable(registration::remove);
        });
    }
}
