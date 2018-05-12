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
import java.util.function.Function;

import io.reactivex.Observable;

/**
 * Extension with actions
 *
 * @author dohnal
 */
public interface ActionExtension
{
    /**
     * Returns an action which will execute given command without any input parameter
     *
     * @param command command
     * @return action
     */
    @Nonnull
    default Function<Object, Observable<?>> execute(final @Nonnull ReactiveCommand<Void, ?> command)
    {
        Objects.requireNonNull(command, "Command cannot be null");

        return value -> command.execute();
    }

    /**
     * Returns an action which will execute given command with given input parameter
     *
     * @param command command
     * @param input input parameter to command execution
     * @param <T> type of command input
     * @return action
     */
    @Nonnull
    default <T> Function<Object, Observable<?>> executeWithInput(final @Nonnull ReactiveCommand<? super T, ?> command,
                                                                 final @Nonnull T input)
    {
        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(input, "Input cannot be null");

        return value -> command.execute(input);
    }

    /**
     * Returns an action which will execute given command with value passed to this action as
     * input parameter
     *
     * @param command command
     * @param <T> input parameter to command execution
     * @return action
     */
    @Nonnull
    default <T> Function<T, Observable<?>> executeWithInput(final @Nonnull ReactiveCommand<? super T, ?> command)
    {
        Objects.requireNonNull(command, "Command cannot be null");

        return command::execute;
    }
}
