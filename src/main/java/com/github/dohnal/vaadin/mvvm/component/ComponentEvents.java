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

package com.github.dohnal.vaadin.mvvm.component;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.component.event.ButtonClickedEvent;
import com.github.dohnal.vaadin.mvvm.component.event.FieldValueChangedEvent;
import com.github.dohnal.vaadin.reactive.Event;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;

/**
 * List of all component events
 *
 * @author dohnal
 */
public interface ComponentEvents
{
    /**
     * Returns an event which will happen when user click on given button
     *
     * @param button button
     * @return event
     */
    @Nonnull
    default Event<Button.ClickEvent> clickedOn(final @Nonnull Button button)
    {
        return new ButtonClickedEvent(button);
    }

    /**
     * Returns an event which will happen when user changes value of given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return event
     */
    @Nonnull
    default <T> Event<T> valueChangedOf(final @Nonnull HasValue<T> field)
    {
        return new FieldValueChangedEvent<>(field);
    }
}
