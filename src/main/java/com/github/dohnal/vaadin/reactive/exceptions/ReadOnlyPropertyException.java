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

package com.github.dohnal.vaadin.reactive.exceptions;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;

/**
 * Indicates that read-only property is modified
 *
 * @author dohnal
 */
public class ReadOnlyPropertyException extends RuntimeException
{
    private final ReactiveProperty<?> property;

    public ReadOnlyPropertyException(final @Nonnull ReactiveProperty<?> property)
    {
        super("Property is read-only");

        Objects.requireNonNull(property, "Property cannot be null");

        this.property = property;
    }

    /**
     * Returns property which this exception was thrown for
     *
     * @return property
     */
    @Nonnull
    public ReactiveProperty<?> getProperty()
    {
        return property;
    }
}
