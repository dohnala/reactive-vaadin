package com.github.dohnal.vaadin.reactive.command.progress;

import javax.annotation.Nonnull;
import java.util.List;

import com.github.dohnal.vaadin.reactive.command.ProgressCommand;
import com.github.dohnal.vaadin.reactive.command.async.AbstractAsyncCommandTest;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Base tests for {@link ProgressCommand}
 *
 * @author dohnal
 */
public class AbstractProgressCommandTest extends AbstractAsyncCommandTest
{
    /**
     * Tests which verify behavior of progress command during execution which finishes successfully
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class DuringExecuteProgressCommand<T, R> extends DuringExecuteCommand<T, R>
    {
        @Nonnull
        protected abstract Float[] getProgress();

        @Test
        @Override
        @DisplayName("Progress observable should emit correct progress values and then reset back to 0")
        public void testProgress()
        {
            final List<Float> progress = Lists.newArrayList(getProgress());
            progress.add(0.0f);

            getCommand().getProgress().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(progress.toArray(new Float[progress.size()]));
        }
    }

    /**
     * Tests which verify behavior of progress command during execution which fails with error
     *
     * @param <T> type of command input
     * @param <R> type of command result
     */
    protected abstract class DuringExecuteProgressCommandWithError<T, R> extends DuringExecuteCommandWithError<T, R>
    {
        @Nonnull
        protected abstract Float[] getProgress();

        @Test
        @DisplayName("Progress observable should emit correct progress values, then 1 and then reset back to 0")
        public void testProgress()
        {
            getCommand().getError().test();

            final List<Float> progress = Lists.newArrayList(getProgress());
            progress.add(1.0f);
            progress.add(0.0f);

            getCommand().getProgress().test()
                    .perform(() -> getCommand().execute(getInput()))
                    .assertValues(progress.toArray(new Float[progress.size()]));
        }
    }
}
