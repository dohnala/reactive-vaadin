package com.github.dohnal.vaadin.reactive.command;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.vaadin.reactive.Progress;
import com.github.dohnal.vaadin.reactive.ReactiveProperty;

/**
 * Implementation of {@link Progress} which sets values to {@link ReactiveProperty}
 *
 * @author dohnal
 */
public final class ReactiveProgress implements Progress
{
    private final ReactiveProperty<Float> property;

    public ReactiveProgress(final @Nonnull ReactiveProperty<Float> property)
    {
        this.property = property;
    }

    @Override
    public void set(float value)
    {
        final Float currentValue = property.getValue();

        final Float valueToSet = Math.max(Math.min(value, 1.0f), currentValue != null ? currentValue : 0.0f);

        if (!Objects.equals(currentValue, valueToSet))
        {
            property.setValue(valueToSet);
        }
    }

    @Override
    public void add(float value)
    {
        final Float currentValue = property.getValue();

        set((currentValue != null ? currentValue : 0.0f) + value);
    }
}
