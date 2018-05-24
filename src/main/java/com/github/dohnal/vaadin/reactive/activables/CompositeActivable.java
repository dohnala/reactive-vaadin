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

package com.github.dohnal.vaadin.reactive.activables;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Composite implementation of {@link Activable} for multiple activables
 *
 * @author dohnal
 */
public final class CompositeActivable implements Activable
{
    private final List<Activable> activables;

    /**
     * Creates new composite activable with empty activables
     */
    public CompositeActivable()
    {
        this.activables = new ArrayList<>();
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
    }

    /**
     * Deactivates all activables and clear them
     */
    public void clear()
    {
        deactivate();

        activables.clear();
    }

    @Override
    public void activate()
    {
        activables.forEach(Activable::activate);
    }

    @Override
    public void deactivate()
    {
        activables.forEach(Activable::deactivate);
    }

    @Override
    public boolean isActivated()
    {
        return activables.stream().allMatch(Activable::isActivated);
    }

    @Nonnull
    @Override
    public Disposable asDisposable()
    {
        return new CompositeDisposable(activables.stream()
                .map(Activable::asDisposable)
                .collect(Collectors.toList()));
    }
}
