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
