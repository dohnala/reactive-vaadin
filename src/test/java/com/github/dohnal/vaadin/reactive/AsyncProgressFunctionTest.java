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

package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link AsyncProgressFunction}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous progress function specification")
public class AsyncProgressFunctionTest
{
    /**
     * Synchronized implementation of {@link Executor} for testing asynchronous commands
     */
    class TestExecutor implements Executor
    {
        @Override
        public void execute(final @Nonnull Runnable runnable)
        {
            runnable.run();
        }
    }

    @Nested
    @DisplayName("When new asynchronous progress function is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("From bi-consumer")
        class FromBiConsumer
        {
            protected final Integer INPUT = 5;

            private ProgressContext progressContext;
            private BiConsumer<ProgressContext, Integer> consumer;
            private AsyncProgressFunction<Integer, Void> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progressContext = Mockito.mock(ProgressContext.class);
                consumer = Mockito.mock(BiConsumer.class);
                asyncProgressFunction = AsyncProgressFunction.create(consumer);

                Mockito.doNothing().when(consumer).accept(progressContext, INPUT);
            }

            @Test
            @DisplayName("When run, bi-consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressFunction.apply(progressContext, INPUT).get());

                Mockito.verify(consumer).accept(progressContext, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-consumer with executor")
        class FromBiConsumerWithExecutor
        {
            protected final Integer INPUT = 5;

            private ProgressContext progressContext;
            private BiConsumer<ProgressContext, Integer> consumer;
            private AsyncProgressFunction<Integer, Void> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progressContext = Mockito.mock(ProgressContext.class);
                consumer = Mockito.mock(BiConsumer.class);
                asyncProgressFunction = AsyncProgressFunction.create(consumer, new TestExecutor());

                Mockito.doNothing().when(consumer).accept(progressContext, INPUT);
            }

            @Test
            @DisplayName("When run, bi-consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressFunction.apply(progressContext, INPUT).get());

                Mockito.verify(consumer).accept(progressContext, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-function")
        class FromBiFunction
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private ProgressContext progressContext;
            private BiFunction<ProgressContext, Integer, Integer> function;
            private AsyncProgressFunction<Integer, Integer> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progressContext = Mockito.mock(ProgressContext.class);
                function = Mockito.mock(BiFunction.class);
                asyncProgressFunction = AsyncProgressFunction.create(function);

                Mockito.when(function.apply(progressContext, INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, bi-function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressFunction.apply(progressContext, INPUT).get());

                Mockito.verify(function).apply(progressContext, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-function with executor")
        class FromBiFunctionWithExecutor
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private ProgressContext progressContext;
            private BiFunction<ProgressContext, Integer, Integer> function;
            private AsyncProgressFunction<Integer, Integer> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progressContext = Mockito.mock(ProgressContext.class);
                function = Mockito.mock(BiFunction.class);
                asyncProgressFunction = AsyncProgressFunction.create(function, new TestExecutor());

                Mockito.when(function.apply(progressContext, INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, bi-function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressFunction.apply(progressContext, INPUT).get());

                Mockito.verify(function).apply(progressContext, INPUT);
            }
        }
    }
}
