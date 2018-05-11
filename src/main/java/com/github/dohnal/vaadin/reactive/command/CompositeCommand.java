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

package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Composite implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class CompositeCommand<T, R> implements ReactiveCommand<T, List<R>>
{
    private final List<ReactiveCommand<T, R>> commands;

    private final ReactiveCommand<T, List<R>> compositeCommand;

    /**
     * Creates new composite reactive command from given child commands
     *
     * @param canExecute observable which controls command executability
     * @param commands child commands this command is composed from
     * @param scheduler scheduler used to schedule execution
     */
    @SuppressWarnings("unchecked")
    public CompositeCommand(final @Nonnull Observable<Boolean> canExecute,
                            final @Nonnull List<ReactiveCommand<T, R>> commands,
                            final @Nonnull Scheduler scheduler)
    {
        Objects.requireNonNull(canExecute, "CanExecute cannot be null");
        Objects.requireNonNull(commands, "Commands cannot be null");
        Objects.requireNonNull(scheduler, "Scheduler cannot be null");

        if (commands.size() == 0)
        {
            throw new IllegalArgumentException("At least one command is required");
        }

        this.commands = commands;

        final Observable<Boolean> compositeCanExecute = Observable.combineLatest(
                canExecute.startWith(true).distinctUntilChanged(),
                Observable.combineLatest(
                        commands.stream()
                                .map(command -> command
                                        .canExecute()
                                        .startWith(true)
                                        .distinctUntilChanged())
                                .collect(Collectors.toList()),
                        values -> Arrays.stream(Arrays.copyOf(values, values.length, Boolean[].class))
                                .allMatch(Boolean.TRUE::equals)),
                (x, y) -> x && y);

        final Observable<Float> compositeProgress = Observable
                .combineLatest(commands.stream()
                                .map(command -> command.getProgress()
                                        .withLatestFrom(command.isExecuting().take(3), AbstractMap.SimpleImmutableEntry::new)
                                        .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                                        .map(AbstractMap.SimpleImmutableEntry::getKey)
                                        .startWith(0.0f))
                                .collect(Collectors.toList()),
                        values -> computeProgress(Arrays.copyOf(values, values.length, Float[].class)));

        final Function<T, Observable<List<R>>> compositeExecution = input -> Observable
                .just(Optional.ofNullable(input))
                .flatMap(value -> Observable.concat(getChildExecutions(value)))
                .map(Arrays::asList)
                .reduce((x, y) -> Stream.concat(x.stream(), y.stream()).collect(Collectors.toList()))
                .toObservable();

        this.compositeCommand = new Command<>(compositeCanExecute, compositeProgress, compositeExecution, scheduler);
    }

    @Nonnull
    private List<Observable<R>> getChildExecutions(final @Nonnull Optional<T> input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        if (input.isPresent())
        {
            return commands.stream()
                    .map(command -> command.execute(input.get()))
                    .collect(Collectors.toList());
        }

        return commands.stream()
                .map(ReactiveCommand::execute)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public Observable<List<R>> getResult()
    {
        return compositeCommand.getResult();
    }

    @Nonnull
    @Override
    public Observable<Throwable> getError()
    {
        return compositeCommand.getError();
    }

    @Nonnull
    @Override
    public Observable<Boolean> isExecuting()
    {
        return compositeCommand.isExecuting();
    }

    @Nonnull
    @Override
    public Observable<Integer> getExecutionCount()
    {
        return compositeCommand.getExecutionCount();
    }

    @Nonnull
    @Override
    public Observable<Boolean> hasBeenExecuted()
    {
        return compositeCommand.hasBeenExecuted();
    }

    @Nonnull
    @Override
    public Observable<Boolean> canExecute()
    {
        return compositeCommand.canExecute();
    }

    @Nonnull
    @Override
    public Observable<Float> getProgress()
    {
        return compositeCommand.getProgress();
    }

    @Nonnull
    @Override
    public Observable<List<R>> execute()
    {
        return compositeCommand.execute();
    }

    @Nonnull
    @Override
    public Observable<List<R>> execute(final @Nonnull T input)
    {
        Objects.requireNonNull(input, "Input cannot be null");

        return compositeCommand.execute(input);
    }

    @Nonnull
    private Float computeProgress(final @Nonnull Float... values)
    {
        Objects.requireNonNull(values, "Values cannot be null");

        return Arrays.stream(values).reduce(0.0f, (x, y) -> x + y) / commands.size();
    }
}
