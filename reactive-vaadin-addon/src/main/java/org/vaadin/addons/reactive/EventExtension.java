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

package org.vaadin.addons.reactive;

import javax.annotation.Nonnull;
import java.util.Objects;

import io.reactivex.Observable;

/**
 * Extension with events
 *
 * @author dohnal
 */
public interface EventExtension
{
    /**
     * Returns event that represents that given observable has been changed
     *
     * @param observable observable
     * @param <T> type of value
     * @return event
     */
    @Nonnull
    default <T> Observable<T> changed(final @Nonnull Observable<T> observable)
    {
        Objects.requireNonNull(observable, "Observable cannot be null");

        return observable;
    }

    /**
     * Returns event that represents that given observable has been changed
     *
     * @param isObservable observable
     * @param <T> type of value
     * @return event
     */
    @Nonnull
    default <T> Observable<T> changed(final @Nonnull IsObservable<T> isObservable)
    {
        Objects.requireNonNull(isObservable, "IsObservable cannot be null");

        return changed(isObservable.asObservable());
    }

    /**
     * Returns event that represents that given command has succeeded
     *
     * @param command command
     * @param <R> type of command result
     * @return event
     */
    @Nonnull
    default <R> Observable<R> succeeded(final @Nonnull ReactiveCommand<?, R> command)
    {
        Objects.requireNonNull(command, "Command cannot be null");

        return command.getResult();
    }

    /**
     * Returns event that represents that given command has failed
     *
     * @param command command
     * @return event
     */
    @Nonnull
    default Observable<Throwable> failed(final @Nonnull ReactiveCommand<?, ?> command)
    {
        Objects.requireNonNull(command, "Command cannot be null");

        return command.getError();
    }

    /**
     * Returns event that represents that given command has finished (succeeded or failed)
     * Parameter is current execution count
     *
     * @param command command
     * @return event
     */
    @Nonnull
    default Observable<Integer> finished(final @Nonnull ReactiveCommand<?, ?> command)
    {
        Objects.requireNonNull(command, "Command cannot be null");

        return command.getExecutionCount().filter(count -> count > 0);
    }

    /**
     * Returns event that represents that given interaction has been invoked
     *
     * @param interaction interaction
     * @param <T> type of interaction input
     * @param <R> type of interaction result
     * @return event
     */
    @Nonnull
    default <T, R> Observable<InteractionContext<T, R>> invoked(final @Nonnull ReactiveInteraction<T, R> interaction)
    {
        Objects.requireNonNull(interaction, "Interaction cannot be null");

        return interaction.asObservable();
    }
}
