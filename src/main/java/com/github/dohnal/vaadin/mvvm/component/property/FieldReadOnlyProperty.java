package com.github.dohnal.vaadin.mvvm.component.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.vaadin.ui.AbstractField;

/**
 * @author dohnal
 */
public final class FieldReadOnlyProperty extends AbstractComponentProperty<Boolean>
{
    private final AbstractField<?> field;

    public FieldReadOnlyProperty(final @Nonnull AbstractField<?> field)
    {
        this.field = field;
    }

    @Override
    public final void setValue(final @Nullable Boolean value)
    {
        withUIAccess(field, () -> field.setReadOnly(Boolean.TRUE.equals(value)));
    }
}
