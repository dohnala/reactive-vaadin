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

package com.github.dohnal.vaadin.reactive.command;

import io.reactivex.observers.TestObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author dohnal
 */
public interface CompositeCanExecuteEmitsValueSpecification extends CanExecuteEmitsValueSpecification
{
    /**
     * Specification that tests behavior of composite command after it is created with custom CanExecute
     * which controls command executability
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractCompositeCanExecuteEmitsValueSpecification<T, R> extends
            AbstractCanExecuteEmitsValueSpecification<T, R>
    {
        protected abstract void executeChild();

        @Nested
        @DisplayName("When child command is executed")
        class WhenExecuteChild
        {
            @Test
            @DisplayName("CanExecute observable should not emit false and then true")
            public void testCanExecute()
            {
                final TestObserver<Boolean> testObserver = getCommand().canExecute().test();

                testObserver.assertValue(true);

                executeChild();

                testObserver.assertValues(true, false, true);
            }
        }
    }
}
