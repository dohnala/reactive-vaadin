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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * List of all component actions
 *
 * @author dohnal
 */
public interface ComponentActions
{
    /**
     * Returns an action which shows given notification in current page
     *
     * @param notificationSupplier notification supplier
     * @return action
     */
    @Nonnull
    default Runnable show(final @Nonnull Supplier<Notification> notificationSupplier)
    {
        Objects.requireNonNull(notificationSupplier, "Notification supplier cannot be null");

        return () -> notificationSupplier.get().show(Page.getCurrent());
    }

    /**
     * Returns an action which shows given notification with action value in current page
     *
     * @param notificationFunction notificationFunction which creates notification for given value
     * @return action
     */
    @Nonnull
    default <T> Consumer<T> show(final @Nonnull Function<T, Notification> notificationFunction)
    {
        Objects.requireNonNull(notificationFunction, "Notification function cannot be null");

        return value -> notificationFunction.apply(value).show(Page.getCurrent());
    }
}
