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

import com.github.dohnal.vaadin.reactive.binder.ObservableToActionSpecification;
import com.github.dohnal.vaadin.reactive.binder.ObservableToPropertySpecification;
import com.github.dohnal.vaadin.reactive.binder.PropertyToActionSpecification;
import com.github.dohnal.vaadin.reactive.binder.PropertyToPropertySpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveBinder}
 *
 * @author dohnal
 */
@DisplayName("Reactive binder specification")
public class ReactiveBinderTest implements
        ObservableToPropertySpecification,
        PropertyToPropertySpecification,
        ObservableToActionSpecification,
        PropertyToActionSpecification

{
    @Nested
    @DisplayName("When observable is bound to property")
    class WhenBindObservableToProperty extends WhenBindObservableToPropertySpecification {}

    @Nested
    @DisplayName("When empty property is bound to property")
    class WhenBindEmptyPropertyToProperty extends WhenBindEmptyPropertyToPropertySpecification {}

    @Nested
    @DisplayName("When property with value is bound to property")
    class WhenBindPropertyWithValueToProperty extends WhenBindPropertyWithValueToPropertySpecification {}

    @Nested
    @DisplayName("When observable is bound to action")
    class WhenBindObservableToAction extends WhenBindObservableToActionSpecification {}

    @Nested
    @DisplayName("When empty property is bound to action")
    class AfterBindEmptyPropertyToAction extends AfterBindEmptyPropertyToActionSpecification {}

    @Nested
    @DisplayName("When property with value is bound to action")
    class AfterBindPropertyWithValueToAction extends AfterBindPropertyWithValueToActionSpecification {}
}
