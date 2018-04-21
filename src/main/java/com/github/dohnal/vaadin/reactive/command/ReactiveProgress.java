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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.Progress;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;

/**
 * Implementation of {@link Progress} which sets values to {@link ReactiveProperty}
 *
 * @author dohnal
 */
public final class ReactiveProgress implements Progress
{
    private final ReactiveProperty<Float> property;

    public ReactiveProgress(final @Nonnull ReactiveProperty<Float> property)
    {
        this.property = property;
    }

    @Override
    public void set(float value)
    {
        final Float currentValue = property.getValue();

        final Float valueToSet = Math.max(Math.min(value, 1.0f), currentValue != null ? currentValue : 0.0f);

        if (!Objects.equals(currentValue, valueToSet))
        {
            property.setValue(valueToSet);
        }
    }

    @Override
    public void add(float value)
    {
        final Float currentValue = property.getValue();

        set((currentValue != null ? currentValue : 0.0f) + value);
    }
}
