package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vaadin.ui.ProgressBar;

/**
 * @author dohnal
 */
public final class ProgressBarValueProperty extends AbstractComponentProperty<Float>
{
    private final ProgressBar progressBar;

    public ProgressBarValueProperty(final @Nonnull ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    @Override
    public final void setValue(final @Nullable Float value)
    {
        withUIAccess(progressBar, () -> progressBar.setValue(value != null ? value : 0.0f));
    }
}
