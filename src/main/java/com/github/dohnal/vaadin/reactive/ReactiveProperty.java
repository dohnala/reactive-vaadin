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
import javax.annotation.Nullable;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.exceptions.ReadOnlyPropertyException;

/**
 * Reactive property stores single editable value and also acts as observable
 *
 * @param <T> type of property
 * @author dohnal
 */
public interface ReactiveProperty<T> extends ObservableProperty<T>
{
    /**
     * Returns whether this property has any value
     *
     * @return whether this property has any value
     */
    boolean hasValue();

    /**
     * Returns whether this property is read-only
     *
     * @return whether this property is read-only
     */
    boolean isReadOnly();

    /**
     * Returns current value or null if this property has no value
     *
     * @return current value or null if this property has no value
     * @see #hasValue()
     */
    @Nullable
    T getValue();

    /**
     * Sets given value to this property
     *
     * @param value value
     * @throws ReadOnlyPropertyException if this property is read-only
     * @see #isReadOnly()
     */
    @Override
    void setValue(final @Nonnull T value);

    /**
     * Updates a value by given update function
     *
     * @param update update function
     * @throws ReadOnlyPropertyException if this property is read-only
     * @see #isReadOnly()
     */
    void updateValue(final @Nonnull Function<? super T, ? extends T> update);
}
