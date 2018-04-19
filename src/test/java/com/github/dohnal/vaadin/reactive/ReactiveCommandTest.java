package com.github.dohnal.vaadin.reactive;

import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromAsyncFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromAsyncSupplierSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromRunnableSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromSupplierSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncEmptyCommandSpecification;
import com.github.dohnal.vaadin.reactive.command.sync.SyncCommandFromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.sync.SyncCommandFromFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.sync.SyncCommandFromRunnableSpecification;
import com.github.dohnal.vaadin.reactive.command.sync.SyncCommandFromSupplierSpecification;
import com.github.dohnal.vaadin.reactive.command.sync.SyncEmptyCommandSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveCommand}
 *
 * @author dohnal
 */
@DisplayName("Reactive command specification")
public class ReactiveCommandTest
{
    @Nested
    @DisplayName("When new synchronous command is created")
    class WhenCreateSync implements
            SyncEmptyCommandSpecification,
            SyncCommandFromRunnableSpecification,
            SyncCommandFromSupplierSpecification,
            SyncCommandFromConsumerSpecification,
            SyncCommandFromFunctionSpecification
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty extends WhenCreateEmptySpecification {}

        @Nested
        @DisplayName("As empty with CanExecute")
        class AsEmptyWithCanExecute extends WhenCreateEmptyWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From runnable")
        class FromRunnable extends WhenCreateFromRunnableSpecification {}

        @Nested
        @DisplayName("From runnable with CanExecute")
        class FromRunnableWithCanExecute extends WhenCreateFromRunnableWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From supplier")
        class FromSupplier extends WhenCreateFromSupplierSpecification {}

        @Nested
        @DisplayName("From supplier with CanExecute")
        class FromSupplierWithCanExecute extends WhenCreateFromSupplierWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From consumer")
        class FromConsumer extends WhenCreateFromConsumerSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute")
        class FromConsumerWithCanExecute extends WhenCreateFromConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From function")
        class FromFunction extends WhenCreateFromFunctionSpecification {}

        @Nested
        @DisplayName("From function with CanExecute")
        class FromFunctionWithCanExecute extends WhenCreateFromFunctionWithCanExecuteSpecification {}
    }

    @Nested
    @DisplayName("When new asynchronous command is created")
    class WhenCreateAsync implements
            AsyncEmptyCommandSpecification,
            AsyncCommandFromRunnableSpecification,
            AsyncCommandFromSupplierSpecification,
            AsyncCommandFromConsumerSpecification,
            AsyncCommandFromFunctionSpecification,
            AsyncCommandFromAsyncSupplierSpecification,
            AsyncCommandFromAsyncFunctionSpecification
    {
        @Nested
        @DisplayName("As empty")
        class AsEmpty extends WhenCreateEmptySpecification {}

        @Nested
        @DisplayName("As empty with CanExecute")
        class AsEmptyWithCanExecute extends WhenCreateEmptyWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From runnable")
        class FromRunnable extends WhenCreateFromRunnableSpecification {}

        @Nested
        @DisplayName("From runnable with CanExecute")
        class FromRunnableWithCanExecute extends WhenCreateFromRunnableWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From supplier")
        class FromSupplier extends WhenCreateFromSupplierSpecification {}

        @Nested
        @DisplayName("From supplier with CanExecute")
        class FromSupplierWithCanExecute extends WhenCreateFromSupplierWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From consumer")
        class FromConsumer extends WhenCreateFromConsumerSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute")
        class FromConsumerWithCanExecute extends WhenCreateFromConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From function")
        class FromFunction extends WhenCreateFromFunctionSpecification {}

        @Nested
        @DisplayName("From function with CanExecute")
        class FromFunctionWithCanExecute extends WhenCreateFromFunctionWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From asynchronous supplier")
        class FromAsyncSupplier extends WhenCreateFromAsyncSupplierSpecification {}

        @Nested
        @DisplayName("From asynchronous supplier with CanExecute")
        class FromAsyncSupplierWithCanExecute extends WhenCreateFromAsyncSupplierWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From asynchronous function")
        class FromAsyncFunction extends WhenCreateFromAsyncFunctionSpecification {}

        @Nested
        @DisplayName("From asynchronous function with CanExecute")
        class FromAsyncFunctionWithCanExecute extends WhenCreateFromAsyncFunctionWithCanExecuteSpecification {}
    }
}
