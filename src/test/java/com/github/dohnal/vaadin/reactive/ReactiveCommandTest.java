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

import com.github.dohnal.vaadin.reactive.command.factory.CompositeFromCommandsSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.FromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.FromFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.FromRunnableSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.FromSupplierSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.ProgressFromBiConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.ProgressFromBiFunctionSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.ProgressFromConsumerSpecification;
import com.github.dohnal.vaadin.reactive.command.factory.ProgressFromFunctionSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests for {@link ReactiveProperty}
 *
 * @author dohnal
 */
@DisplayName("Reactive command specification")
public class ReactiveCommandTest
{
    @Nested
    @DisplayName("When new reactive command is created")
    class WhenCreate implements
            FromRunnableSpecification,
            FromSupplierSpecification,
            FromConsumerSpecification,
            FromFunctionSpecification
    {
        @Nested
        @DisplayName("From runnable")
        class FromRunnable extends AbstractFromRunnableSpecification {}

        @Nested
        @DisplayName("From runnable with scheduler")
        class FromRunnableWithScheduler extends AbstractFromRunnableWithSchedulerSpecification {}

        @Nested
        @DisplayName("From runnable with CanExecute")
        class FromRunnableWithCanExecute extends AbstractFromRunnableWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From runnable with CanExecute and scheduler")
        class FromRunnableWithCanExecuteAndScheduler extends AbstractFromRunnableWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From supplier")
        class FromSupplier extends AbstractFromSupplierSpecification {}

        @Nested
        @DisplayName("From supplier with scheduler")
        class FromSupplierWithScheduler extends AbstractFromSupplierWithSchedulerSpecification {}

        @Nested
        @DisplayName("From supplier with CanExecute")
        class FromSupplierWithCanExecute extends AbstractFromSupplierWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From supplier with CanExecute and scheduler")
        class FromSupplierWithCanExecuteAndScheduler extends AbstractFromSupplierWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From consumer")
        class FromConsumer extends AbstractFromConsumerSpecification {}

        @Nested
        @DisplayName("From consumer with scheduler")
        class FromConsumerWithScheduler extends AbstractFromConsumerWithSchedulerSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute")
        class FromConsumerWithCanExecute extends AbstractFromConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute and scheduler")
        class FromConsumerWithCanExecuteAndScheduler extends AbstractFromConsumerWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From function")
        class FromFunction extends AbstractFromFunctionSpecification {}

        @Nested
        @DisplayName("From function with scheduler")
        class FromFunctionWithScheduler extends AbstractFromFunctionWithSchedulerSpecification {}

        @Nested
        @DisplayName("From function with CanExecute")
        class FromFunctionWithCanExecute extends AbstractFromFunctionWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From function with CanExecute and scheduler")
        class FromFunctionWithCanExecuteAndScheduler extends AbstractFromFunctionWithCanExecuteAndSchedulerSpecification {}
    }

    @Nested
    @DisplayName("When new reactive progress command is created")
    class WhenCreateProgress implements
            ProgressFromConsumerSpecification,
            ProgressFromFunctionSpecification,
            ProgressFromBiConsumerSpecification,
            ProgressFromBiFunctionSpecification
    {
        @Nested
        @DisplayName("From consumer")
        class FromConsumer extends AbstractProgressFromConsumerSpecification {}

        @Nested
        @DisplayName("From consumer with scheduler")
        class FromConsumerWithScheduler extends AbstractProgressFromConsumerWithSchedulerSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute")
        class FromConsumerWithCanExecute extends AbstractProgressFromConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From consumer with CanExecute and scheduler")
        class FromConsumerWithCanExecuteAndScheduler extends AbstractProgressFromConsumerWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From function")
        class FromFunction extends AbstractProgressFromFunctionSpecification {}

        @Nested
        @DisplayName("From function with scheduler")
        class FromFunctionWithScheduler extends AbstractProgressFromFunctionWithSchedulerSpecification {}

        @Nested
        @DisplayName("From function with CanExecute")
        class FromFunctionWithCanExecute extends AbstractProgressFromFunctionWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From function with CanExecute and scheduler")
        class FromFunctionWithCanExecuteAndScheduler extends AbstractProgressFromFunctionWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From bi-consumer")
        class FromBiConsumer extends AbstractProgressFromBiConsumerSpecification {}

        @Nested
        @DisplayName("From bi-consumer with scheduler")
        class FromBiConsumerWithScheduler extends AbstractProgressFromBiConsumerWithSchedulerSpecification {}

        @Nested
        @DisplayName("From bi-consumer with CanExecute")
        class FromBiConsumerWithCanExecute extends AbstractProgressFromBiConsumerWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From bi-consumer with CanExecute and scheduler")
        class FromBiConsumerWithCanExecuteAndScheduler extends AbstractProgressFromBiConsumerWithCanExecuteAndSchedulerSpecification {}

        @Nested
        @DisplayName("From bi-function")
        class FromBiFunction extends AbstractProgressFromBiFunctionSpecification {}

        @Nested
        @DisplayName("From bi-function with scheduler")
        class FromBiFunctionWithScheduler extends AbstractProgressFromBiFunctionWithSchedulerSpecification {}

        @Nested
        @DisplayName("From bi-function with CanExecute")
        class FromBiFunctionWithCanExecute extends AbstractProgressFromBiFunctionWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From bi-function with CanExecute and scheduler")
        class FromBiFunctionWithCanExecuteAndScheduler extends AbstractProgressFromBiFunctionWithCanExecuteAndSchedulerSpecification {}
    }

    @Nested
    @DisplayName("When new reactive composite command is created")
    class WhenCreateComposite implements CompositeFromCommandsSpecification
    {
        @Nested
        @DisplayName("From no commands")
        class FromNoCommands extends AbstractCompositeFromNoCommandsSpecification {}

        @Nested
        @DisplayName("From no commands with CanExecute")
        class FromNoCommandsWithCanExecute extends AbstractCompositeFromNoCommandsWithCanExecuteSpecification {}

        @Nested
        @DisplayName("From commands with no input")
        class FromCommandsWithNoInput extends AbstractCompositeFromCommandsWithNoInputSpecification {}

        @Nested
        @DisplayName("From commands with input")
        class FromCommandsWithInput extends AbstractCompositeFromCommandsWithInputSpecification {}

        @Nested
        @DisplayName("From commands with CanExecute")
        class FromCommandsWithCanExecute extends AbstractCompositeFromCommandsWithCanExecuteSpecification {}
    }
}
