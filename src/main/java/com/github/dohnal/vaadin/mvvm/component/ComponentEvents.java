package com.github.dohnal.vaadin.mvvm.component;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.Event;
import com.github.dohnal.vaadin.mvvm.component.event.ButtonClickedEvent;
import com.github.dohnal.vaadin.mvvm.component.event.FieldValueChangedEvent;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;

/**
 * List of all component events
 *
 * @author dohnal
 */
public interface ComponentEvents
{
    /**
     * Returns an event which will happen when user click on given button
     *
     * @param button button
     * @return event
     */
    @Nonnull
    default Event<Button.ClickEvent> clickedOn(final @Nonnull Button button)
    {
        return new ButtonClickedEvent(button);
    }

    /**
     * Returns an event which will happen when user changes value of given field
     *
     * @param field field
     * @param <T> type of value in field
     * @return event
     */
    @Nonnull
    default <T> Event<T> valueChangedOf(final @Nonnull HasValue<T> field)
    {
        return new FieldValueChangedEvent<>(field);
    }
}
