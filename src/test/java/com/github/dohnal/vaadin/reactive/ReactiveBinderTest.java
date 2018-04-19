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
