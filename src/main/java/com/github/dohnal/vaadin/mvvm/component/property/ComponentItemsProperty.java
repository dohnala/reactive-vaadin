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

package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import com.vaadin.data.HasItems;

/**
 * @author dohnal
 */
public final class ComponentItemsProperty<T, C extends Collection<T>> extends AbstractComponentProperty<C>
{
    private final HasItems<T> component;

    public ComponentItemsProperty(final @Nonnull HasItems<T> component)
    {
        this.component = component;
    }

    @Override
    public final void setValue(final @Nullable C value)
    {
        withUIAccess(component, () -> component.setItems(value));
    }
}
