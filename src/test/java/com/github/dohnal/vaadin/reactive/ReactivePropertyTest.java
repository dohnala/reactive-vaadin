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

import com.github.dohnal.vaadin.reactive.property.EmptyPropertySpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromObservableSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromPropertiesSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromPropertiesWithCombinerSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromPropertySpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromPropertyWithFunctionSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromTwoPropertiesSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromTwoPropertiesWithCombinerSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyWithValueSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveProperty}
 *
 * @author dohnal
 */
@DisplayName("Reactive property specification")
public class ReactivePropertyTest implements
        EmptyPropertySpecification,
        PropertyWithValueSpecification,
        PropertyFromObservableSpecification,
        PropertyFromPropertySpecification,
        PropertyFromPropertyWithFunctionSpecification,
        PropertyFromTwoPropertiesSpecification,
        PropertyFromTwoPropertiesWithCombinerSpecification,
        PropertyFromPropertiesSpecification,
        PropertyFromPropertiesWithCombinerSpecification
{
    @Nested
    @DisplayName("When new reactive property is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty extends WhenCreateEmptySpecification {}

        @Nested
        @DisplayName("From default value")
        class WithValue extends WhenCreateWithValueSpecification {}

        @Nested
        @DisplayName("From observable")
        class FromObservable extends WhenCreateFromObservableSpecification {}

        @Nested
        @DisplayName("From empty property")
        class FromEmptyProperty extends WhenCreateFromEmptyPropertySpecification {}

        @Nested
        @DisplayName("From property with default value")
        class FromPropertyWithValue extends WhenCreateFromPropertyWithValueSpecification {}

        @Nested
        @DisplayName("From property with function")
        class FromPropertyWithFunction extends WhenCreateFromPropertyWithFunctionSpecification {}

        @Nested
        @DisplayName("From two properties")
        class FromTwoProperties extends WhenCreateFromTwoPropertiesSpecification {}

        @Nested
        @DisplayName("From two properties with combiner")
        class FromTwoPropertiesWithCombiner extends WhenCreateFromTwoPropertiesWithCombinerSpecification {}

        @Nested
        @DisplayName("From no properties")
        class FromNoProperties extends WhenCreateFromNoPropertiesSpecification {}

        @Nested
        @DisplayName("From properties")
        class FromProperties extends WhenCreateFromPropertiesSpecification {}

        @Nested
        @DisplayName("From no properties with combiner")
        class FromNoPropertiesWithCombiner extends WhenCreateFromNoPropertiesWithCombinerSpecification {}

        @Nested
        @DisplayName("From properties with combiner")
        class FromPropertiesWithCombiner extends WhenCreateFromPropertiesWithCombinerSpecification {}
    }
}
