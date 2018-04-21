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
