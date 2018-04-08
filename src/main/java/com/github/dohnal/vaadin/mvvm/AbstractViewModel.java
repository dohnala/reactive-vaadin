package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.reactive.command.CommandActions;

/**
 * Base class for all view models in MVVM pattern
 *
 * @author dohnal
 */
public class AbstractViewModel implements ViewModelBinder, CommandActions
{
    @Nonnull
    @Override
    public AbstractViewModel unbind()
    {
        return this;
    }
}
