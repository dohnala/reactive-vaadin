package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

import com.github.dohnal.vaadin.reactive.ReactiveCommand;
import rx.Observable;

/**
 * Synchronous implementation of {@link ReactiveCommand}
 *
 * @param <T> type of command input parameter
 * @param <R> type of command result
 * @author dohnal
 */
public final class SyncCommand<T, R> extends AbstractCommand<T, R>
{
    protected final Function<T, R> execution;

    /**
     * Creates new synchronous reactive command with given execution
     *
     * @param canExecute observable which controls command executability
     * @param execution execution
     */
    public SyncCommand(final @Nonnull Observable<Boolean> canExecute,
                       final @Nonnull Function<T, R> execution)
    {
        super(canExecute);

        this.execution = execution;
    }

    @Override
    public final void executeInternal(final @Nullable T input)
    {
        try
        {
            this.progress.setValue(0.0f);
            this.isExecuting.setValue(true);

            handleResult(execution.apply(input), null);
        }
        catch (final Throwable error)
        {
            handleResult(null, error);
        }
        finally
        {
            this.progress.setValue(1.0f);
            this.isExecuting.setValue(false);
            this.progress.setValue(0.0f);
        }
    }
}
