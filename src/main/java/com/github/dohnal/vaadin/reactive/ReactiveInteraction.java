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
import java.util.function.Consumer;

import com.github.dohnal.vaadin.reactive.exceptions.UnhandledInteractionException;

/**
 * Represents an interaction between collaborating parties.
 *
 * @param <T> type of input
 * @param <R> type of result
 * @author dohnal
 */
public interface ReactiveInteraction<T, R> extends IsObservable<InteractionContext<T, R>>
{
    /**
     * Invokes interaction with no input and call an action when the interaction is handled
     *
     * @param action action called when interaction is handled and result is available
     * @throws UnhandledInteractionException if no handler is subscribed
     */
    void invoke(final @Nonnull Runnable action);

    /**
     * Invokes interaction with no input and call an action when the interaction is handled
     *
     * @param action action called when interaction is handled and result is available
     * @throws UnhandledInteractionException if no handler is subscribed
     */
    void invoke(final @Nonnull Consumer<? super R> action);

    /**
     * Invokes interaction with given input and call an action when the interaction is handled
     *
     * @param input input
     * @param action action called when interaction is handled
     * @throws UnhandledInteractionException if no handler is subscribed
     */
    void invoke(final @Nonnull T input, final @Nonnull Runnable action);

    /**
     * Invokes interaction with given input and call an action when the interaction is handled
     *
     * @param input input
     * @param action action called when interaction is handled and result is available
     * @throws UnhandledInteractionException if no handler is subscribed
     */
    void invoke(final @Nonnull T input, final @Nonnull Consumer<? super R> action);
}
