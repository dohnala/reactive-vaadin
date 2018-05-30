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

package org.vaadin.addons.reactive.activable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.vaadin.addons.reactive.Activable;

/**
 * Composite implementation of {@link Activable} for multiple activable
 *
 * @author dohnal
 */
public final class CompositeActivable implements Activable
{
    private final List<Activable> activables;

    private final CompositeDisposable compositeDisposable;

    private final AtomicBoolean isActivated;

    /**
     * Creates new composite activable with empty activable
     */
    public CompositeActivable()
    {
        this.activables = new ArrayList<>();
        this.compositeDisposable = new CompositeDisposable();
        this.isActivated = new AtomicBoolean(false);
    }

    /**
     * Adds new activable
     *
     * @param activable activable
     */
    public void add(final @Nonnull Activable activable)
    {
        Objects.requireNonNull(activable, "Activable cannot be null");

        activables.add(activable);
        compositeDisposable.add(activable.asDisposable());
    }

    /**
     * Deactivates all activable and clear them
     */
    public void clear()
    {
        deactivate();

        activables.clear();
        compositeDisposable.clear();
    }

    @Override
    public void activate()
    {
        isActivated.set(true);
        activables.forEach(Activable::activate);
    }

    @Override
    public void deactivate()
    {
        isActivated.set(false);
        activables.forEach(Activable::deactivate);
    }

    @Override
    public boolean isActivated()
    {

        return !compositeDisposable.isDisposed() && isActivated.get();
    }

    @Nonnull
    @Override
    public Disposable asDisposable()
    {
        return compositeDisposable;
    }
}
