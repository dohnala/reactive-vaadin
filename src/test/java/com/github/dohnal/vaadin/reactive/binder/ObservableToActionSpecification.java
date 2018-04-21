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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

/**
 * Specification for binding observable to action by {@link ReactiveBinder#whenChanged(Observable)}
 *
 * @author dohnal
 */
public interface ObservableToActionSpecification extends BaseActionBinderSpecification
{
    abstract class WhenBindObservableToActionSpecification implements ReactiveBinder
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
        @DisplayName("When source observable emits value")
        class WhenSourceObservableEmitsValue extends WhenSourceEmitsValueSpecification
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
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }

        @Nested
        @DisplayName("When source observable emits value after observable is unbound from action")
        class WhenSourceObservableEmitsValueAfterUnbind extends WhenSourceEmitsValueAfterUnbindSpecification
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
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }
        }
    }
}
