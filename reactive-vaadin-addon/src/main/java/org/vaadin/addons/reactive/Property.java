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

/**
 * Represents property which can be set
 *
 * @param <T> type of value
 * @author dohnal
 */
@FunctionalInterface
public interface Property<T>
{
    /**
     * Sets given value to this property
     *
     * @param value value
     */
    void setValue(final @Nonnull T value);
}
