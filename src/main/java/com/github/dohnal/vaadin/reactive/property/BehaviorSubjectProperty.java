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

package com.github.dohnal.vaadin.reactive.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Basic implementation of {@link ReactiveProperty} based on {@link BehaviorSubject}
 *
 * @param <T> type of property
 * @author dohnal
 */
public final class BehaviorSubjectProperty<T> implements ReactiveProperty<T>
{
    private BehaviorSubject<T> subject;

    /**
     * Creates new property with no value
     */
    public BehaviorSubjectProperty()
    {
        this.subject = BehaviorSubject.create();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    public BehaviorSubjectProperty(final @Nullable T defaultValue)
    {
        this.subject = BehaviorSubject.create(defaultValue);
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    public BehaviorSubjectProperty(final @Nonnull Observable<? extends T> observable)
    {
        this();

        observable.subscribe(this::setValue);
    }

    /**
     * Creates new property with another property bound to it
     *
     * @param anotherProperty another property
     */
    public BehaviorSubjectProperty(final @Nonnull ReactiveProperty<? extends T> anotherProperty)
    {
        if (anotherProperty.hasValue())
        {
            this.subject = BehaviorSubject.create(anotherProperty.getValue());
        }
        else
        {
            this.subject = BehaviorSubject.create();
        }

        anotherProperty.asObservable().subscribe(this::setValue);
    }

    @Nullable
    @Override
    public final T getValue()
    {
        return subject.getValue();
    }

    @Override
    public final void setValue(final @Nullable T value)
    {
        if (!Objects.equals(value, getValue()))
        {
            subject.onNext(value);
        }
    }

    @Override
    public boolean hasValue()
    {
        return subject.hasValue();
    }

    @Override
    public final void updateValue(final @Nonnull Function<? super T, ? extends T> update)
    {
        setValue(update.apply(getValue()));
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return subject;
    }
}
