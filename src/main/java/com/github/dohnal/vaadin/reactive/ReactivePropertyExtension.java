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
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.dohnal.vaadin.reactive.property.BehaviorSubjectProperty;
import io.reactivex.Observable;

/**
 * Extension to create instances of {@link ReactiveProperty}
 *
 * @author dohnal
 */
public interface ReactivePropertyExtension
{
    /**
     * Creates new property with no value
     *
     * @param <T> type of property value
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createProperty()
    {
        return new BehaviorSubjectProperty<>();
    }

    /**
     * Creates new property with given default value
     *
     * @param defaultValue default value
     * @param <T> type of property value
     * @return created property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createProperty(final @Nonnull T defaultValue)
    {
        Objects.requireNonNull(defaultValue, "Default value cannot be null");

        return new BehaviorSubjectProperty<>(defaultValue);
    }

    /**
     * Creates new read-only property from given source observable
     *
     * @param observable source observable
     * @param <T> type of property value
     * @return created read-only property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(final @Nonnull Observable<? extends T> observable)
    {
        Objects.requireNonNull(observable, "Source observable cannot be null");

        return new BehaviorSubjectProperty<>(observable);
    }

    /**
     * Creates new read-only property from given source property
     *
     * @param property source property
     * @param <T> type of property value
     * @return created read-only property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(final @Nonnull ReactiveProperty<? extends T> property)
    {
        Objects.requireNonNull(property, "Source property cannot be null");

        return new BehaviorSubjectProperty<>(property.asObservable());
    }

    /**
     * Creates new read-only property from given source property mapped with given function
     *
     * @param property source property
     * @param function function
     * @param <T> type of property value
     * @param <U> type of source property value
     * @return created read-only property
     */
    @Nonnull
    default <T, U> ReactiveProperty<T> createPropertyFrom(final @Nonnull ReactiveProperty<? extends U> property,
                                                          final @Nonnull Function<? super U, ? extends T> function)
    {
        Objects.requireNonNull(property, "Source property cannot be null");
        Objects.requireNonNull(function, "Function cannot be null");

        return createPropertyFrom(property.asObservable().map(function::apply));
    }

    /**
     * Creates new read-only property from given source properties
     *
     * @param first first property
     * @param second second property
     * @param <T> type of property value
     * @return created read-only property
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(final @Nonnull ReactiveProperty<? extends T> first,
                                                       final @Nonnull ReactiveProperty<? extends T> second)
    {
        Objects.requireNonNull(first, "First property cannot be null");
        Objects.requireNonNull(second, "Second property cannot be null");

        return createPropertyFrom(Observable.merge(first.asObservable(), second.asObservable()));
    }

    /**
     * Creates new read-only property from combining given source properties with given combiner
     *
     * @param first first source property
     * @param second second source property
     * @param combiner combiner
     * @param <T> type of property value
     * @param <U> type of first source property value
     * @param <V> type of second source property value
     * @return created read-only property
     */
    @Nonnull
    default <T, U, V> ReactiveProperty<T> createPropertyFrom(
            final @Nonnull ReactiveProperty<? extends U> first,
            final @Nonnull ReactiveProperty<? extends V> second,
            final @Nonnull BiFunction<? super U, ? super V, ? extends T> combiner)
    {
        Objects.requireNonNull(first, "First source property cannot be null");
        Objects.requireNonNull(second, "Second source property cannot be null");
        Objects.requireNonNull(combiner, "Combiner cannot be null");

        return createPropertyFrom(Observable.combineLatest(
                first.asObservable(), second.asObservable(), combiner::apply));
    }

    /**
     * Creates new read-only property from merging given source properties
     *
     * @param properties source properties
     * @param <T> type of property value
     * @return created read-only property
     * @throws IllegalArgumentException if source properties are empty
     */
    @Nonnull
    default <T> ReactiveProperty<T> createPropertyFrom(
            final @Nonnull Iterable<? extends ReactiveProperty<? extends T>> properties)
    {
        Objects.requireNonNull(properties, "Source properties cannot be null");

        final List<? extends ReactiveProperty<? extends T>> propertyList =
                StreamSupport
                        .stream(properties.spliterator(), false)
                        .collect(Collectors.toList());

        if (propertyList.size() == 0)
        {
            throw new IllegalArgumentException("Source properties cannot be empty");
        }

        propertyList.forEach(property ->
                Objects.requireNonNull(properties, "Source properties cannot contain null"));

        return createPropertyFrom(Observable.merge(
                propertyList.stream()
                        .map(ReactiveProperty::asObservable)
                        .collect(Collectors.toList())));
    }

    /**
     * Creates new read-only property from combining given source properties with given combiner
     *
     * @param properties source properties
     * @param combiner combiner
     * @param <T> type of property value
     * @param <U> type of source property values
     * @return created read-only property
     * @throws IllegalArgumentException if source properties are empty
     */
    @Nonnull
    default <T, U> ReactiveProperty<T> createPropertyFrom(
            final @Nonnull Iterable<? extends ReactiveProperty<? extends U>> properties,
            final @Nonnull Function<? super Object[], ? extends T> combiner)
    {
        Objects.requireNonNull(properties, "Source properties cannot be null");
        Objects.requireNonNull(combiner, "Combiner cannot be null");

        final List<? extends ReactiveProperty<? extends U>> propertyList =
                StreamSupport
                        .stream(properties.spliterator(), false)
                        .collect(Collectors.toList());

        if (propertyList.size() == 0)
        {
            throw new IllegalArgumentException("Source properties cannot be empty");
        }

        propertyList.forEach(property ->
                Objects.requireNonNull(properties, "Source properties cannot contain null"));

        return createPropertyFrom(Observable.combineLatest(
                propertyList.stream()
                        .map(ReactiveProperty::asObservable)
                        .collect(Collectors.toList()),
                combiner::apply));
    }
}
