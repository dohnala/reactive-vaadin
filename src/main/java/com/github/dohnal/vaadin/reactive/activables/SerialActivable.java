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
import java.util.Objects;
import java.util.function.Supplier;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;

/**
 * Serial implementation of {@link Activable} for single activation
 *
 * @author dohnal
 */
public final class SerialActivable implements Activable
{
    private final Supplier<Disposable> activation;

    private final SerialDisposable disposable;

    /**
     * Creates new activable from given activation
     *
     * @param activation activation
     */
    public SerialActivable(final @Nonnull Supplier<Disposable> activation)
    {
        Objects.requireNonNull(activation, "Activation cannot be null");

        this.activation = activation;
        this.disposable = new SerialDisposable();
    }

    @Override
    public void activate()
    {
        disposable.set(activation.get());
    }

    @Override
    public void deactivate()
    {
        disposable.dispose();
    }

    @Override
    public boolean isActivated()
    {
        return disposable.get() != null && !disposable.get().isDisposed();
    }

    @Nonnull
    @Override
    public Disposable asDisposable()
    {
        return disposable;
    }
}
