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
import javax.annotation.Nullable;
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ObservableBinder;
import com.github.dohnal.vaadin.reactive.ObservableProperty;
import com.github.dohnal.vaadin.reactive.ObservablePropertyBinder;
import com.github.dohnal.vaadin.reactive.Property;
import com.github.dohnal.vaadin.reactive.PropertyBinder;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.binder.ObservableBinderDecorator;
import com.github.dohnal.vaadin.reactive.binder.ObservablePropertyBinderDecorator;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import rx.Observable;

/**
 * Base class for all view in MVVM pattern
 *
 * @param <M> type of view model
 * @author dohnal
 */
public abstract class AbstractView<M extends AbstractViewModel>
        extends CustomComponent
        implements ReactiveBinder, ComponentEvents, ComponentProperties, ComponentActions
{
    private M viewModel;

    /**
     * Creates view for given view model
     *
     * @param viewModel view model
     */
    public AbstractView(final @Nonnull M viewModel)
    {
        this.viewModel = viewModel;
    }

    /**
     * Returns binder for given property which wraps setting property value to
     * hold the session lock to ensure exclusive access to UI this view is attached to
     *
     * @param property property
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    @Override
    public <T> PropertyBinder<T> bind(final @Nonnull Property<T> property)
    {
        return ReactiveBinder.super.bind(withUIAccess(property));
    }

    /**
     * Returns binder for given observable property which wraps setting property value to
     * hold the session lock to ensure exclusive access to UI this view is attached to
     *
     * @param property property
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    @Override
    public <T> ObservablePropertyBinder<T> bind(final @Nonnull ObservableProperty<T> property)
    {
        final ObservablePropertyBinder<T> binder = ReactiveBinder.super.bind(withUIAccess(property));

        return new ObservablePropertyBinderDecorator<T>(binder)
        {
            @Nonnull
            @Override
            public Disposable to(final @Nonnull ObservableProperty<T> anotherProperty)
            {
                return super.to(withUIAccess(anotherProperty));
            }
        };
    }

    /**
     * Returns binder for given observable which wraps calling actions to hold the session lock
     * to ensure exclusive access to UI this view is attached to
     *
     * @param observable observable
     * @param <T> type of value
     * @return binder
     */
    @Nonnull
    @Override
    public <T> ObservableBinder<T> when(final @Nonnull Observable<T> observable)
    {
        final ObservableBinder<T> binder = ReactiveBinder.super.when(observable);

        return new ObservableBinderDecorator<T>(binder)
        {
            @Nonnull
            @Override
            public Disposable then(final @Nonnull Consumer<? super T> action)
            {
                return super.then(value -> withUIAccess(() -> action.accept(value)));
            }

            @Nonnull
            @Override
            public Disposable then(final @Nonnull Runnable action)
            {
                return super.then(() -> withUIAccess(action));
            }
        };
    }

    /**
     * Wraps given property to set its value while holding the session lock
     * to ensure exclusive access to UI this view is attached to
     *
     * @param property property
     * @param <T> type of value
     * @return wrapped property
     */
    @Nonnull
    protected <T> Property<T> withUIAccess(final @Nonnull Property<T> property)
    {
        return value -> withUIAccess(() -> property.setValue(value));
    }

    /**
     * Wraps given observable property to set its value while holding the session lock
     * to ensure exclusive access to UI this view is attached to
     *
     * @param property property
     * @param <T> type of value
     * @return wrapped property
     */
    @Nonnull
    protected <T> ObservableProperty<T> withUIAccess(final @Nonnull ObservableProperty<T> property)
    {
        return new ObservableProperty<T>()
        {
            @Nonnull
            @Override
            public Observable<T> asObservable()
            {
                return property.asObservable();
            }

            @Override
            public void setValue(final @Nullable T value)
            {
                withUIAccess(() -> property.setValue(value));
            }
        };
    }

    /**
     * Runs given action while holding the session lock to ensure exclusive access
     * to UI this view is attached to
     *
     * @param action action
     */
    protected void withUIAccess(final @Nonnull Runnable action)
    {
        final UI ui = getUI();

        if (ui != null && ui.isAttached())
        {
            ui.access(action);
        }
        else
        {
            action.run();
        }
    }
}
