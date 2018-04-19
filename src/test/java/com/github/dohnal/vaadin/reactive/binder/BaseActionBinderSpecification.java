package com.github.dohnal.vaadin.reactive.binder;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.Action;
import com.github.dohnal.vaadin.reactive.Disposable;
import com.github.dohnal.vaadin.reactive.ReactiveBinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Base specifications for {@link ReactiveBinder} which binds source to {@link Action}
 *
 * @author dohnal
 */
public interface BaseActionBinderSpecification
{
    /**
     * Base interface for tests which needs action
     *
     * @param <T> type of action
     */
    interface RequireAction<T>
    {
        @Nonnull
        Action<T> getAction();
    }

    /**
     * Base interface for tests which needs disposable
     */
    interface RequireDisposable
    {
        @Nonnull
        Disposable getDisposable();
    }

    /**
     * Specification that tests behavior of action bound to the source when source emits value
     */
    abstract class WhenSourceEmitsValueSpecification implements RequireAction<Integer>
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        @Test
        @SuppressWarnings("unchecked")
        @DisplayName("Action should be run with correct value as parameter")
        public void testPropertyValue()
        {
            Mockito.clearInvocations(getAction());

            emitValue(7);

            Mockito.verify(getAction()).call(7);
        }
    }

    /**
     * Specification that tests behavior of action unbound from the source when source emits value
     */
    abstract class WhenSourceEmitsValueAfterUnbindSpecification implements RequireAction<Integer>, RequireDisposable
    {
        protected abstract void emitValue(final @Nonnull Integer value);

        @BeforeEach
        void unbind()
        {
            getDisposable().unbind();
        }

        @Test
        @SuppressWarnings("unchecked")
        @DisplayName("Action should not be run")
        public void testPropertyValue()
        {
            Mockito.clearInvocations(getAction());

            emitValue(7);

            Mockito.verify(getAction(), Mockito.never()).call(Mockito.any());
        }
    }
}
