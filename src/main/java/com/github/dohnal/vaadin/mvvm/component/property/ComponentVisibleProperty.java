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

import com.vaadin.ui.Component;

/**
 * @author dohnal
 */
public final class ComponentVisibleProperty extends AbstractComponentProperty<Boolean>
{
    private final Component component;

    public ComponentVisibleProperty(final @Nonnull Component component)
    {
        this.component = component;
    }

    @Override
    public final void setValue(final @Nullable Boolean value)
    {
        withUIAccess(component, () -> component.setVisible(Boolean.TRUE.equals(value)));
    }
}
