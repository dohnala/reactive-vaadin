package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.component.ComponentEvents;
import com.github.dohnal.vaadin.mvvm.component.ComponentProperties;
import com.github.dohnal.vaadin.reactive.command.CommandActions;
import com.vaadin.ui.CustomComponent;

/**
 * Base class for all view in MVVM pattern
 *
 * @param <M> type of view model
 * @author dohnal
 */
public abstract class AbstractView<M extends AbstractViewModel>
        extends CustomComponent
        implements ViewBinder, ComponentEvents, ComponentProperties, CommandActions
{
    private M viewModel;

    /**
     * Creates view for given view model
     *
     * @param viewModel view model
     */
    public AbstractView(final @Nonnull M viewModel)
    {
        this.viewModel = viewModel;
    }

    @Nonnull
    @Override
    public AbstractView unbind()
    {
        return this;
    }
}
