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

package com.github.dohnal.vaadin.reactive.command.factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import com.github.dohnal.vaadin.reactive.ReactiveCommandFactory;
import com.github.dohnal.vaadin.reactive.command.CompositeCanExecuteEmitsValueSpecification;
import com.github.dohnal.vaadin.reactive.command.CompositeExecutionSpecification;
import com.github.dohnal.vaadin.reactive.command.CreateSpecification;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Specification for {@link ReactiveCommand} created by
 * {@link ReactiveCommandFactory#createCompositeCommand(List)}
 * {@link ReactiveCommandFactory#createCompositeCommand(Observable, List)}
 *
 * @author dohnal
 */
public interface CompositeFromCommandsSpecification extends
        CreateSpecification,
        CompositeExecutionSpecification,
        CompositeCanExecuteEmitsValueSpecification
{
    abstract class AbstractCompositeFromNoCommandsSpecification implements ReactiveCommandFactory
    {
        @Test
        @DisplayName("IllegalArgumentException should be thrown")
        public void testCreate()
        {
            assertThrows(IllegalArgumentException.class, () -> createCompositeCommand(new ArrayList<>()));
        }
    }

    abstract class AbstractCompositeFromNoCommandsWithCanExecuteSpecification implements ReactiveCommandFactory
    {
        @Test
        @DisplayName("IllegalArgumentException should be thrown")
        public void testCreate()
        {
            assertThrows(IllegalArgumentException.class, () -> createCompositeCommand(
                    PublishSubject.create(), new ArrayList<>()));
        }
    }

    abstract class AbstractCompositeFromCommandsWithNoInputSpecification extends
            AbstractCreateSpecification<Void, List<Integer>> implements ReactiveCommandFactory
    {
        private Supplier<Integer> executionA;
        private ReactiveCommand<Void, Integer> commandA;

        private Supplier<Integer> executionB;
        private ReactiveCommand<Void, Integer> commandB;

        private ReactiveCommand<Void, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            executionA = Mockito.mock(Supplier.class);
            commandA = createCommand(executionA);

            executionB = Mockito.mock(Supplier.class);
            commandB = createCommand(executionB);

            command = createCompositeCommand(Arrays.asList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, List<Integer>> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Child commands should not be executed")
        public void testChildCommands()
        {
            Mockito.verify(executionA, Mockito.never()).get();
            Mockito.verify(executionB, Mockito.never()).get();
        }

        @Nested
        @DisplayName("Execute specification")
        class Execute extends AbstractCompositeExecuteSpecification<Void, List<Integer>>
        {
            private final Integer RESULT_A = 7;
            private final Integer RESULT_B = 9;
            private final List<Integer> RESULT = Arrays.asList(RESULT_A, RESULT_B);
            private final Throwable ERROR_A = new RuntimeException("Error A");
            private final Throwable ERROR_B = new RuntimeException("Error B");

            @Nonnull
            @Override
            public ReactiveCommand<Void, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                Mockito.when(executionA.get()).thenReturn(RESULT_A);
                Mockito.when(executionB.get()).thenReturn(RESULT_B);
                command.execute();
            }

            @Override
            protected void executeWithError()
            {
                Mockito.when(executionA.get()).thenThrow(ERROR_A);
                Mockito.when(executionB.get()).thenReturn(RESULT_B);
                command.execute();
            }

            @Override
            protected void executeWithMultipleErrors()
            {
                Mockito.when(executionA.get()).thenThrow(ERROR_A);
                Mockito.when(executionB.get()).thenThrow(ERROR_B);
                command.execute();
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void executeChild()
            {
                Mockito.reset(executionA);
                Mockito.when(executionA.get()).thenReturn(RESULT_A);
                commandA.execute();
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void executeChildWithError()
            {
                Mockito.reset(executionA);
                Mockito.when(executionA.get()).thenThrow(ERROR_A);
                commandA.getError().test();
                commandA.execute();
            }

            @Nullable
            @Override
            protected List<Integer> getResult()
            {
                return RESULT;
            }

            @Nullable
            @Override
            protected Throwable getError()
            {
                return ERROR_A;
            }

            @Nonnull
            @Override
            protected Throwable[] getErrors()
            {
                return new Throwable[]{ERROR_A, ERROR_B};
            }

            @Override
            protected void verifyExecution()
            {
                Mockito.verify(executionA).get();
                Mockito.verify(executionB).get();
            }
        }
    }

    abstract class AbstractCompositeFromCommandsWithInputSpecification extends
            AbstractCreateSpecification<Integer, List<Integer>> implements ReactiveCommandFactory
    {
        private Function<Integer, Integer> executionA;
        private ReactiveCommand<Integer, Integer> commandA;

        private Function<Integer, Integer> executionB;
        private ReactiveCommand<Integer, Integer> commandB;

        private ReactiveCommand<Integer, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            executionA = Mockito.mock(Function.class);
            commandA = createCommand(executionA);

            executionB = Mockito.mock(Function.class);
            commandB = createCommand(executionB);

            command = createCompositeCommand(Arrays.asList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Integer, List<Integer>> getCommand()
        {
            return command;
        }

        @Test
        @DisplayName("Child commands should not be executed")
        public void testChildCommands()
        {
            Mockito.verify(executionA, Mockito.never()).apply(Mockito.anyInt());
            Mockito.verify(executionB, Mockito.never()).apply(Mockito.anyInt());
        }

        @Nested
        @DisplayName("When command is executed with no input")
        class ExecuteWithNoInput
        {
            @Test
            @DisplayName("NullPointerException should be thrown")
            public void testExecute()
            {
                assertThrows(NullPointerException.class, () -> command.execute());
            }
        }

        @Nested
        @DisplayName("Execute specification")
        class Execute extends AbstractCompositeExecuteSpecification<Integer, List<Integer>>
        {
            private final Integer INPUT = 5;
            private final Integer RESULT_A = 7;
            private final Integer RESULT_B = 9;
            private final List<Integer> RESULT = Arrays.asList(RESULT_A, RESULT_B);
            private final Throwable ERROR_A = new RuntimeException("Error A");
            private final Throwable ERROR_B = new RuntimeException("Error B");

            @Nonnull
            @Override
            public ReactiveCommand<Integer, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void execute()
            {
                Mockito.when(executionA.apply(INPUT)).thenReturn(RESULT_A);
                Mockito.when(executionB.apply(INPUT)).thenReturn(RESULT_B);
                command.execute(INPUT);
            }

            @Override
            protected void executeWithError()
            {
                Mockito.when(executionA.apply(INPUT)).thenThrow(ERROR_A);
                Mockito.when(executionB.apply(INPUT)).thenReturn(RESULT_B);
                command.execute(INPUT);
            }

            @Override
            protected void executeWithMultipleErrors()
            {
                Mockito.when(executionA.apply(INPUT)).thenThrow(ERROR_A);
                Mockito.when(executionB.apply(INPUT)).thenThrow(ERROR_B);
                command.execute(INPUT);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void executeChild()
            {
                Mockito.reset(executionA);
                Mockito.when(executionA.apply(INPUT)).thenReturn(RESULT_A);
                commandA.execute(INPUT);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void executeChildWithError()
            {
                Mockito.reset(executionA);
                Mockito.when(executionA.apply(INPUT)).thenThrow(ERROR_A);
                commandA.getError().test();
                commandA.execute(INPUT);
            }

            @Nullable
            @Override
            protected List<Integer> getResult()
            {
                return RESULT;
            }

            @Nullable
            @Override
            protected Throwable getError()
            {
                return ERROR_A;
            }

            @Nonnull
            @Override
            protected Throwable[] getErrors()
            {
                return new Throwable[]{ERROR_A, ERROR_B};
            }

            @Override
            protected void verifyExecution()
            {
                Mockito.verify(executionA).apply(INPUT);
                Mockito.verify(executionB).apply(INPUT);
            }
        }
    }

    abstract class AbstractCompositeFromCommandsWithCanExecuteSpecification
            extends AbstractCreateSpecification<Void, List<Integer>> implements ReactiveCommandFactory
    {
        private Supplier<Integer> executionA;
        private ReactiveCommand<Void, Integer> commandA;

        private Supplier<Integer> executionB;
        private ReactiveCommand<Void, Integer> commandB;

        private TestScheduler testScheduler;
        private PublishSubject<Boolean> testSubject;
        private ReactiveCommand<Void, List<Integer>> command;

        @BeforeEach
        @SuppressWarnings("unchecked")
        void create()
        {
            executionA = Mockito.mock(Supplier.class);
            commandA = createCommand(executionA);

            executionB = Mockito.mock(Supplier.class);
            commandB = createCommand(executionB);

            testScheduler = new TestScheduler();
            testSubject = PublishSubject.create();
            testSubject.observeOn(testScheduler);
            command = createCompositeCommand(testSubject, Arrays.asList(commandA, commandB));
        }

        @Nonnull
        @Override
        public ReactiveCommand<Void, List<Integer>> getCommand()
        {
            return command;
        }

        @Nested
        @DisplayName("CanExecute emits value specification")
        class CanExecuteEmitsValue extends AbstractCompositeCanExecuteEmitsValueSpecification<Void, List<Integer>>
        {
            private final Integer RESULT_A = 7;
            private final Integer RESULT_B = 9;
            private final List<Integer> RESULT = Arrays.asList(RESULT_A, RESULT_B);

            @Nonnull
            @Override
            public ReactiveCommand<Void, List<Integer>> getCommand()
            {
                return command;
            }

            @Override
            protected void emitValue(final @Nonnull Boolean value)
            {
                testSubject.onNext(value);
                testScheduler.triggerActions();
            }

            @Override
            protected void execute()
            {
                Mockito.when(executionA.get()).thenReturn(RESULT_A);
                Mockito.when(executionB.get()).thenReturn(RESULT_B);
                command.execute();
            }

            @Override
            protected void executeChild()
            {
                Mockito.when(executionA.get()).thenReturn(RESULT_A);
                commandA.execute();
            }
        }
    }
}
