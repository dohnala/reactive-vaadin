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
 * Represents something which can delay change notifications
 *
 * @author dohnal
 */
public interface Delayable
{
    /**
     * Returns whether change notifications are currently delayed
     *
     * @return whether change notifications are currently delayed
     */
    boolean isDelayed();

    /**
     * Delays change notifications until the returned value is disposed and then fires latest change notifications
     *
     * @return disposable which when disposed, enables change notifications and fires latest change notifications
     */
    @Nonnull
    Disposable delay();

    /**
     * Delays change notifications while given action is run
     * and after it completes, enables change notifications and fires latest change notifications
     *
     * @param action action to run
     */
    default void delay(final @Nonnull Runnable action)
    {
        Objects.requireNonNull(action, "Action cannot be null");

        final Disposable disposable = delay();

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
