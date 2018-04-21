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

package com.github.dohnal.vaadin.mvvm.component.event;

import javax.annotation.Nonnull;

import com.vaadin.data.HasValue;
import rx.Observable;

/**
 * @author dohnal
 */
public final class FieldValueChangedEvent<T> extends AbstractComponentEvent<T>
{
    private final HasValue<T> field;

    public FieldValueChangedEvent(final @Nonnull HasValue<T> field)
    {
        this.field = field;
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return valueChangeEvent().map(HasValue.ValueChangeEvent::getValue);
    }

    @Nonnull
    public final Observable<HasValue.ValueChangeEvent<T>> valueChangeEvent()
    {
        return toObservable(event -> event::accept, field::addValueChangeListener);
    }
}
