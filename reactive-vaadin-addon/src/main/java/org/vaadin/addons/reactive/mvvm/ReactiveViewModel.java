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

package org.vaadin.addons.reactive.mvvm;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addons.reactive.Delayable;
import org.vaadin.addons.reactive.ObservableBinder;
import org.vaadin.addons.reactive.ObservableProperty;
import org.vaadin.addons.reactive.ObservablePropertyBinder;
import org.vaadin.addons.reactive.Property;
import org.vaadin.addons.reactive.PropertyBinder;
import org.vaadin.addons.reactive.ReactiveBinderExtension;
import org.vaadin.addons.reactive.ReactiveCommandExtension;
import org.vaadin.addons.reactive.ReactiveInteractionExtension;
import org.vaadin.addons.reactive.ReactiveProperty;
import org.vaadin.addons.reactive.ReactivePropertyExtension;
import org.vaadin.addons.reactive.Suppressible;
import org.vaadin.addons.reactive.activable.CompositeActivable;
import org.vaadin.addons.reactive.mvvm.binder.ActivableObservableBinder;
import org.vaadin.addons.reactive.mvvm.binder.ActivableObservablePropertyBinder;
import org.vaadin.addons.reactive.mvvm.binder.ActivablePropertyBinder;

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
        return activation.asObservable().filter(Boolean.TRUE::equals).map(value -> true);
    }

    /**
     * Returns an event which will happen when this view model is deactivated
     *
     * @return event
     */
    @Nonnull
    protected final Observable<Boolean> deactivated()
    {
        return activation.asObservable().filter(Boolean.FALSE::equals).map(value -> true);
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
