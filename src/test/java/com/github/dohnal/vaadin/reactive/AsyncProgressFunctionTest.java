package com.github.dohnal.vaadin.reactive;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
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
        @DisplayName("From asynchronous progress supplier")
        class FromAsyncProgressSupplier
        {
            protected final Integer RESULT = 5;

            private Progress progress;
            private AsyncProgressSupplier<Integer> asyncProgressSupplier;
            private AsyncProgressFunction<Void, Integer> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                asyncProgressSupplier = Mockito.mock(AsyncProgressSupplier.class);
                asyncProgressFunction = AsyncProgressFunction.create(asyncProgressSupplier);

                Mockito.when(asyncProgressSupplier.apply(progress))
                        .thenReturn(CompletableFuture.completedFuture(RESULT));
            }

            @Test
            @DisplayName("When run, asynchronous progress supplier should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressFunction.apply(progress, null).get());

                Mockito.verify(asyncProgressSupplier).apply(progress);
            }
        }

        @Nested
        @DisplayName("From bi-consumer")
        class FromBiConsumer
        {
            protected final Integer INPUT = 5;

            private Progress progress;
            private BiConsumer<Progress, Integer> consumer;
            private AsyncProgressFunction<Integer, Void> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                consumer = Mockito.mock(BiConsumer.class);
                asyncProgressFunction = AsyncProgressFunction.create(consumer);

                Mockito.doNothing().when(consumer).accept(progress, INPUT);
            }

            @Test
            @DisplayName("When run, bi-consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressFunction.apply(progress, INPUT).get());

                Mockito.verify(consumer).accept(progress, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-consumer with executor")
        class FromBiConsumerWithExecutor
        {
            protected final Integer INPUT = 5;

            private Progress progress;
            private BiConsumer<Progress, Integer> consumer;
            private AsyncProgressFunction<Integer, Void> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                consumer = Mockito.mock(BiConsumer.class);
                asyncProgressFunction = AsyncProgressFunction.create(consumer, new TestExecutor());

                Mockito.doNothing().when(consumer).accept(progress, INPUT);
            }

            @Test
            @DisplayName("When run, bi-consumer should be run and null should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertNull(asyncProgressFunction.apply(progress, INPUT).get());

                Mockito.verify(consumer).accept(progress, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-function")
        class FromBiFunction
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private Progress progress;
            private BiFunction<Progress, Integer, Integer> function;
            private AsyncProgressFunction<Integer, Integer> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                function = Mockito.mock(BiFunction.class);
                asyncProgressFunction = AsyncProgressFunction.create(function);

                Mockito.when(function.apply(progress, INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, bi-function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressFunction.apply(progress, INPUT).get());

                Mockito.verify(function).apply(progress, INPUT);
            }
        }

        @Nested
        @DisplayName("From bi-function with executor")
        class FromBiFunctionWithExecutor
        {
            protected final Integer INPUT = 5;
            protected final Integer RESULT = 7;

            private Progress progress;
            private BiFunction<Progress, Integer, Integer> function;
            private AsyncProgressFunction<Integer, Integer> asyncProgressFunction;

            @BeforeEach
            @SuppressWarnings("unchecked")
            protected void create()
            {
                progress = Mockito.mock(Progress.class);
                function = Mockito.mock(BiFunction.class);
                asyncProgressFunction = AsyncProgressFunction.create(function, new TestExecutor());

                Mockito.when(function.apply(progress, INPUT)).thenReturn(RESULT);
            }

            @Test
            @DisplayName("When run, bi-function should be run and correct result should be returned")
            public void testResult() throws ExecutionException, InterruptedException
            {
                assertEquals(RESULT, asyncProgressFunction.apply(progress, INPUT).get());

                Mockito.verify(function).apply(progress, INPUT);
            }
        }
    }
}
