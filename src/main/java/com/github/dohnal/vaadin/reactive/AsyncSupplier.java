package com.github.dohnal.vaadin.reactive;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Represents an asynchronous supplier of results.
 *
 * @param <T> type of result
 * @author dohnal
 */
public interface AsyncSupplier<T> extends Supplier<CompletableFuture<T>>
{
}
