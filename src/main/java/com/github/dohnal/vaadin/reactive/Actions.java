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
import java.util.function.Consumer;

/**
 * List of all actions
 *
 * @author dohnal
 */
public interface Actions
{
    /**
     * Returns an action which will execute given command without any input parameter
     *
     * @param command command
     * @return action
     */
    @Nonnull
    default Runnable execute(final @Nonnull ReactiveCommand<Void, ?> command)
    {
        return () -> command.execute(null);
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
    default <T> Runnable executeWithInput(final @Nonnull ReactiveCommand<? super T, ?> command,
                                          final @Nullable T input)
    {
        return () -> command.execute(input);
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
    default <T> Consumer<T> executeWithInput(final @Nonnull ReactiveCommand<? super T, ?> command)
    {
        return command::execute;
    }
}
