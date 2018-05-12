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

import com.github.dohnal.vaadin.reactive.binder.ObservableBinderSpecification;
import com.github.dohnal.vaadin.reactive.binder.ObservablePropertyBinderSpecification;
import com.github.dohnal.vaadin.reactive.binder.PropertyBinderSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveBinderExtension}
 *
 * @author dohnal
 */
@DisplayName("Reactive binder extension specification")
public class ReactiveBinderExtensionTest implements
        PropertyBinderSpecification,
        ObservablePropertyBinderSpecification,
        ObservableBinderSpecification
{
    @Nested
    @DisplayName("When property is bound")
    class WhenBindProperty
    {
        @Nested
        @DisplayName("to observable")
        class ToObservable extends WhenBindPropertyToObservableSpecification {}

        @Nested
        @DisplayName("to isObservable")
        class ToIsObservable extends WhenBindPropertyToIsObservableSpecification {}
    }

    @Nested
    @DisplayName("When observable property is bound")
    class WhenBindObservableProperty
    {
        @Nested
        @DisplayName("to observable")
        class ToObservable extends WhenBindObservablePropertyToObservableSpecification {}

        @Nested
        @DisplayName("to isObservable")
        class ToIsObservable extends WhenBindObservablePropertyToIsObservableSpecification {}

        @Nested
        @DisplayName("to observable property")
        class ToObservableProperty extends WhenBindObservablePropertyToObservablePropertySpecification {}
    }

    @Nested
    @DisplayName("When observable is bound")
    class WhenBindObservable
    {
        @Nested
        @DisplayName("to consumer")
        class ToConsumer extends WhenBindObservableToConsumerSpecification {}

        @Nested
        @DisplayName("to runnable")
        class ToRunnable extends WhenBindObservableToRunnableSpecification {}

        @Nested
        @DisplayName("to observable function")
        class ToObservableFunction extends WhenBindObservableToObservableFunctionSpecification {}
    }

    @Nested
    @DisplayName("When isObservable is bound")
    class WhenBindIsObservable
    {
        @Nested
        @DisplayName("to consumer")
        class ToConsumer extends WhenBindIsObservableToConsumerSpecification {}

        @Nested
        @DisplayName("to runnable")
        class ToRunnable extends WhenBindIsObservableToRunnableSpecification {}

        @Nested
        @DisplayName("to observable function")
        class ToObservableFunction extends WhenBindIsObservableToObservableFunctionSpecification {}
    }
}
