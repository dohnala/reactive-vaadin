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

import io.reactivex.disposables.Disposable;

/**
 * Represents a resource which can be activated
 *
 * @author dohnal
 */
public interface Activable
{
    /**
     * Activates the resource
     */
    void activate();

    /**
     * Deactivates the resource
     */
    void deactivate();

    /**
     * Returns true if this resource has been activated
     * @return true if this resource has been activated
     */
    boolean isActivated();

    /**
     * Returns disposable which calls {@link #deactivate()} when disposed
     *
     * @return disposable
     */
    @Nonnull
    Disposable asDisposable();
}
