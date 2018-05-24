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

package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.dohnal.vaadin.mvvm.binder.ActivableObservableBinder;
import com.github.dohnal.vaadin.mvvm.binder.ActivableObservablePropertyBinder;
import com.github.dohnal.vaadin.mvvm.binder.ActivablePropertyBinder;
import com.github.dohnal.vaadin.reactive.ObservableBinder;
import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinderExtension;
import com.github.dohnal.vaadin.reactive.ReactiveCommandExtension;
import com.github.dohnal.vaadin.reactive.ReactiveInteractionExtension;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import com.github.dohnal.vaadin.reactive.ReactivePropertyExtension;
import com.github.dohnal.vaadin.reactive.activables.CompositeActivable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all view models in MVVM pattern
 *
 * @author dohnal
 */
public class ReactiveViewModel implements
        ReactiveBinderExtension,
        ReactivePropertyExtension,
        ReactiveCommandExtension,
        ReactiveInteractionExtension
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReactiveViewModel.class);

    private final AtomicInteger refCount;

    private final ReactiveProperty<Boolean> activation;

    private final CompositeActivable compositeActivable;

    public ReactiveViewModel()
    {
        this.refCount = new AtomicInteger(0);
        this.activation = createProperty();
        this.compositeActivable = new CompositeActivable();
    }

    /**
     * Returns an event which will happen when this view model is activated
     *
     * @return event
     */
    @Nonnull
    protected final Observable<Boolean> activated()
    {
        return activation.asObservable().filter(Boolean.TRUE::equals);
    }

    /**
     * Returns an event which will happen when this view model is deactivated
     *
     * @return event
     */
    @Nonnull
    protected final Observable<Boolean> deactivated()
    {
        return activation.asObservable().filter(Boolean.FALSE::equals);
    }

    @Nonnull
    @Override
    public <T> PropertyBinder<T> bind(final @Nonnull Property<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        final PropertyBinder<T> binder = ReactiveBinderExtension.super.bind(property);

        return new ActivablePropertyBinder<>(binder, compositeActivable);
    }

    @Nonnull
    @Override
    public <T> ObservablePropertyBinder<T> bind(final @Nonnull ObservableProperty<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        final ObservablePropertyBinder<T> binder = ReactiveBinderExtension.super.bind(property);

        return new ActivableObservablePropertyBinder<>(binder, compositeActivable);
    }

    @Nonnull
    @Override
    public <T> ObservableBinder<T> when(final @Nonnull Observable<T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        final ObservableBinder<T> binder = ReactiveBinderExtension.super.when(observable);

        return new ActivableObservableBinder<>(binder, compositeActivable);
    }

    @Override
    public void handleError(final @Nonnull Throwable error)
    {
        Objects.requireNonNull(error, "Error cannot be null");

        LOGGER.error("Unhandled error", error);
    }

    @Nonnull
    final Disposable activate()
    {
        if (refCount.getAndIncrement() == 0)
        {
            compositeActivable.activate();

            activation.setValue(true);
        }

        return Disposables.fromRunnable(() -> {
            if (refCount.decrementAndGet() == 0)
            {
                activation.setValue(false);

                compositeActivable.deactivate();
            }
        });
    }
}
