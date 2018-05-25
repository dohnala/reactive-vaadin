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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.github.dohnal.vaadin.mvvm.binder.ActivableObservableBinder;
import com.github.dohnal.vaadin.mvvm.binder.ActivableObservablePropertyBinder;
import com.github.dohnal.vaadin.mvvm.binder.ActivablePropertyBinder;
import com.github.dohnal.vaadin.reactive.Delayable;
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
import com.github.dohnal.vaadin.reactive.Suppressible;
import com.github.dohnal.vaadin.reactive.activable.CompositeActivable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
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
        Suppressible,
        Delayable,
        ReactiveBinderExtension,
        ReactivePropertyExtension,
        ReactiveCommandExtension,
        ReactiveInteractionExtension
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReactiveViewModel.class);

    private final List<ReactiveProperty<?>> properties;

    private final AtomicInteger suppressed;

    private final AtomicInteger delayed;

    private final AtomicInteger viewCount;

    private final ReactiveProperty<Boolean> activation;

    private final CompositeActivable compositeActivable;

    public ReactiveViewModel()
    {
        this.properties = new ArrayList<>();
        this.suppressed = new AtomicInteger(0);
        this.delayed = new AtomicInteger(0);
        this.viewCount = new AtomicInteger(0);
        this.activation = createProperty();
        this.compositeActivable = new CompositeActivable();
    }

    @Override
    public final boolean isSuppressed()
    {
        return suppressed.get() > 0;
    }

    @Nonnull
    @Override
    public final Disposable suppress()
    {
        suppressed.incrementAndGet();

        final Disposable disposable = new CompositeDisposable(properties.stream()
                .map(ReactiveProperty::suppress)
                .collect(Collectors.toList()));

        return Disposables.fromRunnable(() -> {
            disposable.dispose();
            suppressed.decrementAndGet();
        });
    }

    @Override
    public final boolean isDelayed()
    {
        return delayed.get() > 0;
    }

    @Nonnull
    @Override
    public final Disposable delay()
    {
        delayed.incrementAndGet();

        final Disposable disposable = new CompositeDisposable(properties.stream()
                .map(ReactiveProperty::delay)
                .collect(Collectors.toList()));

        return Disposables.fromRunnable(() -> {
            disposable.dispose();
            delayed.decrementAndGet();
        });
    }

    @Nonnull
    @Override
    public <T> PropertyBinder<T> bind(final @Nonnull Property<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        return new ActivablePropertyBinder<>(compositeActivable,
                ReactiveBinderExtension.super.bind(property));
    }

    @Nonnull
    @Override
    public <T> ObservablePropertyBinder<T> bind(final @Nonnull ObservableProperty<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        return new ActivableObservablePropertyBinder<>(compositeActivable,
                ReactiveBinderExtension.super.bind(property));
    }

    @Nonnull
    @Override
    public <T> ObservableBinder<T> when(final @Nonnull Observable<T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return new ActivableObservableBinder<>(compositeActivable,
                ReactiveBinderExtension.super.when(observable));
    }

    @Override
    public void handleError(final @Nonnull Throwable error)
    {
        Objects.requireNonNull(error, "Error cannot be null");

        LOGGER.error("Unhandled error", error);
    }

    @Nonnull
    @Override
    public <T> ReactiveProperty<T> onCreateProperty(final @Nonnull ReactiveProperty<T> property)
    {
        Objects.requireNonNull(property, "Property cannot be null");

        properties.add(property);

        return property;
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
    final Disposable activate()
    {
        if (viewCount.getAndIncrement() == 0)
        {
            compositeActivable.activate();

            activation.setValue(true);
        }

        return Disposables.fromRunnable(() -> {
            if (viewCount.decrementAndGet() == 0)
            {
                activation.setValue(false);

                compositeActivable.deactivate();
            }
        });
    }
}
