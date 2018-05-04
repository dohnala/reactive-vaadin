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

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Reactive command encapsulates an execution and provides a reactive way how to listen for its
 * execution result, errors or state
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public interface ReactiveCommand<T, R>
{
    /**
     * Returns an observable of execution result
     *
     * @return observable of execution result
     */
    @Nonnull
    Observable<R> getResult();

    /**
     * Returns an observable of errors produces during execution
     *
     * @return observable of errors
     */
    @Nonnull
    Observable<Throwable> getError();

    /**
     * Returns an observable of whether command is executing right now
     *
     * @return an observable of whether command is executing right now
     */
    @Nonnull
    Observable<Boolean> isExecuting();

    /**
     * Returns an observable of number of executions for this command
     *
     * @return an observable of number of executions for this command
     */
    @Nonnull
    Observable<Integer> getExecutionCount();

    /**
     * Returns an observable of whether command has been executed
     *
     * @return an observable of whether command has been executed
     */
    @Nonnull
    Observable<Boolean> hasBeenExecuted();

    /**
     * Returns an observable of whether command can be executed right now
     *
     * @return an observable of whether command can be executed right now
     */
    @Nonnull
    Observable<Boolean> canExecute();

    /**
     * Returns an observable of command execution progress if it is currently executing
     *
     * @return an observable of command execution progress if it is currently executing
     */
    @Nonnull
    Observable<Float> getProgress();

    /**
     * Executes this command with no input
     *
     * @return completable to notify when an execution is completed
     * @throws IllegalArgumentException if this command requires input
     */
    Completable execute();

    /**
     * Executes this command with given input
     *
     * @param input command input
     * @return completable to notify when an execution is completed
     */
    Completable execute(final @Nonnull T input);
}
