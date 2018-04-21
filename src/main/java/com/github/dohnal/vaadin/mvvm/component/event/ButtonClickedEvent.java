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
