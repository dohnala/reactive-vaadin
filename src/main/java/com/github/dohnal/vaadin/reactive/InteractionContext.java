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
import javax.annotation.Nullable;

import com.github.dohnal.vaadin.reactive.exceptions.AlreadyHandledInteractionException;

/**
 * Contains contextual information for {@link ReactiveInteraction}
 *
 * @param <T> type of input
 * @param <R> type of result
 * @author dohnal
 */
public interface InteractionContext<T, R>
{
    /**
     * Returns an input the interaction was called with
     *
     * @return an input the interaction was called with
     */
    @Nullable
    T getInput();

    /**
     * Returns value indicating if the interaction is handled
     *
     * @return value indicating if the interaction is handled
     */
    boolean isHandled();

    /**
     * Handles interaction with no result
     *
     * @throws NullPointerException if the interaction requires result
     * @throws AlreadyHandledInteractionException if the interaction is already handled
     */
    void handle();

    /**
     * Handles interaction with given result
     *
     * @param result result
     * @throws AlreadyHandledInteractionException if the interaction is already handled
     */
    void handle(final @Nonnull R result);
}
