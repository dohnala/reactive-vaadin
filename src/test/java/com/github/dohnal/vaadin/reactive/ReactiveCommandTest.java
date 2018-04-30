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

import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromRunnableSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncCommandFromSupplierSpecification;
import com.github.dohnal.vaadin.reactive.command.async.AsyncEmptyCommandSpecification;
import com.github.dohnal.vaadin.reactive.command.composite.CompositeCommandSpecification;
import com.github.dohnal.vaadin.reactive.command.progress.ProgressCommandFromBiConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.progress.ProgressCommandFromBiFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.progress.ProgressCommandFromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.progress.ProgressCommandFromFunctionSpecification;
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
            AsyncCommandFromFunctionSpecification
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
    @DisplayName("When new progress command is created")
    class WhenCreateProgress implements
            ProgressCommandFromConsumerSpecification,
            ProgressCommandFromFunctionSpecification,
            ProgressCommandFromBiFunctionSpecification,
            ProgressCommandFromBiConsumerSpecification
    {
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
        @DisplayName("From bi-consumer")
        class FromBiConsumer extends WhenCreateFromBiConsumerSpecification {}

        @Nested
        @DisplayName("From bi-consumer supplier with CanExecute")
        class FromBiConsumerWithCanExecute extends WhenCreateFromBiConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From bi-function")
        class FromBiFunction extends WhenCreateFromBiFunctionSpecification {}

        @Nested
        @DisplayName("From bi-function with CanExecute")
        class FromBiFunctionWithCanExecute extends WhenCreateFromBiFunctionWithCanExecuteSpecification {}
    }

    @Nested
    @DisplayName("When new composite command is created")
    class WhenCreateComposite implements CompositeCommandSpecification
    {
        @Nested
        @DisplayName("From no child commands")
        class FromNoChildCommands extends WhenCreateFromNoCommandsSpecification {}

        @Nested
        @DisplayName("From child commands with no input")
        class FromChildCommandsWithNoInput extends WhenCreateFromCommandsWithNoInputSpecification {}

        @Nested
        @DisplayName("From child commands with input")
        class FromChildCommandsWithInput extends WhenCreateFromCommandsWithInputSpecification {}

        @Nested
        @DisplayName("From child commands with CanExecute")
        class FromChildCommandsWithCanExecute extends WhenCreateFromCommandsWithCanExecuteSpecification {}
    }
}
