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

package com.github.dohnal.vaadin.mvvm;

import javax.annotation.Nonnull;

import com.github.dohnal.vaadin.mvvm.component.ComponentEvents;
import com.github.dohnal.vaadin.mvvm.component.ComponentProperties;
import com.github.dohnal.vaadin.reactive.CommandActions;
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
}
