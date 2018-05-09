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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author dohnal
 */
public interface CreateSpecification extends BaseCommandSpecification
{
    /**
     * Specification that tests behavior of command after it is created
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    abstract class AbstractCreateSpecification<T, R> implements RequireCommand<T, R>
    {
        @Test
        @DisplayName("Result observable should not emit any value")
        public void testResult()
        {
            getCommand().getResult().test().assertNoValues();
        }

        @Test
        @DisplayName("Error observable should not emit any value")
        public void testError()
        {
            getCommand().getError().test().assertNoValues();
        }

        @Test
        @DisplayName("CanExecute observable should emit true")
        public void testCanExecute()
        {
            getCommand().canExecute().test().assertValue(true);
        }

        @Test
        @DisplayName("IsExecuting observable should emit false")
        public void testIsExecuting()
        {
            getCommand().isExecuting().test().assertValue(false);
        }

        @Test
        @DisplayName("ExecutionCount observable should emit 0")
        public void testExecutionCount()
        {
            getCommand().getExecutionCount().test().assertValue(0);
        }

        @Test
        @DisplayName("HasBeenExecuted observable should emit false")
        public void testHasBeenExecuted()
        {
            getCommand().hasBeenExecuted().test().assertValue(false);
        }

        @Test
        @DisplayName("Progress observable should emit 0")
        public void testProgress()
        {
            getCommand().getProgress().test().assertValue(0.0f);
        }
    }
}
