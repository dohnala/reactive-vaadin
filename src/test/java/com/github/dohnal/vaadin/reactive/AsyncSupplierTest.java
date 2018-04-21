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
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link AsyncSupplier}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous supplier specification")
public class AsyncSupplierTest
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
    @DisplayName("When new asynchronous supplier is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty
        {
            private AsyncSupplier<Void> asyncSupplier;

            @BeforeEach
            protected void create()
            {
                asyncSupplier = AsyncSupplier.create();
            }

            @Test
            @DisplayName("When run, it should return null")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncSupplier.get().get());
            }
        }

        @Nested
        @DisplayName("As empty with executor")
        class AsEmptyWithExecutor
        {
            private AsyncSupplier<Void> asyncSupplier;

            @BeforeEach
            protected void create()
            {
                asyncSupplier = AsyncSupplier.create(new TestExecutor());
            }

            @Test
            @DisplayName("When run, it should return null")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncSupplier.get().get());
            }
        }

        @Nested
        @DisplayName("From runnable")
        class FromRunnable
        {
            private Runnable runnable;
            private AsyncSupplier<Void> asyncSupplier;

            @BeforeEach
            protected void create()
            {
                runnable = Mockito.mock(Runnable.class);
                asyncSupplier = AsyncSupplier.create(runnable);

                Mockito.doNothing().when(runnable).run();
            }

            @Test
            @DisplayName("When run, runnable should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncSupplier.get().get());

                Mockito.verify(runnable).run();
            }
        }

        @Nested
        @DisplayName("From runnable with executor")
        class FromRunnableWithExecutor
        {
            private Runnable runnable;
            private AsyncSupplier<Void> asyncSupplier;

            @BeforeEach
            protected void create()
            {
                runnable = Mockito.mock(Runnable.class);
                asyncSupplier = AsyncSupplier.create(runnable, new TestExecutor());

                Mockito.doNothing().when(runnable).run();
            }

            @Test
            @DisplayName("When run, runnable should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncSupplier.get().get());

                Mockito.verify(runnable).run();
            }
        }

        @Nested
        @DisplayName("From supplier")
        class FromSupplier
        {
            protected final Integer RESULT = 5;

            private Supplier<Integer> supplier;
            private AsyncSupplier<Integer> asyncSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                supplier = Mockito.mock(Supplier.class);
                asyncSupplier = AsyncSupplier.create(supplier);

                Mockito.when(supplier.get()).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, supplier should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncSupplier.get().get());

                Mockito.verify(supplier).get();
            }
        }

        @Nested
        @DisplayName("From supplier with executor")
        class FromSupplierWithExecutor
        {
            protected final Integer RESULT = 5;

            private Supplier<Integer> supplier;
            private AsyncSupplier<Integer> asyncSupplier;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                supplier = Mockito.mock(Supplier.class);
                asyncSupplier = AsyncSupplier.create(supplier, new TestExecutor());

                Mockito.when(supplier.get()).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, supplier should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncSupplier.get().get());

                Mockito.verify(supplier).get();
            }
        }
    }
}
