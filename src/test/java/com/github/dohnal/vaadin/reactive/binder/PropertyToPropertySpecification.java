package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.IsObservable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Specification for binding property to property by {@link ReactiveBinder#bind(IsObservable))}
 *
 * @author dohnal
 */
public interface PropertyToPropertySpecification extends BasePropertyBinderSpecification
{
    abstract class WhenBindEmptyPropertyToPropertySpecification implements ReactiveBinder
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindEmptyPropertyToProperty()
        {
            sourceProperty = ReactiveProperty.empty();
            property = Mockito.mock(ReactiveProperty.class);

            binder = bind(sourceProperty).to(property);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
        }

        @Nested
        @DisplayName("When source property emits value")
        class WhenSourcePropertyEmitsValue extends WhenSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }

        @Nested
        @DisplayName("When source property emits value after property is unbound from property")
        class WhenSourcePropertyEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Nonnull
            @Override
            public Disposable getDisposable()
            {
                return binder;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }
    }

    abstract class WhenBindPropertyWithValueToPropertySpecification implements ReactiveBinder
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindEmptyPropertyToProperty()
        {
            sourceProperty = ReactiveProperty.withValue(5);
            property = Mockito.mock(ReactiveProperty.class);

            binder = bind(sourceProperty).to(property);
        }

        @Test
        @DisplayName("Property value should be set with default value")
        public void testPropertyValue()
        {
            Mockito.verify(property).setValue(5);
        }

        @Nested
        @DisplayName("When source property emits value")
        class WhenSourcePropertyEmitsValue extends WhenSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }

        @Nested
        @DisplayName("When source property emits value after property is unbound from property")
        class WhenSourcePropertyEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
        {
            @Nonnull
            @Override
            public ReactiveProperty<Integer> getProperty()
            {
                return property;
            }

            @Nonnull
            @Override
            public Disposable getDisposable()
            {
                return binder;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                sourceProperty.setValue(value);
            }
        }
    }
}
