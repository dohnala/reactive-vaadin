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

import com.github.dohnal.vaadin.reactive.interaction.PublishSubjectInteraction;

/**
 * Extension to create instances of {@link ReactiveInteraction}
 *
 * @author dohnal
 */
public interface ReactiveInteractionExtension
{
    /**
     * Creates new interaction
     *
     * @param <T> type of interaction input
     * @param <R> type of interaction result
     * @return created interaction
     */
    @Nonnull
    default <T, R> ReactiveInteraction<T, R> createInteraction()
    {
        return onCreateInteraction(new PublishSubjectInteraction<>());
    }

    /**
     * Extension method with is called when new interaction has been created
     *
     * @param interaction created interaction
     * @param <T> type of interaction input
     * @param <R> type of interaction result
     * @return created interaction
     */
    @Nonnull
    default <T, R> ReactiveInteraction<T, R> onCreateInteraction(final @Nonnull ReactiveInteraction<T, R> interaction)
    {
        Objects.requireNonNull(interaction, "Interaction cannot be null");

        return interaction;
    }
}
