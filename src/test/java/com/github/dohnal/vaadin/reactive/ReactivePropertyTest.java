package com.github.dohnal.vaadin.reactive;

import com.github.dohnal.vaadin.reactive.property.EmptyPropertySpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromObservableSpecification;
import com.github.dohnal.vaadin.reactive.property.PropertyFromPropertySpecification;
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
        PropertyFromPropertySpecification
{
    @Nested
    @DisplayName("When new reactive property is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty extends WhenCreateEmptySpecification {}

        @Nested
        @DisplayName("With default value")
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
    }
}
