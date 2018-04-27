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
import javax.annotation.Nullable;
import java.util.Collection;

import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.Property;
import com.vaadin.data.HasItems;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressBar;
import rx.Observable;

/**
 * List of all component properties
 *
 * @author dohnal
 */
public interface ComponentProperties extends ComponentEvents
{
    /**
     * Returns visible property of given component
     *
     * @param component component
     * @return visible property
     */
    @Nonnull
    default Property<Boolean> visibleOf(final @Nonnull Component component)
    {
        return value -> component.setVisible(Boolean.TRUE.equals(value));
    }

    /**
     * Returns enabled property of given component
     *
     * @param component component
     * @return enabled property
     */
    @Nonnull
    default Property<Boolean> enabledOf(final @Nonnull Component component)
    {
        return value -> component.setEnabled(Boolean.TRUE.equals(value));
    }

    /**
     * Returns read-only property of given field
     *
     * @param field field
     * @return read-only property
     */
    @Nonnull
    default Property<Boolean> readOnlyOf(final @Nonnull HasValue<?> field)
    {
        return value -> field.setReadOnly(Boolean.TRUE.equals(value));
    }

    /**
     * Returns value property of given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return value property
     */
    @Nonnull
    default <T> ObservableProperty<T> valueOf(final @Nonnull HasValue<T> field)
    {
        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return valueChangedOf(field);
            }

            @Override
            public void setValue(final @Nullable T value)
            {
                field.setValue(value);
            }
        };
    }

    /**
     * Returns value property of given progress bar
     *
     * @param progressBar progress bar
     * @return value property
     */
    @Nonnull
    default Property<Float> valueOf(final @Nonnull ProgressBar progressBar)
    {
        return value -> progressBar.setValue(value != null ? value : 0.0f);
    }

    /**
     * Returns items property of given component
     *
     * @param component component with items
     * @param <T> type of item
     * @param <C> type of collection of items
     * @return items property
     */
    @Nonnull
    default <T, C extends Collection<T>> Property<C> itemsOf(final @Nonnull HasItems<T> component)
    {
        return component::setItems;
    }
}
