package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
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
 * Tests for {@link AsyncFunction}
 *
 * @author dohnal
 */
@DisplayName("Asynchronous function specification")
public class AsyncFunctionTest
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
    @DisplayName("When new asynchronous function is created")
    class WhenCreate
    {
        @Nested
        @DisplayName("From asynchronous supplier")
        class FromAsyncSupplier
        {
            protected final Integer RESULT = 5;

            private AsyncSupplier<Integer> asyncSupplier;
            private AsyncFunction<Void, Integer> asyncFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                asyncSupplier = Mockito.mock(AsyncSupplier.class);
                asyncFunction = AsyncFunction.create(asyncSupplier);

                Mockito.when(asyncSupplier.get()).thenReturn(CompletableFuture.completedFuture(RESULT));
            }

            @Test
            @DisplayName("When run, asynchronous supplier should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncFunction.apply(null).get());

                Mockito.verify(asyncSupplier).get();
            }
        }

        @Nested
        @DisplayName("From consumer")
        class FromConsumer
        {
            protected final Integer INPUT = 5;

            private Consumer<Integer> consumer;
            private AsyncFunction<Integer, Void> asyncFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                consumer = Mockito.mock(Consumer.class);
                asyncFunction = AsyncFunction.create(consumer);

                Mockito.doNothing().when(consumer).accept(INPUT);
            }

            @Test
            @DisplayName("When run, consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncFunction.apply(INPUT).get());

                Mockito.verify(consumer).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("From consumer with executor")
        class FromConsumerWithExecutor
        {
            protected final Integer INPUT = 5;

            private Consumer<Integer> consumer;
            private AsyncFunction<Integer, Void> asyncFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                consumer = Mockito.mock(Consumer.class);
                asyncFunction = AsyncFunction.create(consumer, new TestExecutor());

                Mockito.doNothing().when(consumer).accept(INPUT);
            }

            @Test
            @DisplayName("When run, consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncFunction.apply(INPUT).get());

                Mockito.verify(consumer).accept(INPUT);
            }
        }

        @Nested
        @DisplayName("From function")
        class FromFunction
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private Function<Integer, Integer> function;
            private AsyncFunction<Integer, Integer> asyncFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                function = Mockito.mock(Function.class);
                asyncFunction = AsyncFunction.create(function);

                Mockito.when(function.apply(INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncFunction.apply(INPUT).get());

                Mockito.verify(function).apply(INPUT);
            }
        }

        @Nested
        @DisplayName("From function with executor")
        class FromFunctionWithExecutor
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private Function<Integer, Integer> function;
            private AsyncFunction<Integer, Integer> asyncFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                function = Mockito.mock(Function.class);
                asyncFunction = AsyncFunction.create(function, new TestExecutor());

                Mockito.when(function.apply(INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncFunction.apply(INPUT).get());

                Mockito.verify(function).apply(INPUT);
            }
        }
    }
}
