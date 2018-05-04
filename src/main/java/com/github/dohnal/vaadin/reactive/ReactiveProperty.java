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

/**
 * Reactive property stores single editable value and also acts as observable
 *
 * @param <T> type of property
 * @author dohnal
 */
public interface ReactiveProperty<T> extends ObservableProperty<T>
{
    /**
     * Returns current value
     * NOTE: returns null if this property has no value
     *
     * @return current value
     * @see #hasValue()
     */
    @Nullable
    T getValue();

    /**
     * Return if this property has any value
     *
     * @return if this property has any value
     */
    boolean hasValue();

    /**
     * Updates a value by given update function and notifies all subscribers
     *
     * @param update update function
     */
    void updateValue(final @Nonnull Function<? super T, ? extends T> update);
}
