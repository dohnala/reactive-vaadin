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

package org.vaadin.addons.reactive.mvvm.binder;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

import org.vaadin.addons.reactive.Property;
import org.vaadin.addons.reactive.PropertyBinder;
import org.vaadin.addons.reactive.binder.PropertyBinderDecorator;

/**
 * Decorator which binds property with UI access
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class UIPropertyBinder<T> extends PropertyBinderDecorator<T>
{
    private final Consumer<Runnable> withUIAccess;

    public UIPropertyBinder(final @Nonnull Consumer<Runnable> withUIAccess,
                            final @Nonnull PropertyBinder<T> binder)
    {
        super(binder);

        Objects.requireNonNull(withUIAccess, "With UI access cannot be null");

        this.withUIAccess = withUIAccess;
    }

    @Nonnull
    @Override
    public final Property<T> getProperty()
    {
        return new UIProperty<>(withUIAccess, super.getProperty());
    }
}
