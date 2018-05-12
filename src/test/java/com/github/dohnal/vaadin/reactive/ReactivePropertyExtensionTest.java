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

import com.github.dohnal.vaadin.reactive.property.create.CreateEmptySpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromObservableSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromPropertiesSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromPropertiesWithCombinerSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromPropertySpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromPropertyWithFunctionSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromTwoPropertiesSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateFromTwoPropertiesWithCombinerSpecification;
import com.github.dohnal.vaadin.reactive.property.create.CreateWithValueSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveProperty}
 *
 * @author dohnal
 */
@DisplayName("Reactive property extension specification")
public class ReactivePropertyExtensionTest implements
        CreateEmptySpecification,
        CreateWithValueSpecification,
        CreateFromObservableSpecification,
        CreateFromPropertySpecification,
        CreateFromPropertyWithFunctionSpecification,
        CreateFromTwoPropertiesSpecification,
        CreateFromTwoPropertiesWithCombinerSpecification,
        CreateFromPropertiesSpecification,
        CreateFromPropertiesWithCombinerSpecification
{
    @Nested
    @DisplayName("When new reactive property is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty extends AbstractCreateEmptySpecification {}

        @Nested
        @DisplayName("With default value")
        class WithValue extends AbstractCreateWithValueSpecification {}

        @Nested
        @DisplayName("From observable")
        class FromObservable extends AbstractCreateFromObservableSpecification {}

        @Nested
        @DisplayName("From empty property")
        class FromEmptyProperty extends AbstractCreateFromEmptyPropertySpecification {}

        @Nested
        @DisplayName("From property with default value")
        class FromPropertyWithValue extends AbstractCreateFromPropertyWithValueSpecification {}

        @Nested
        @DisplayName("From property with function")
        class FromPropertyWithFunction extends AbstractCreateFromPropertyWithFunctionSpecification {}

        @Nested
        @DisplayName("From two properties")
        class FromTwoProperties extends AbstractCreateFromTwoPropertiesSpecification {}

        @Nested
        @DisplayName("From two properties with combiner")
        class FromTwoPropertiesWithCombiner extends AbstractCreateFromTwoPropertiesWithCombinerSpecification {}

        @Nested
        @DisplayName("From no properties")
        class FromNoProperties extends AbstractCreateFromNoPropertiesSpecification {}

        @Nested
        @DisplayName("From properties")
        class FromProperties extends AbstractCreateFromPropertiesSpecification {}

        @Nested
        @DisplayName("From no properties with combiner")
        class FromNoPropertiesWithCombiner extends AbstractCreateFromNoPropertiesWithCombinerSpecification {}

        @Nested
        @DisplayName("From properties with combiner")
        class FromPropertiesWithCombiner extends AbstractCreateFromPropertiesWithCombinerSpecification {}
    }
}
