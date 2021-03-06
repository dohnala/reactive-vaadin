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

package org.vaadin.addons.reactive.command;

import javax.annotation.Nonnull;
import java.util.Objects;

import org.vaadin.addons.reactive.ProgressContext;
import org.vaadin.addons.reactive.ReactiveProperty;

/**
 * Implementation of {@link ProgressContext} which sets values to {@link ReactiveProperty}
 *
 * @author dohnal
 */
public final class ReactiveProgressContext implements ProgressContext
{
    private final ReactiveProperty<Float> property;

    public ReactiveProgressContext(final @Nonnull ReactiveProperty<Float> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        this.property = property;

        if (!property.hasValue())
        {
            property.setValue(0.0f);
        }
    }

    @Override
    public void set(float value)
    {
        final Float currentValue = property.getValue();

        Objects.requireNonNull(currentValue);

        final Float valueToSet = Math.max(Math.min(value, 1.0f), currentValue);

        if (!Objects.equals(currentValue, valueToSet))
        {
            property.setValue(valueToSet);
        }
    }

    @Override
    public void add(float value)
    {
        final Float currentValue = property.getValue();

        Objects.requireNonNull(currentValue);

        set(currentValue + value);
    }

    @Override
    public float getCurrentProgress()
    {
        return property.getValue() != null ? property.getValue() : 0.0f;
    }
}
