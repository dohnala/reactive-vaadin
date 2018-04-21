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

import com.github.dohnal.vaadin.mvvm.component.event.FieldValueChangedEvent;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.vaadin.ui.AbstractField;
import rx.Observable;

/**
 * @author dohnal
 */
public final class FieldValueProperty<T> extends AbstractComponentProperty<T> implements IsObservable<T>
{
    private final AbstractField<T> field;

    public FieldValueProperty(final @Nonnull AbstractField<T> field)
    {
        this.field = field;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return new FieldValueChangedEvent<>(field).asObservable();
    }

    @Override
    public final void setValue(final @Nullable T value)
    {
        withUIAccess(field, () -> field.setValue(value));
    }
}
