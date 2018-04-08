package com.github.dohnal.vaadin.mvvm.component.event;

import javax.annotation.Nonnull;

import com.vaadin.ui.Button;
import rx.Observable;

/**
 * @author dohnal
 */
public final class ButtonClickedEvent extends AbstractComponentEvent<Button.ClickEvent>
{
    private final Button button;

    public ButtonClickedEvent(final @Nonnull Button button)
    {
        this.button = button;
    }

    @Nonnull
    @Override
    public final Observable<Button.ClickEvent> asObservable()
    {
        return toObservable(event -> event::accept, button::addClickListener);
    }
}
