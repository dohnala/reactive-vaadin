package com.github.dohnal.vaadin.reactive.binder;

import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Tests for {@link ObservablePropertyBinder}
 *
 * @author dohnal
 */
@DisplayName("Binding observable to property")
public class ObservablePropertyBinderTest implements ReactiveBinder
{
    @Nested
    @DisplayName("After bind observable to property")
    class AfterBindObservableToProperty
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindObservableToProperty()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            property = Mockito.mock(ReactiveProperty.class);

            binder = bind(testSubject).to(property);
        }

        @Test
        @DisplayName("Property value should not be set")
        public void testPropertyValue()
        {
            Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
        }

        @Nested
        @DisplayName("After source observable emits value")
        class AfterSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                testSubject.onNext(5);
                testScheduler.triggerActions();

                Mockito.verify(property).setValue(5);
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindObservableFromProperty()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source observable emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Property value should not be set")
                public void testPropertyValue()
                {
                    testSubject.onNext(5);
                    testScheduler.triggerActions();

                    Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
                }
            }
        }
    }

    @Nested
    @DisplayName("After bind property to property")
    class AfterBindPropertyToProperty
    {
        private ReactiveProperty<Integer> sourceProperty;
        private ReactiveProperty<Integer> property;
        private ObservablePropertyBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindPropertyToProperty()
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
        @DisplayName("After source property emits value")
        class AfterSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Property value should be set with correct value")
            public void testPropertyValue()
            {
                sourceProperty.setValue(5);

                Mockito.verify(property).setValue(5);
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindPropertyFromProperty()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source property emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Property value should be set with correct value")
                public void testPropertyValue()
                {
                    sourceProperty.setValue(5);

                    Mockito.verify(property, Mockito.never()).setValue(Mockito.any());
                }
            }
        }
    }
}
