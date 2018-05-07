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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

/**
 * Represents something which can suppress change notifications
 *
 * @author dohnal
 */
public interface Suppressible
{
    /**
     * Returns whether change notifications are currently suppressed
     *
     * @return whether change notifications are currently suppressed
     */
    boolean isSuppressed();

    /**
     * Suppresses change notifications until the returned value is disposed
     *
     * @return disposable which when disposed, enables change notifications
     */
    @Nonnull
    Disposable suppress();

    /**
     * Suppresses change notifications while given action is run and after it completes, enables change notifications
     *
     * @param action action to run
     */
    default void suppress(final @Nonnull Runnable action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        final Disposable disposable = suppress();

        try
        {
            action.run();
        }
        finally
        {
            disposable.dispose();
        }
    }
}
