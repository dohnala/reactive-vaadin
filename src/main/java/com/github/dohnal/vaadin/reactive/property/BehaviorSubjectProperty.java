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
import com.github.dohnal.vaadin.reactive.exceptions.ReadOnlyPropertyException;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Basic implementation of {@link ReactiveProperty} based on {@link BehaviorSubject}
 *
 * @param <T> type of property
 * @author dohnal
 */
public final class BehaviorSubjectProperty<T> implements ReactiveProperty<T>
{
    private final BehaviorSubject<T> subject;

    private final Boolean readOnly;

    /**
     * Creates new property with no value
     */
    public BehaviorSubjectProperty()
    {
        this.subject = BehaviorSubject.create();
        this.readOnly = false;
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     */
    public BehaviorSubjectProperty(final @Nonnull T defaultValue)
    {
        Objects.requireNonNull(defaultValue, "Default value cannot be null");

        this.subject = BehaviorSubject.createDefault(defaultValue);
        this.readOnly = false;
    }

    /**
     * Creates new property with observable bound to it
     *
     * @param observable observable
     */
    public BehaviorSubjectProperty(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        this.subject = BehaviorSubject.create();
        this.readOnly = true;

        observable.subscribe(subject::onNext, subject::onError, subject::onComplete);
    }

    @Override
    public final boolean hasValue()
    {
        return subject.hasValue();
    }

    @Override
    public final boolean isReadOnly()
    {
        return Boolean.TRUE.equals(readOnly);
    }

    @Nullable
    @Override
    public final T getValue()
    {
        return subject.getValue();
    }

    @Override
    public final void setValue(final @Nonnull T value)
    {
        Objects.requireNonNull(value, "Value cannot be null");

        if (isReadOnly())
        {
            throw new ReadOnlyPropertyException("Property is read-only");
        }

        subject.onNext(value);
    }

    @Override
    public final void updateValue(final @Nonnull Function<? super T, ? extends T> update)
    {
        Objects.requireNonNull(update, "Update cannot be null");

        setValue(update.apply(getValue()));
    }

    @Nonnull
    @Override
    public final Observable<T> asObservable()
    {
        return subject.distinctUntilChanged();
    }
}
