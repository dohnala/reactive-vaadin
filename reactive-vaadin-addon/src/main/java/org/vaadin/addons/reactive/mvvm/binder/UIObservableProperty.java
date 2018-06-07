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

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.vaadin.addons.reactive.ObservableProperty;

/**
 * Represents observable property which values are set with UI access
 *
 * @param <T> type of value
 * @author dohnal
 */
public final class UIObservableProperty<T> implements ObservableProperty<T>
{
    private final Consumer<Runnable> withUIAccess;

    private final ObservableProperty<T> property;

    public UIObservableProperty(final @Nonnull Consumer<Runnable> withUIAccess,
                                final @Nonnull ObservableProperty<T> property)
    {
        Objects.requireNonNull(withUIAccess, "With UI access cannot be null");
        Objects.requireNonNull(property, "Property cannot be null");

        this.withUIAccess = withUIAccess;
        this.property = property;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return property.asObservable();
    }

    @Override
    public final void setValue(final @Nonnull T value)
    {
        withUIAccess.accept(() -> property.setValue(value));
    }

    @Override
    public boolean isSuppressed()
    {
        return property.isSuppressed();
    }

    @Nonnull
    @Override
    public Disposable suppress()
    {
        return property.suppress();
    }
}
