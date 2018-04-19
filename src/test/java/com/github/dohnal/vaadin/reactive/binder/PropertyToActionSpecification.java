package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.Action;
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
 * Specification for binding property to action by {@link ReactiveBinder#whenChanged(IsObservable)}
 *
 * @author dohnal
 */
public interface PropertyToActionSpecification extends BaseActionBinderSpecification
{
    abstract class AfterBindEmptyPropertyToActionSpecification implements ReactiveBinder
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
        @DisplayName("When source property emits value")
        class WhenSourcePropertyEmitsValue extends WhenSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public Action<Integer> getAction()
            {
                return action;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                property.setValue(value);
            }
        }

        @Nested
        @DisplayName("When source property emits value after property is unbound from action")
        class WhenSourcePropertyEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
        {
            @Nonnull
            @Override
            public Action<Integer> getAction()
            {
                return action;
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
                property.setValue(value);
            }
        }
    }

    abstract class AfterBindPropertyWithValueToActionSpecification implements ReactiveBinder
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
        public void testAction()
        {
            Mockito.verify(action).call(5);
        }

        @Nested
        @DisplayName("When source property emits value")
        class WhenSourcePropertyEmitsValue extends WhenSourceEmitsValueSpecification
        {
            @Nonnull
            @Override
            public Action<Integer> getAction()
            {
                return action;
            }

            @Override
            protected void emitValue(final @Nonnull Integer value)
            {
                property.setValue(value);
            }
        }

        @Nested
        @DisplayName("When source property emits value after property is unbound from action")
        class WhenSourcePropertyEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
        {
            @Nonnull
            @Override
            public Action<Integer> getAction()
            {
                return action;
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
                property.setValue(value);
            }
        }
    }
}
