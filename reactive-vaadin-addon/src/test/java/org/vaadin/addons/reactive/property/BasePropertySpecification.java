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

package org.vaadin.addons.reactive.property;

import javax.annotation.Nonnull;

import org.vaadin.addons.reactive.ReactiveProperty;

/**
 * Base interface for all command specifications
 *
 * @author dohnal
 */
public interface BasePropertySpecification
{
    /**
     * Base interface for specifications which needs property
     *
     * @param <T> type of property value
     * @author dohnal
     */
    interface RequireProperty<T>
    {
        @Nonnull
        ReactiveProperty<T> getProperty();
    }

    /**
     * Base interface for specifications which needs source emitter
     *
     * @param <T> type of property value
     * @author dohnal
     */
    interface RequireSource<T> extends RequireProperty<T>
    {
        void emitValue(final @Nonnull T value);
    }
}
