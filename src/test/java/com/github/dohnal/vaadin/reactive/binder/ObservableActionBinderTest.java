package com.github.dohnal.vaadin.reactive.binder;

import com.github.dohnal.vaadin.reactive.Action;
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
 * Tests for {@link ObservableActionBinder}
 *
 * @author dohnal
 */
@DisplayName("Binding observable to action")
public class ObservableActionBinderTest implements ReactiveBinder
{
    @Nested
    @DisplayName("After bind observable to action")
    class AfterBindObservableToProperty
    {
        private TestScheduler testScheduler;
        private TestSubject<Integer> testSubject;
        private Action<Integer> action;
        private ObservableActionBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindObservableToAction()
        {
            testScheduler = Schedulers.test();
            testSubject = TestSubject.create(testScheduler);
            action = Mockito.mock(Action.class);

            binder = whenChanged(testSubject).then(action);
        }

        @Test
        @DisplayName("Action should not be run")
        public void testAction()
        {
            Mockito.verify(action, Mockito.never()).call(Mockito.any());
        }

        @Nested
        @DisplayName("After source observable emits value")
        class AfterSourceObservableEmitsValue
        {
            @Test
            @DisplayName("Action should be run with correct parameter")
            public void testValue()
            {
                testSubject.onNext(5);
                testScheduler.triggerActions();

                Mockito.verify(action).call(5);
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindObservableFromAction()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source observable emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Action should not be run")
                public void testAction()
                {
                    testSubject.onNext(5);
                    testScheduler.triggerActions();

                    Mockito.verify(action, Mockito.never()).call(Mockito.any());
                }
            }
        }
    }

    @Nested
    @DisplayName("After bind empty property to action")
    class AfterBindEmptyPropertyToProperty
    {
        private ReactiveProperty<Integer> property;
        private Action<Integer> action;
        private ObservableActionBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindPropertyToAction()
        {
            property = ReactiveProperty.empty();
            action = Mockito.mock(Action.class);

            binder = whenChanged(property).then(action);
        }

        @Test
        @DisplayName("Action should not be run")
        public void testAction()
        {
            Mockito.verify(action, Mockito.never()).call(Mockito.any());
        }

        @Nested
        @DisplayName("After property emits value")
        class AfterPropertyEmitsValue
        {
            @Test
            @DisplayName("Action should be run with correct parameter")
            public void testValue()
            {
                property.setValue(5);

                Mockito.verify(action).call(5);
            }
        }

        @Nested
        @DisplayName("After unbind")
        class AfterUnbind
        {
            @BeforeEach
            void unbindPropertyFromAction()
            {
                binder.unbind();
            }

            @Nested
            @DisplayName("After source property emits value")
            class AfterSourceObservableEmitsValue
            {
                @Test
                @DisplayName("Action should not be run")
                public void testAction()
                {
                    property.setValue(5);

                    Mockito.verify(action, Mockito.never()).call(Mockito.any());
                }
            }
        }
    }

    @Nested
    @DisplayName("After bind property with default value to action")
    class AfterBindPropertyWithDefaultValueToProperty
    {
        private ReactiveProperty<Integer> property;
        private Action<Integer> action;
        private ObservableActionBinder<Integer> binder;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void bindPropertyToAction()
        {
            property = ReactiveProperty.withValue(5);
            action = Mockito.mock(Action.class);

            binder = whenChanged(property).then(action);
        }

        @Test
        @DisplayName("Action should be run with default value as parameter")
        public void testValue()
        {
            Mockito.verify(action).call(5);
        }
    }
}
