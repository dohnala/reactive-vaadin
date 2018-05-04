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
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.property.BehaviorSubjectProperty;
import io.reactivex.Observable;

/**
 * Factory to create instances of {@link ReactiveProperty}
 *
 * @author dohnal
 */
public interface ReactivePropertyFactory
{
    /**
     * Creates new property with no value
     *
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createProperty()
    {
        return new BehaviorSubjectProperty<>();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createProperty(final @Nonnull T defaultValue)
    {
        Objects.requireNonNull(defaultValue, "Default value cannot be null");

        return new BehaviorSubjectProperty<>(defaultValue);
    }

    /**
     * Creates new property from given observable
     *
     * @param observable observable
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return new BehaviorSubjectProperty<>(observable);
    }

    /**
     * Creates new property from given property
     *
     * @param property property
     * @param <T> type of property
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(final @Nonnull ReactiveProperty<? extends T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        return new BehaviorSubjectProperty<>(property);
    }
}
