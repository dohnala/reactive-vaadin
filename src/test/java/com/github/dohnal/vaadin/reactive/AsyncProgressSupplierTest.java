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
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link AsyncProgressSupplier}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous progress supplier specification")
public class AsyncProgressSupplierTest
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
    @DisplayName("When new asynchronous progress supplier is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("From consumer")
        class FromConsumer
        {
            private Progress progress;
            private Consumer<Progress> consumer;
            private AsyncProgressSupplier<Void> asyncProgressSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                consumer = Mockito.mock(Consumer.class);
                asyncProgressSupplier = AsyncProgressSupplier.create(consumer);

                Mockito.doNothing().when(consumer).accept(progress);
            }

            @Test
            @DisplayName("When run, consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressSupplier.apply(progress).get());

                Mockito.verify(consumer).accept(progress);
            }
        }

        @Nested
        @DisplayName("From consumer with executor")
        class FromConsumerWithExecutor
        {
            private Progress progress;
            private Consumer<Progress> consumer;
            private AsyncProgressSupplier<Void> asyncProgressSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                consumer = Mockito.mock(Consumer.class);
                asyncProgressSupplier = AsyncProgressSupplier.create(consumer, new TestExecutor());

                Mockito.doNothing().when(consumer).accept(progress);
            }

            @Test
            @DisplayName("When run, consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressSupplier.apply(progress).get());

                Mockito.verify(consumer).accept(progress);
            }
        }

        @Nested
        @DisplayName("From function")
        class FromFunction
        {
            protected final Integer RESULT = 7;

            private Progress progress;
            private Function<Progress, Integer> function;
            private AsyncProgressSupplier<Integer> asyncProgressSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                function = Mockito.mock(Function.class);
                asyncProgressSupplier = AsyncProgressSupplier.create(function);

                Mockito.when(function.apply(progress)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressSupplier.apply(progress).get());

                Mockito.verify(function).apply(progress);
            }
        }

        @Nested
        @DisplayName("From function with executor")
        class FromFunctionWithExecutor
        {
            protected final Integer RESULT = 7;

            private Progress progress;
            private Function<Progress, Integer> function;
            private AsyncProgressSupplier<Integer> asyncProgressSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                function = Mockito.mock(Function.class);
                asyncProgressSupplier = AsyncProgressSupplier.create(function, new TestExecutor());

                Mockito.when(function.apply(progress)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressSupplier.apply(progress).get());

                Mockito.verify(function).apply(progress);
            }
        }
    }
}
